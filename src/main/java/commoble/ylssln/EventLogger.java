package commoble.ylssln;

import java.util.List;
import java.util.function.Supplier;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.server.level.ServerPlayer;

public record EventLogger(int minPermLevel, int maxPermLevel, List<LogRule> rules)
{
	public static final Codec<EventLogger> CODEC = RecordCodecBuilder.create(builder -> builder.group(
			Codec.INT.fieldOf("minPermLevel").forGetter(EventLogger::minPermLevel),
			Codec.INT.fieldOf("maxPermLevel").forGetter(EventLogger::maxPermLevel),
			LogRule.CODEC.listOf().fieldOf("rules").forGetter(EventLogger::rules)
		).apply(builder, EventLogger::new));
	
	public void log(ServerPlayer serverPlayer, Supplier<String> logFactory)
	{
		if (this.rules.isEmpty())
			return;

		int permLevel = serverPlayer.server.getProfilePermissions(serverPlayer.getGameProfile());
		
		if (permLevel >= this.minPermLevel && permLevel <= maxPermLevel)
		{
			String log = logFactory.get();
			for (LogRule rule : rules)
			{
				if (rule.tryLog(log))
				{
					return;
				}
			}
		}
	}
}