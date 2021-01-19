package space.arim.executeeverywhere;

import space.arim.dazzleconf.annote.ConfDefault;
import space.arim.dazzleconf.annote.ConfHeader;
import space.arim.dazzleconf.annote.ConfKey;
import space.arim.dazzleconf.annote.SubSection;

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

}
