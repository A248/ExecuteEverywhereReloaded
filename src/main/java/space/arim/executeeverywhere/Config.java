package space.arim.executeeverywhere;

import space.arim.dazzleconf.ConfigurationOptions;
import space.arim.dazzleconf.annote.ConfDefault;
import space.arim.dazzleconf.annote.ConfHeader;
import space.arim.dazzleconf.annote.ConfKey;
import space.arim.dazzleconf.annote.SubSection;
import space.arim.dazzleconf.error.InvalidConfigException;
import space.arim.dazzleconf.ext.snakeyaml.SnakeYamlConfigurationFactory;
import space.arim.dazzleconf.helper.ConfigurationHelper;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Path;

public interface Config {

	@ConfHeader("Redis connection settings")
	interface ConnectionSettings {

		@ConfDefault.DefaultString("localhost")
		String host();

		@ConfDefault.DefaultInteger(6379)
		int port();

		@ConfDefault.DefaultString("")
		String password();

	}

	@ConfKey("connection-settings")
	@SubSection
	ConnectionSettings connectionSettings();

	@ConfKey("no-permission")
	@ConfDefault.DefaultString("&cYou cannot use this.")
	String noPermission();

	@ConfDefault.DefaultString("&aSuccessfully dispatched command")
	String successful();

	static Config load(Path folder, String filename) {
		try {
			return new ConfigurationHelper<>(folder, filename, new SnakeYamlConfigurationFactory<>(Config.class, ConfigurationOptions.defaults())).reloadConfigData();
		} catch (IOException ex) {
			throw new UncheckedIOException(ex);
		} catch (InvalidConfigException ex) {
			throw new RuntimeException("Please fix your configuration and restart.", ex);
		}
	}
}
