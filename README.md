
# ExecuteEverywhereReloaded

Recoded version of ExecuteEverywhere

## Config

The configuration is self-explanatory. Restart the server after changing the configuration to have it take effect.
The configuration is platform-independent so you can copy it once across each server and proxy in your network.

## Commands

To use any of the commands, you must have the `executeeverywhere.use` permission

### Paper

/eebukkit <command> - sends the specified to all proxy servers

You must use Paper. Spigot will not work unless you add slf4j to it.

### Velocity

/eevelocity <command> - sends the specified command to all backend servers

### Sponge

/eesponge <command> - sends the specified to all proxy servers

### Waterfall

/eebungee <command> - sends the specified command to all backend servers

You must use Waterfall. BungeeCord will not work unless you add slf4j to it.

## Explanation about slf4j

slf4j is the de-facto logging API for Java applications. As a result, many libraries depend on it.

Paper, Velocity, Waterfall, and Sponge all support slf4j. ExecuteEverywhereReloaded requires slf4j because of Jedis.

It would be unfair for ExecuteEverywhereReloaded to shade slf4j. That would lead to more bloat for Paper, Velocity, Waterfall, and Sponge users who have done nothing wrong. It is Spigot and BungeeCord which are at fault for not providing such a widely-used standard.

## License

GNU AGPL v3.
