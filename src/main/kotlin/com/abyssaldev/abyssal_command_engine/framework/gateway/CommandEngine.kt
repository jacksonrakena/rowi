package com.abyssaldev.abyssal_command_engine.framework.gateway

import com.abyssaldev.abyssal_command_engine.framework.gateway.command.GatewayCommandInternal
import com.abyssaldev.abyss.framework.gateway.GatewayCommandRequest
import com.abyssaldev.abyssal_command_engine.framework.common.CommandModule
import com.abyssaldev.abyssal_command_engine.framework.common.reflect.Description
import com.abyssaldev.abyssal_command_engine.framework.common.reflect.Name
import com.abyssaldev.abyssal_command_engine.framework.gateway.reflect.ArgumentContract
import com.abyssaldev.abyssal_command_engine.framework.gateway.reflect.GatewayCommand
import com.abyssaldev.abyssal_command_engine.framework.gateway.command.GatewayCommandFailure
import com.abyssaldev.abyssal_command_engine.framework.gateway.command.GatewayCommandParameter
import com.abyssaldev.abyssal_command_engine.framework.gateway.event.CommandEngineEventHandler
import com.abyssaldev.abyssal_command_engine.framework.gateway.event.DefaultCommandEngineEventHandler
import com.abyssaldev.abyssal_command_engine.framework.gateway.prefix.PrefixStrategy
import com.abyssaldev.abyssal_command_engine.framework.gateway.prefix.StaticPrefixStrategy
import com.abyssaldev.abyssal_command_engine.util.Loggable
import com.abyssaldev.abyssal_command_engine.util.getAnnotation
import com.abyssaldev.abyssal_command_engine.util.getAnnotations
import com.abyssaldev.abyssal_command_engine.util.trySendMessage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import net.dv8tion.jda.api.MessageBuilder
import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import java.util.*
import kotlin.reflect.KParameter
import kotlin.reflect.full.memberFunctions

/**
 * A Discord event listener that can handle commands received over the gateway.
 */
