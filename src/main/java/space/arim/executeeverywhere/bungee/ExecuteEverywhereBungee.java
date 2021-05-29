package space.arim.executeeverywhere.bungee;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.Plugin;
import space.arim.executeeverywhere.ExecuteEverywhere;

public class ExecuteEverywhereBungee extends Plugin {

	private ExecuteEverywhere instance;

	@Override
	public void onEnable() {
		instance = ExecuteEverywhere.setupAndStart(getDataFolder().toPath(), true, (command) -> {
			getProxy().getPluginManager().dispatchCommand(getProxy().getConsole(), command);
		});
		getProxy().getPluginManager().registerCommand(this, new Command("eebungee") {
			@Override
			public void execute(CommandSender sender, String[] args) {
				if (sender.hasPermission("executeeverywhere.use")) {
					getProxy().getScheduler().runAsync(ExecuteEverywhereBungee.this, () -> {
						instance.dispatchBackendBoundCommand(String.join(" ", args));
					});
					sendMessage(sender, instance.config().successful());
				} else {
					sendMessage(sender, instance.config().noPermission());
				}
			}
		});
	}

	private void sendMessage(CommandSender sender, String message) {
		sender.sendMessage(TextComponent.fromLegacyText(message));
	}

	@Override
	public void onDisable() {
		instance.close();
	}
}
