package commoble.ylssln;

import java.util.List;

import com.google.common.base.Functions;
import com.mojang.serialization.Codec;

import commoble.databuddy.config.ConfigHelper;
import commoble.databuddy.config.ConfigHelper.ConfigObject;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.BooleanValue;
import net.minecraftforge.common.ForgeConfigSpec.IntValue;

public record CommonConfig(
	BooleanValue logBlockPlace,
	BooleanValue logBlockBreak,
	BooleanValue logBlockInteract,
	IntValue minPermLevel,
	IntValue maxPermLevel,
	ConfigObject<List<LogRule>> rules)
{
	public static final Codec<List<LogRule>> RULES_CODEC =
		LogRule.CODEC.listOf().xmap(list -> list.stream().sorted().toList(), Functions.identity());
	
	public static final List<LogRule> DEFAULT_RULES = List.of(LogRule.of());
	
	public static CommonConfig create(ForgeConfigSpec.Builder builder, ConfigHelper helper)
	{
		BooleanValue logBlockPlace = builder.comment("Whether to enable logging of block placing.",
			"Log format is [ylssln]event=BLOCK_PLACE;player=playername;pos=BlockPos{x=Y, y=Y, z=Z};block=Block{namespace:id}")
			.define("logBlockPlace", true);
		BooleanValue logBlockBreak = builder.comment("Whether to enable logging of block breaking.",
			"Log format is [ylssln]event=BLOCK_BREAK;player=playername;pos=BlockPos{x=Y, y=Y, z=Z};block=Block{namespace:id}")
			.define("logBlockBreak", true);
		BooleanValue logBlockInteract = builder.comment("Whether to enable logging of block interactions.",
			"Log format is [ylssln]event=BLOCK_INTERACT;player=playername;pos=BlockPos{x=Y, y=Y, z=Z};block=Block{namespace:id}")
			.define("logBlockInteract", false);
		IntValue minPermLevel = builder.comment("Minimum permission level of players to log events for. Defaults to 0 (non-ops)")
			.defineInRange("minPermLevel", 0, 0, 4);
		IntValue maxPermLevel = builder.comment("Maximum permission level of players to log events for. Defaults to 1 (vips)")
			.defineInRange("maxPermLevel", 1, 0, 4);
		
		builder.comment("Logging rules entries. Multiple [[rules]] entries can exist. If none exist or match, then nothing will be logged.",
			"Example of a rules entry:",
			"[[rules]]",
			"\tlogLevel = \"WARN\" # Logging level to log at. Higher-priority logging level rules are checked first. Your server may be configured not to log low-priority logs. Valid log levels are FATAL, ERROR, WARN, INFO, DEBUG, and TRACE.",
			"\tregexFilter = \"block=Block\\\\{minecraft:tnt\\\\}\" # Optional regex filter for this rule, will be checked against the message to be logged. Remember to escape regex backslashes!");
			
		var rules = helper.defineObject("rules", RULES_CODEC, DEFAULT_RULES);
		
		return new CommonConfig(logBlockPlace, logBlockBreak, logBlockInteract, minPermLevel, maxPermLevel, rules);
	}
}