class CommandEngine private constructor(
    /**
     * The current [PrefixStrategy].
     */
    val prefixStrategy: PrefixStrategy,

    /**
     * The [CommandModule]s in use by this engine.
     */
    val modules: List<CommandModule>,

    /**
     * The owner of this [CommandEngine].
     */
    val ownerId: String,

    /**
     * The event handler of this [CommandEngine].
     */
    val eventHandler: CommandEngineEventHandler
): Loggable, ListenerAdapter() {
    private val commands: List<GatewayCommandInternal>

    init {
        val gatewayCommands = mutableListOf<GatewayCommandInternal>()
        modules.forEach {
            it::class.memberFunctions.forEach { member ->
                val annot = member.annotations.getAnnotation<GatewayCommand>() ?: return@forEach
                val parameters = member.parameters.filter { param ->
                    param.kind == KParameter.Kind.VALUE && param.name != null
                }.map { param ->
                    GatewayCommandParameter(
                        name = param.annotations.getAnnotation<Name>()?.name ?: param.name!!,
                        description = param.annotations.getAnnotation<Description>()?.description ?: "",
                        type = param.type,
                        contracts = param.annotations.getAnnotations()
                    )
                }
                val commandInternal = GatewayCommandInternal(
                    name = annot.name,
                    description = annot.description,
                    isBotOwnerRestricted = annot.isOwnerRestricted,
                    requiredBotPermissions = EnumSet.noneOf(Permission::class.java)!!,
                    requiredUserPermissions = EnumSet.noneOf(Permission::class.java)!!,
                    requiredRole = annot.requiredRole,
                    invoke = member,
                    parentModule = it,
                    parameters = parameters
                )
                gatewayCommands.add(commandInternal)
                eventHandler.onGatewayCommandRegistered(commandInternal)
            }
        }
        commands = gatewayCommands
    }

    override fun onMessageReceived(event: MessageReceivedEvent) {
        GlobalScope.launch(Dispatchers.Default) {
            handleMessageReceived(event)
        }
    }

    private suspend fun handleMessageReceived(event: MessageReceivedEvent) {
        if (event.author.isBot) return

        var content = event.message.contentRaw
        val prefix = prefixStrategy.getPrefix(event.guild)

        if (!content.startsWith(prefix, true)) return
        content = content.substring(prefix.length)

        val contentSplit = content.split(" ")

        val commandToken = contentSplit[0]
        val command = commands.firstOrNull { it.isMatch(commandToken) } ?: return

        val request = GatewayCommandRequest(event.guild, event.textChannel, event.member, event.author, contentSplit.drop(1), event.jda, event.message)

        // Bot owner check
        if (command.isBotOwnerRestricted && (request.user.id != ownerId)) {
            return handleOwnerOnlyCommandError(command, request)
        }

        if (request.member != null) {
            // Required role check
            if (command.requiredRole.isNotEmpty() && request.member.roles.none { it.id == command.requiredRole }) {
                return handleMissingRequiredRole(command, request, command.requiredRole!!)
            }

            // Caller permissions
            if (!request.member.hasPermission(command.requiredUserPermissions)) {
                return handleMissingRequiredUserPermissions(command, request, command.requiredUserPermissions)
            }

            // Bot permissions
            if (!request.botMember!!.hasPermission(command.requiredBotPermissions)) {
                return handleMissingRequiredBotPermissions(command, request, command.requiredBotPermissions)
            }
        }

        // Contracts
        //
        // TODO("this")

        // Finalization
        try {
            val canInvoke = command.canInvoke(request)
            if (!canInvoke.isNullOrEmpty()) {
                eventHandler.onGatewayCommandFailure(command, request, GatewayCommandFailure.FailedCanInvokeCheck, canInvoke)
                return
            }
            val message = command.invoke(request)
            if (message != null) request.channel.trySendMessage(message.build())
        } catch (e: Throwable) {
            logger.error("Error thrown while processing gateway command ${command.name}", e)
            request.channel.trySendMessage( "There was an internal error running that command. Try again later.")
            return
        }
    }

    private fun handleOwnerOnlyCommandError(command: GatewayCommandInternal, call: GatewayCommandRequest) {
        eventHandler.onGatewayCommandFailure(command, call, GatewayCommandFailure.FailedOwnerRestrictionCheck, "")
    }

    private fun handleMissingRequiredRole(command: GatewayCommandInternal, call: GatewayCommandRequest, roleId: String) {
        eventHandler.onGatewayCommandFailure(command, call, GatewayCommandFailure.MissingUserRole, call.guild!!.getRoleById(roleId)?.name ?: "")
    }

    private fun handleMissingRequiredUserPermissions(command: GatewayCommandInternal, call: GatewayCommandRequest, permissions: EnumSet<Permission>) {
        eventHandler.onGatewayCommandFailure(command, call, GatewayCommandFailure.MissingUserPermissions, permissions.map { "`${it.name}`" }.joinToString(", "))
    }

    private fun handleMissingRequiredBotPermissions(command: GatewayCommandInternal, call: GatewayCommandRequest, permissions: EnumSet<Permission>) {
        eventHandler.onGatewayCommandFailure(command, call, GatewayCommandFailure.MissingBotPermissions, permissions.map { "`${it.name}`" }.joinToString(", "))
    }

    /**
     * A builder pattern for [CommandEngine] instances.
     */
    class Builder {
        private var prefixStrategy: PrefixStrategy = StaticPrefixStrategy("!")
        private var modules: MutableList<CommandModule> = mutableListOf()
        private var ownerId: String = ""
        private var engineEventHandler: CommandEngineEventHandler = DefaultCommandEngineEventHandler()

        /**
         * Sets the prefix strategy for the resulting [CommandEngine].
         * Defaults to a [StaticPrefixStrategy] with prefix `!`.
         */
        fun setPrefixStrategy(prefixStrategy: PrefixStrategy): Builder {
            this.prefixStrategy = prefixStrategy
            return this
        }

        /**
         * Adds the supplied [CommandModule] to the resulting [CommandEngine].
         * These modules will be searched for commands.
         */
        fun addModules(vararg modulesToAdd: CommandModule): Builder {
            this.modules.addAll(modulesToAdd)
            return this
        }

        /**
         * Sets the IDs of the owners of the resulting [CommandEngine].
         * This field is required.
         */
        fun setOwnerId(ownerId: String): Builder {
            this.ownerId = ownerId
            return this
        }

        /**
         * Sets the [CommandEngineEventHandler] to use.
         */
        fun setEventHandler(eventHandler: CommandEngineEventHandler): Builder {
            this.engineEventHandler = eventHandler
            return this
        }

        /**
         * Creates a [CommandEngine] instance from this [CommandEngine.Builder].
         */
        fun build(): CommandEngine {
            if (this.ownerId.isEmpty()) throw IllegalArgumentException("CommandEngine.Builder.ownerId is a required property.")
            return CommandEngine(
                prefixStrategy = this.prefixStrategy,
                modules = this.modules,
                ownerId = this.ownerId,
                eventHandler = this.engineEventHandler
            )
        }
    }
}