package commoble.ylssln;

import java.util.List;

import commoble.databuddy.config.ConfigHelper;
import commoble.databuddy.config.ConfigHelper.ConfigObject;
import net.minecraftforge.common.ForgeConfigSpec;

public record CommonConfig(
	ConfigObject<EventLogger> blockPlaceConfig,
	ConfigObject<EventLogger> blockBreakConfig,
	ConfigObject<EventLogger> blockInteractConfig,
	ConfigObject<EventLogger> itemInteractConfig,
	ConfigObject<EventLogger> entityInteractConfig,
	ConfigObject<EventLogger> entityKillConfig
	)
{	
	public static final List<LogRule> DEFAULT_RULES = List.of(LogRule.defaultRule());
	
	public static CommonConfig create(ForgeConfigSpec.Builder builder)
	{
		return new CommonConfig(
			ConfigHelper.defineObject(builder.comment(
				"Each logging event can be configured with the following fields:",
				"minPermLevel : int in range [0,3] => Minimum permission level of players to log events for. Defaults to 0 (non-ops).",
				"maxPermLevel : int in range [0,3] => Maximum permission level of players to log events for. Defaults to 1 (VIPs).",
				"  (For min and max permission level, 0 = non-ops, 1 = VIPs, 2 = creative players, 3 = moderators, 4 = server admins)",
				"rules : list of logging rules entries. Multiple [[rules]] entries can exist.",
				"  Rules are checked in the order listed and the event will be logged at the logging level of the first rule matched.",
				"  If none exist or none match, then nothing will be logged.",
				"  Example of rules entries:",
				"  [[blockPlace.rules]]",
				"    \tlogLevel = \"WARN\" # Logging level to log at. Be aware that your server may have been configured not to log low-priority log levels (most servers will log at least INFO and WARN). Valid log levels are ERROR, WARN, INFO, DEBUG, and TRACE.",
				"    \tregexFilter = \"block=Block\\\\{minecraft:tnt\\\\}\" # Optional regex filter for this rule, will be checked against the message to be logged. Remember to escape regex backslashes!",
				"  [[blockPlace.rules]]",
				"    \tlogLevel = \"WARN\"",
				"    \tregexFilter = \"block=Block\\\\{minecraft:fire\\\\}\"",
				" ",
				" ",
				"Configuration of logging of block placing.",
				"Log format is [ylssln]event=BLOCK_PLACE;player=playername;pos={x=Y,y=Y,z=Z};block={namespace:id}"),
				"blockPlace", EventLogger.CODEC, new EventLogger(0, 1, List.of(LogRule.defaultRule()))),
			ConfigHelper.defineObject(builder.comment("Configuration of logging of block breaking.",
				"Log format is [ylssln]event=BLOCK_BREAK;player=playername;pos={x=Y, y=Y,z=Z};block={namespace:id}"),
				"blockBreak", EventLogger.CODEC, new EventLogger(0, 1, List.of(LogRule.defaultRule()))),
			ConfigHelper.defineObject(builder.comment("Configuration of logging of block interacting.",
				"Log format is [ylssln]event=BLOCK_INTERACT;player=playername;pos={x=Y,y=Y,z=Z};block={namespace:id};hand=MAIN_HAND|OFF_HAND;item={namespace:id}"),
				"blockInteract", EventLogger.CODEC, new EventLogger(0, 1, List.of(LogRule.defaultRule()))),
			ConfigHelper.defineObject(builder.comment("Configuration of logging of using items without targeting a block.",
				"Log format is [ylssln]event=ITEM_INTERACT;player=playername;pos={x=Y,y=Y,z=Z};item={namespace:id};hand=MAIN_HAND|OFF_HAND"),
				"itemInteract", EventLogger.CODEC, new EventLogger(0, 1, List.of(LogRule.defaultRule()))),
			ConfigHelper.defineObject(builder.comment("Configuration of logging of entity interacting.",
				"Log format is [ylssln]event=ENTITY_INTERACT;player=playername;pos={x=Y,y=Y,z=Z};entity={namespace:id};hand=MAIN_HAND|OFF_HAND,item={namespace:id}"),
				"entityInteract", EventLogger.CODEC, new EventLogger(0, 1, List.of(LogRule.defaultRule()))),
			ConfigHelper.defineObject(builder.comment("Configuration of logging of entities killed by players.",
				"Log format is [ylssln]event=ENTITY_KILL;player=playername;pos={x=Y,y=Y,z=Z};entity={namespace:id};damageSource=sourcename"),
				"entityKill", EventLogger.CODEC, new EventLogger(0, 1, List.of(LogRule.defaultRule()))));
	}
}
