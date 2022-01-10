package commoble.ylssln;

import java.util.Optional;

import org.apache.logging.log4j.Level;

import com.mojang.serialization.DataResult;

public class Helpers
{
	public static DataResult<Level> parseLogLevel(String name)
	{
		return Optional.ofNullable(Level.getLevel(name))
			.map(DataResult::success)
			.orElseGet(()->DataResult.error("Invalid log level name: " + name));
	}
}
