package commoble.ylssln;

import java.util.Optional;
import java.util.regex.Pattern;

import org.apache.logging.log4j.Level;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public record LogRule(Level logLevel, Optional<Pattern> regexFilter) implements Comparable<LogRule>
{
	public static final Codec<LogRule> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			Codec.STRING.comapFlatMap(Helpers::parseLogLevel, Level::toString).fieldOf("logLevel").forGetter(LogRule::logLevel),
			Codec.STRING.optionalFieldOf("regexFilter").xmap(o->o.map(Pattern::compile), o->o.map(Pattern::toString)).forGetter(LogRule::regexFilter)
		).apply(instance, LogRule::new));
	
	public static LogRule of()
	{
		return new LogRule(Level.INFO, Optional.empty());
	}

	@Override
	public int compareTo(LogRule that)
	{
		// should return a negative number if this comes before that
		// loglevel is most important
		int levelComparison = this.logLevel().compareTo(that.logLevel());
		if (levelComparison != 0)
			return levelComparison;
		
		// shorter rules should be faster to match
		return this.regexFilter().map(p -> p.toString().length()).orElse(0) - that.regexFilter().map(p -> p.toString().length()).orElse(0);
	}
}
