package com.abyssaldev.commands

import com.abyssaldev.commands.gateway.command.GatewayCommandInstance
import com.abyssaldev.commands.common.CommandModule
import com.abyssaldev.commands.common.CommandRequest
import com.abyssaldev.commands.common.reflect.Description
import com.abyssaldev.commands.common.reflect.Name
import com.abyssaldev.commands.gateway.command.GatewayCommand
import com.abyssaldev.commands.gateway.command.GatewayCommandParameter
import com.abyssaldev.commands.gateway.prefix.PrefixStrategy
import com.abyssaldev.commands.gateway.prefix.StaticPrefixStrategy
import com.abyssaldev.commands.common.Result
import com.abyssaldev.commands.common.reflect.Remainder
import com.abyssaldev.commands.gateway.GatewayCommandRequest
import com.abyssaldev.commands.gateway.common.SuppliedArgument
import com.abyssaldev.commands.gateway.contracts.ArgumentContract
import com.abyssaldev.commands.gateway.contracts.ArgumentContractable
import com.abyssaldev.commands.gateway.contracts.impl.DefaultArgumentContracts
import com.abyssaldev.commands.gateway.contracts.impl.NotBotContract
import com.abyssaldev.commands.gateway.contracts.impl.NotCallerContract
import com.abyssaldev.commands.gateway.results.*
import com.abyssaldev.commands.gateway.types.TypeParser
import com.abyssaldev.commands.gateway.types.impl.*
import com.abyssaldev.commands.util.Loggable
import com.abyssaldev.commands.util.getAnnotation
import com.abyssaldev.commands.util.getAnnotations
import com.abyssaldev.commands.util.trySendMessage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import org.apache.commons.lang3.time.DateFormatUtils
import java.time.Instant
import java.util.*
import kotlin.reflect.KParameter
import kotlin.reflect.full.createType
import kotlin.reflect.full.isSubtypeOf
import kotlin.reflect.full.memberFunctions
import kotlin.reflect.jvm.jvmErasure

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
     * The type parsers of this [CommandEngine].
     */
    val typeParsers: List<TypeParser<*>>,

    /**
     * The available [ArgumentContractable] instances.
     */
    val argumentContracts: HashMap<String, ArgumentContractable<*>>,

    /**
     * Sets whether to respond to prefix calls with a "command not found"
     * message.
     */
    val showCommandNotFoundError: Boolean
): Loggable, ListenerAdapter() {
    private val commands: List<GatewayCommandInstance>
    private val adapter = CommandEngineAdapter(this)

    init {
        val gatewayCommands = mutableListOf<GatewayCommandInstance>()
        modules.forEach {
            it::class.memberFunctions.forEach { member ->
                val annot = member.annotations.getAnnotation<GatewayCommand>() ?: return@forEach
                val parameters = member.parameters.filter { param ->
                    param.kind == KParameter.Kind.VALUE && param.name != null && !param.type.isSubtypeOf(CommandRequest::class.createType())
                }.map { param ->
                    logger.info("${param.name ?: "noname"} Param type ${param.type.toString()}")
                    GatewayCommandParameter(
                        name = param.annotations.getAnnotation<Name>()?.name ?: param.name!!,
                        description = param.annotations.getAnnotation<Description>()?.description ?: "",
                        type = param.type.jvmErasure,
                        contracts = param.annotations.getAnnotations<ArgumentContract>().map { c -> c.contractId }
                    )
                }
                GatewayCommandInstance(
                    name = annot.name,
                    description = annot.description,
                    invoke = member,
                    parentModule = it,
                    parameters = parameters
                ).apply(gatewayCommands::add)
            }
        }
        commands = gatewayCommands
    }

    override fun onMessageReceived(event: MessageReceivedEvent) = adapter.onMessageReceived(event)

    fun debugLog(request: GatewayCommandRequest, message: String) {
        request.channel.trySendMessage(":construction: **debug** ${DateFormatUtils.format(Instant.now().toEpochMilli(), "hh:mm a")} - ${message}")
    }

    /**
     * Processes a [MessageReceivedEvent] (including prefixes), and then
     * returns the result. This function does not send a message to any
     * channel (excluding command calls), it only returns what
     * the [CommandEngine] recommends. If you want [CommandEngine] to handle
     * prefixes, add [CommandEngine] to your [ListenerAdapter]
     * in [net.dv8tion.jda.api.JDABuilder].
     */
    suspend fun handleMessage(event: MessageReceivedEvent): Result? {
        if (event.author.isBot) return null

        val content = event.message.contentRaw
        val prefix = prefixStrategy.getPrefix(event.guild)

        if (!content.startsWith(prefix, true)) return null

        return execute(
            content.substring(prefix.length),
            GatewayCommandRequest(event.guild, event.textChannel, event.member, event.author, event.jda, event.message)
        )
    }

    /**
     * Executes a command based on an input [String] and surrounding [GatewayCommandRequest] and returns the [Result].
     * This method assumes that the prefix has been removed from [content]. If you want [CommandEngine] to handle
     * prefixes, add [CommandEngine] to your [ListenerAdapter] in [net.dv8tion.jda.api.JDABuilder], or use the higher-level
     * method [handleMessage].
     */
    suspend fun execute(content: String, request: GatewayCommandRequest): Result {
        val argsRaw = content.split(" ")
        val commandToken = argsRaw[0]
        val command = commands.firstOrNull { it.isMatch(commandToken) } ?: return CommandNotFoundResult(commandToken)
        var args = argsRaw.drop(1)

        // Invocation flags
        val flags = args.filter {it.startsWith("--") }.map { it.replace("--", "") }
        request.flags.addAll(flags)
        if (flags.isNotEmpty()) {
            args = args.filter { !it.startsWith("--") } // a hack, but sue me
        }

        // TODO command contracts

        // Parameters
        if ((command.parameters.size) > args.size) {
            return NotEnoughParametersResult(args.size, command.parameters.size, command)
        }

        val parsedArgs = mutableListOf<Any>()

        parameterParseLoop@ for ((i, parameter) in command.parameters.withIndex()) {
            if (parameter.type == String::class) {
                if (parameter.type.annotations.getAnnotation<Remainder>() != null) {
                    val remainderArg = args.subList(i, args.size).joinToString(" ")
                    parsedArgs.add(remainderArg)
                    break@parameterParseLoop
                }
                parsedArgs.add(args[i])
                continue
            }
            if (parsedArgs.size == command.parameters.size) continue
            val typeParser = typeParsers.firstOrNull {
                it::class.supertypes[0].arguments[0].type!!.isSubtypeOf(parameter.type.createType())
            } ?: return ParameterTypeParserMissingResult(parameter.type)
            if (request.isDebug) debugLog(request, "Parameter `${parameter.name}` matched to type parser `${typeParser::class.qualifiedName}`")
            val parameterValue = args[i]
            try {
                val parsedValueResult = typeParser.parse(parameterValue, request, parameter)
                if (!parsedValueResult.isSuccess) return parsedValueResult
                val parsedValue = parsedValueResult.result!!
                if (request.isDebug) debugLog(request, "Parameter `${parameter.name}` parsed to value:  `${parsedValue}`")

                for (contractId in parameter.contracts) {
                    val contract = this.argumentContracts[contractId] ?: return ArgumentContractMissingResult(contractId)
                    val contractResult = contract::class.memberFunctions.first { it.name == "evaluateContract" }.call(contract, SuppliedArgument(parameter.name, parsedValue), request) as ArgumentContract.Result<*>
                    if (!contractResult.isSuccess) {
                        if (request.isDebug) debugLog(request, "Parameter `${parameter.name}` failed contract `${contract::class.qualifiedName}`")
                        return contractResult
                    } else if (request.isDebug) {
                        debugLog(request, "Parameter `${parameter.name}` passed contract `${contract::class.qualifiedName}`")
                    }
                }
                parsedArgs.add(parsedValue)

            } catch (e: Throwable) {
                return ParameterTypeParserExceptionResult(e, parameter.type)
            }
        }

        // Finalization
        return try {
            if (request.isDebug) debugLog(request, "Invoking command `${command::class.qualifiedName}(name=${command.name})`")
            val message = command.invoke(request, parsedArgs)
            if (message != null) request.channel.trySendMessage(message.build())
            Result(true, null)
        } catch (e: Throwable) {
            if (request.isDebug) debugLog(request, "Command `${command::class.qualifiedName}(name=${command.name})` threw `${e::class.qualifiedName}`")
            CommandExceptionResult(e, command)
        }
    }

    /**
     * A builder pattern for [CommandEngine] instances.
     */
    class Builder {
        private var prefixStrategy: PrefixStrategy = StaticPrefixStrategy("!")
        private var modules: MutableList<CommandModule> = mutableListOf()
        private var typeParsers: MutableList<TypeParser<*>> = mutableListOf(
            IntTypeParser(), BoolTypeParser(), MemberTypeParser(), LongTypeParser()
        )
        private var ownerId: String = ""
        private var argumentContracts: HashMap<String, ArgumentContractable<*>> = hashMapOf(
            DefaultArgumentContracts.NOT_BOT to NotBotContract(),
            DefaultArgumentContracts.NOT_CALLER to NotCallerContract()
        )
        private var showCommandNotFoundError: Boolean = false

        /**
         * Sets whether to respond to prefix calls with a "command not found"
         * message.
         */
        fun setShowCommandNotFoundError(showCommandNotFoundError: Boolean = false): Builder {
            return this.apply {
                this.showCommandNotFoundError = showCommandNotFoundError
            }
        }

        /**
         * Adds the supplied [ArgumentContractable] contracts.
         */
        fun addArgumentContracts(argumentContracts: HashMap<String, ArgumentContractable<*>>): Builder {
            this.argumentContracts.putAll(argumentContracts)
            return this
        }

        /**
         * Registers a type parser for type [T].
         */
        fun <T> addTypeParser(typeParser: TypeParser<T>): Builder {
            this.typeParsers.add(typeParser)
            return this
        }

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
         * Creates a [CommandEngine] instance from this [CommandEngine.Builder].
         */
        fun build(): CommandEngine {
            if (this.ownerId.isEmpty()) throw IllegalArgumentException("CommandEngine.Builder.ownerId is a required property.")
            return CommandEngine(
                prefixStrategy = this.prefixStrategy,
                modules = this.modules,
                ownerId = this.ownerId,
                typeParsers = this.typeParsers,
                argumentContracts = this.argumentContracts,
                showCommandNotFoundError = this.showCommandNotFoundError
            )
        }
    }
}