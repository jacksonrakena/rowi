<div align="center">

# Abyssal Command Engine (ACE)
An advanced Kotlin commands framework for [JDA](https://github.com/DV8FromTheWorld/JDA).  
Automatic module discovery, parameter reading, and slash commands.  
The brains behind the [Abyss](https://github.com/abyssal/abyss) bot project.

</div>

## Feature highlights
- Automatic command discovery through Kotlin annotations
- Quick development cycle with function commands
- Customisable per-guild, per-channel, per-user, or global prefixes with `PrefixStrategy`

## A quick example
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

// Bot.kt
val engine = CommandEngine.Builder().apply {
  setPrefixStrategy(StaticPrefixStrategy("!"))
  addModules(ExampleModule())
}.build()
jdaBuilder.addListener(engine)
```

### Copyright
Copyright &copy; 2021 Abyssal and contributors, under the [MIT License](LICENSE.md).
Parts of the Abyssal Command Engine code are taken from [abyssal/Abyss](https://github.com/abyssal/abyss) with permission.
