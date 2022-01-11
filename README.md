This is a server utility mod for logging player interactions. This mod is currently available for the Forge modloader.

Built jars are available here:
* https://www.curseforge.com/minecraft/mc-mods/your-log-saw-something-last-night

When installed, the following will be logged:

* Blocks broken by players
* Blocks placed by players
* Blocks activated by players (disabled by default)

Interactions by forge's "Fake Players" are not logged.

This mod is configurable to filter logging via regex pattern; the default config file will be generated in yourminecraftinstance/config/ylssln-common.toml by forge the first time minecraft is launched with the mod installed.

The default config file (with comments included) is as follows:

```toml
#Whether to enable logging of block placing.
#Log format is event=BLOCK_PLACE;player=playername;pos=BlockPos{x=Y, y=Y, z=Z};block=Block{namespace:id}
logBlockPlace = true
#Whether to enable logging of block breaking.
#Log format is event=BLOCK_BREAK;player=playername;pos=BlockPos{x=Y, y=Y, z=Z};block=Block{namespace:id}
logBlockBreak = true
#Whether to enable logging of block interactions.
#Log format is event=BLOCK_INTERACT;player=playername;pos=BlockPos{x=Y, y=Y, z=Z};block=Block{namespace:id}
logBlockInteract = true
#Minimum permission level of players to log events for. Defaults to 0 (non-ops)
#Range: 0 ~ 4
minPermLevel = 0
#Maximum permission level of players to log events for. Defaults to 1 (vips)
#Range: 0 ~ 4
maxPermLevel = 1

#Logging rules entries. Multiple [[rules]] entries can exist. If none exist or match, then nothing will be logged.
#Example of a rules entry:
#[[rules]]
#	logLevel = "WARN" # Logging level to log at. Higher-priority logging level rules are checked first. Your server may be configured not to log low-priority logs. Valid log levels are FATAL, ERROR, WARN, INFO, DEBUG, and TRACE.
#	regexFilter = "block=Block\\{minecraft:tnt\\}" # Optional regex filter for this rule, will be checked against the message to be logged. Remember to escape regex backslashes!
[[rules]]
	logLevel = "INFO"

```
