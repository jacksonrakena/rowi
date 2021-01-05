<div align="center">

<h1>Rowi</h1>

![badge](https://github.com/abyssal/rowi/workflows/Gradle/badge.svg)

<code>com.abyssaldev.rowi</code>

A Kotlin framework for building platform-independent command responders.  
Automatic module discovery, parameter/type reading, and heavy customization.  
The brains behind the [Abyss](https://github.com/abyssal/abyss) Discord bot.  
Named after the [Okarito kiwi.](https://en.wikipedia.org/wiki/Okarito_kiwi)

</div>

## Feature highlights
- Automatic command discovery through Kotlin annotations
- Quick development cycle & less boilerplate with function commands
- Custom pre-execution checks with `ArgumentContract`/`CommandContract`
- Overridable command executables with `CommandExecutable<*>`
- Automatic type and argument parsing
  - User can add their own custom type parsers
- Discord support with `com.abyssaldev.rowi.jda` (for [JDA](https://github.com/dv8fromtheworld/jda)) or `com.abyssaldev.rowi.catnip` (for [catnip](https://github.com/mewna/catnip))

## A quick example
Rowi is used extensively in [Abyss](abyssal/Abyss), especially in [AbyssEngine](https://github.com/abyssal/abyss/blob/v16-kt/src/main/kotlin/com/abyssaldev/abyss/AbyssEngine.kt#L93). An example module is available at [AdminModule](https://github.com/abyssal/abyss/blob/v16-kt/src/main/kotlin/com/abyssaldev/abyss/commands/gateway/AdminModule.kt).
```
This example is coming soon.
```

### Copyright
Copyright &copy; 2021 Abyssal and contributors, under the [MIT License](LICENSE.md).  
Aspects of Rowi pipeline design are inspired by [Discord.Net.Commands](https://github.com/discord-net/Discord.Net/tree/dev/src/Discord.Net.Commands) and [Qmmands](https://github.com/Quahu/Qmmands).
