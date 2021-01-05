<div align="center">

<h1>Abyssal Command Engine (ACE)</h1>

![badge](https://github.com/abyssal/command-engine/workflows/Gradle/badge.svg)

<code>com.abyssaldev.commands</code>


A [JDA](https://github.com/DV8FromTheWorld/JDA) Kotlin commands framework for building pipeline-based, scalable Discord bots.  
Automatic module discovery, parameter reading, and slash commands.  
The brains behind the [Abyss](https://github.com/abyssal/abyss) bot project.

</div>

## Feature highlights
- Automatic command discovery through Kotlin annotations
- Quick development cycle & less boilerplate with function commands
- Custom pre-execution checks with `ArgumentContract`/`CommandContract`
- Automatic type and argument parsing
  - User can add their own custom type parsers
- (Experimental) Support for slash-commands/interactions

## A quick example
ACE is used extensively in [Abyss](abyssal/Abyss), especially in [AbyssEngine](https://github.com/abyssal/abyss/blob/v16-kt/src/main/kotlin/com/abyssaldev/abyss/AbyssEngine.kt#L93). An example module is available at [AdminModule](https://github.com/abyssal/abyss/blob/v16-kt/src/main/kotlin/com/abyssaldev/abyss/commands/gateway/AdminModule.kt).
```kt
// ExampleModule.kt
@Name("Example")
@Description("An example module.")
class ExampleModule: CommandModule() {
  @GatewayCommand(name = "ping", description = "Pong!")
  fun pingCommand(call: GatewayCommandRequest): GatewayCommandResponse = respond {
    content("Pong!")
  }
  
  @GatewayCommand(name = "info", description = "Snoops on a user.")
  @CommandContract(CommandContracts.REQUIRE_GUILD)
  fun infoCommand(call: GatewayCommandRequest, member: net.dv8tion.jda.api.entities.Member) = respond {
    content("Snooping on ${member.toString()}.")
  }
}

class Listener(val engine: CommandEngine) : ListenerAdapter() {
  override fun onMessageReceived(event: MessageReceivedEvent) {
    GlobalScope.launch {
      if (!event.message.contentRaw.startsWith(prefix, ignoreCase = true)) return
      val result = engine.executeSuspending(
        event.message.contentRaw.substring(prefix.length),
        GatewayCommandRequest(event.message)
      )
      
      if (!result.isSuccess) {
        event.channel.sendMessage(result.reason).queue()
      }
    }
  }
}

// Bot.kt
val engine = CommandEngine.Builder().apply {
  addModules(ExampleModule())
}.build()
jdaBuilder.addListener(Listener(engine))
```

### Copyright
Copyright &copy; 2021 Abyssal and contributors, under the [MIT License](LICENSE.md).  
Parts of the Abyssal Command Engine code are taken from [abyssal/Abyss](https://github.com/abyssal/abyss) with permission.  
Aspects of the Abyssal Command Engine pipeline design are inspired by [Discord.Net.Commands](https://github.com/discord-net/Discord.Net/tree/dev/src/Discord.Net.Commands) and [Qmmands](https://github.com/Quahu/Qmmands).
