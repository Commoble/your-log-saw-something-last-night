package commoble.ylssln;

import java.util.Locale;
import java.util.Optional;
import java.util.regex.Pattern;

import org.slf4j.event.Level;
import org.slf4j.Logger;

import com.mojang.logging.LogUtils;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public record LogRule(Level logLevel, Optional<Pattern> regexFilter)
{
	private static final Logger LOGGER = LogUtils.getLogger();
	
	public static final Codec<LogRule> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			Codec.STRING.comapFlatMap(LogRule::parseLogLevel, Level::toString).fieldOf("logLevel").forGetter(LogRule::logLevel),
			Codec.STRING.optionalFieldOf("regexFilter").xmap(o->o.map(Pattern::compile), o->o.map(Pattern::toString)).forGetter(LogRule::regexFilter)
		).apply(instance, LogRule::new));
	
	public static LogRule defaultRule()
	{
		return new LogRule(Level.INFO, Optional.empty());
	}
	
	public static DataResult<Level> parseLogLevel(String name)
	{
		return Optional.ofNullable(Level.valueOf(name.toUpperCase(Locale.ENGLISH)))
			.map(DataResult::success)
			.orElseGet(()->DataResult.error("Invalid log level name: " + name));
	}
	
	public boolean tryLog(String s)
	{
		boolean logged = this.regexFilter.map(p -> p.asMatchPredicate().test(s)).orElse(true);
		if (logged)
		{
			switch(this.logLevel)
			{
				case ERROR -> LOGGER.error(s);
				case WARN -> LOGGER.warn(s);
				case INFO -> LOGGER.info(s);
				case DEBUG -> LOGGER.debug(s);
				case TRACE -> LOGGER.trace(s);
				default -> {}
			}
		}
		return logged;
	}
}