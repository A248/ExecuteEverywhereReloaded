package space.arim.executeeverywhere.bukkit;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import space.arim.executeeverywhere.ExecuteEverywhere;

public class ExecuteEverywhereBukkit extends JavaPlugin {

	private ExecuteEverywhere instance;

	@Override
	public void onEnable() {
		instance = ExecuteEverywhere.setupAndStart(getDataFolder().toPath(), false, (command) -> {
			getServer().getScheduler().runTask(this, () -> {
				getServer().dispatchCommand(getServer().getConsoleSender(), command);
			});
		});
		getCommand("eebukkit").setExecutor((sender, command, label, args) -> {
			if (sender.hasPermission("executeeverywhere.use")) {
				getServer().getScheduler().runTaskAsynchronously(this, () -> {
					instance.dispatchProxyBoundCommand(String.join(" ", args));
				});
				sendMessage(sender, instance.config().successful());
			} else {
				sendMessage(sender, instance.config().noPermission());
			}
			return true;
		});
	}

	private void sendMessage(CommandSender sender, String message) {
		sender.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
	}

	@Override
	public void onDisable() {
		instance.close();
	}
}
