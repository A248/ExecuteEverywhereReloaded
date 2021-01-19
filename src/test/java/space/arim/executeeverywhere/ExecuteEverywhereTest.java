package space.arim.executeeverywhere;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class ExecuteEverywhereTest {

	@TempDir
	public Path tempDir;

	@Test
	public void saveConfig() {
		assertDoesNotThrow(() -> ExecuteEverywhere.loadConfig(tempDir, "config.yml"));
	}

	@Test
	public void reloadConfig() {
		ExecuteEverywhere.loadConfig(tempDir, "config.yml");
		assertDoesNotThrow(() -> ExecuteEverywhere.loadConfig(tempDir, "config.yml"));
	}
}
