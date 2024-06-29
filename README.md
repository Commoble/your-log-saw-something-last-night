This is a server utility mod for logging player interactions. This mod is currently available for the Forge modloader.

Built jars are available here:
* https://www.curseforge.com/minecraft/mc-mods/your-log-saw-something-last-night
* https://modrinth.com/mod/your-log-saw-something-last-night

When installed, the following will be logged:

* Blocks broken by players
* Blocks placed by players
* Blocks activated by players
* Items used by players without targeting a block
* Entities activated by players
* Entities killed by players

Interactions by forge's "Fake Players" are not logged.

This mod is configurable to filter logging via regex pattern; the default config file will be generated in yourminecraftinstance/config/ylssln-common.toml by forge the first time minecraft is launched with the mod installed.

The default config file (with comments included) is as follows:

```toml
#Each logging event can be configured with the following fields:
#minPermLevel : int in range [0,3] => Minimum permission level of players to log events for. Defaults to 0 (non-ops).
#maxPermLevel : int in range [0,3] => Maximum permission level of players to log events for. Defaults to 1 (VIPs).
#  (For min and max permission level, 0 = non-ops, 1 = VIPs, 2 = creative players, 3 = moderators, 4 = server admins)
#rules : list of logging rules entries. Multiple [[rules]] entries can exist.
#  Rules are checked in the order listed and the event will be logged at the logging level of the first rule matched.
#  If none exist or none match, then nothing will be logged.
#  Example of rules entries:
#  [[blockPlace.rules]]
#    	logLevel = "WARN" # Logging level to log at. Be aware that your server may have been configured not to log low-priority log levels (most servers will log at least INFO and WARN). Valid log levels are ERROR, WARN, INFO, DEBUG, and TRACE.
#    	regexFilter = "block=Block\\{minecraft:tnt\\}" # Optional regex filter for this rule, will be checked against the message to be logged. Remember to escape regex backslashes!
#  [[blockPlace.rules]]
#    	logLevel = "WARN"
#    	regexFilter = "block=Block\\{minecraft:fire\\}"
# 
# 
#Configuration of logging of block placing.
#Log format is [ylssln]event=BLOCK_PLACE;player=playername;pos={x=Y,y=Y,z=Z};block={namespace:id}
[blockPlace]
	maxPermLevel = 1
	minPermLevel = 0

	[[blockPlace.rules]]
		logLevel = "INFO"

#Configuration of logging of block breaking.
#Log format is [ylssln]event=BLOCK_BREAK;player=playername;pos={x=Y, y=Y,z=Z};block={namespace:id}
[blockBreak]
	maxPermLevel = 1
	minPermLevel = 0

	[[blockBreak.rules]]
		logLevel = "INFO"

#Configuration of logging of block interacting.
#Log format is [ylssln]event=BLOCK_INTERACT;player=playername;pos={x=Y,y=Y,z=Z};block={namespace:id};hand=MAIN_HAND|OFF_HAND;item={namespace:id}
[blockInteract]
	maxPermLevel = 1
	minPermLevel = 0

	[[blockInteract.rules]]
		logLevel = "INFO"

#Configuration of logging of using items without targeting a block.
#Log format is [ylssln]event=ITEM_INTERACT;player=playername;pos={x=Y,y=Y,z=Z};item={namespace:id};hand=MAIN_HAND|OFF_HAND
[itemInteract]
	maxPermLevel = 1
	minPermLevel = 0

	[[itemInteract.rules]]
		logLevel = "INFO"

#Configuration of logging of entity interacting.
#Log format is [ylssln]event=ENTITY_INTERACT;player=playername;pos={x=Y,y=Y,z=Z};entity={namespace:id};hand=MAIN_HAND|OFF_HAND,item={namespace:id}
[entityInteract]
	maxPermLevel = 1
	minPermLevel = 0

	[[entityInteract.rules]]
		logLevel = "INFO"

#Configuration of logging of entities killed by players.
#Log format is [ylssln]event=ENTITY_KILL;player=playername;pos={x=Y,y=Y,z=Z};entity={namespace:id};damageSource=sourcename
[entityKill]
	maxPermLevel = 1
	minPermLevel = 0

	[[entityKill.rules]]
		logLevel = "INFO"

```
