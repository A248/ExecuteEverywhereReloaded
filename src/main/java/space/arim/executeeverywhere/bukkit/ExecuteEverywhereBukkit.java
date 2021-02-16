package space.arim.executeeverywhere.bukkit;

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
				sender.sendMessage(instance.config().successful());
			} else {
				sender.sendMessage(instance.config().noPermission());
			}
			return true;
		});
	}

	@Override
	public void onDisable() {
		instance.close();
	}
}
