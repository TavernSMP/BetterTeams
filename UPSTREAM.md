# Upstream integration

This fork integrates booksaw/BetterTeams 5.1.7 at
`09f8c4cb7904b9c7fd129de66fe920acf3b421a7` while targeting Paper/Spigot 1.20.1.
Java 21 or newer is required, as in the previous fork build.

The fork originally had independent Git history. Its Java source matched upstream
5.1.6 (`af50cb8`), so an ancestry-only merge records that baseline before the
normal 5.1.7 merge. The original fork tip is preserved on
`codex/backup-before-upstream-5.1.7` at `23a0d16`.

Keep these fork adjustments when merging future upstream releases:

- Compile against Spigot `1.20-R0.1-SNAPSHOT` and keep `api-version: 1.20`.
- Keep the DecentHolograms `plugin` module dependency and its transitive exclusions.
- Keep explicit Lombok annotation processing.
- Use the 1.20 potion constants `CONFUSION`, `HARM`, and `SLOW_DIGGING`.
- Send the teleport success message only after a successful teleport.

Upstream 5.1.7 moves state-changing commands and events onto the server thread
and removes Folia support. This fork follows that change and removes the stale
`folia-supported` declaration. No configuration or database schema migration is
introduced by this integration.

Run `mvn clean verify` to build the plugin and extensions and run the tests.
The main plugin JAR is written to `server/plugins/BetterTeams-5.1.7.jar`.
The integration passed a clean reactor build on Java 21 with all 46 tests passing.
The packaged descriptor declares API 1.20 and the plugin bytecode targets Java 21.
Before production use, smoke-test on Paper/Spigot 1.20.1 with Java 21: team
creation/join/kick, homes and warps, rank-up commands, and ender-chest close/save
and reopen using the configured storage backend. Build and unit tests do not
verify a running Minecraft server or a live MySQL database.
