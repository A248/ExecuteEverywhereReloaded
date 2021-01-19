package space.arim.executeeverywhere.velocity;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.RawCommand;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.proxy.ProxyShutdownEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.slf4j.Logger;
import space.arim.executeeverywhere.ExecuteEverywhere;

import javax.inject.Inject;
import java.nio.file.Path;

@Plugin(id = "executeeverywhere", name = "ExecuteEverywhere", version = "0.1.0", authors = "A248")
public class ExecuteEverywhereVelocity {

	private final ProxyServer server;
	private final Logger logger;
	private final Path dataFolder;

	private ExecuteEverywhere instance;

	@Inject
	public ExecuteEverywhereVelocity(ProxyServer server, Logger logger, @DataDirectory Path dataFolder) {
		this.server = server;
		this.logger = logger;
		this.dataFolder = dataFolder;
	}

	@Subscribe
	public void onInitialize(ProxyInitializeEvent event) {
		instance = ExecuteEverywhere.setup(dataFolder, true, (command) -> {
			server.getCommandManager().executeImmediatelyAsync(server.getConsoleCommandSource(), command).exceptionally((ex) -> {
				logger.error("Encountered exception while dispatching command", ex);
				return null;
			});
		});
		server.getCommandManager().register(server.getCommandManager().metaBuilder("eevelocity").build(), new RawCommand() {

			@Override
			public void execute(Invocation invocation) {
				CommandSource sender = invocation.source();
				if (sender.hasPermission("executeeverywhere.use")) {
					server.getScheduler().buildTask(ExecuteEverywhereVelocity.this, () -> {
						instance.dispatchBackendBoundCommand(invocation.arguments());
					}).schedule();
					sender.sendMessage(LegacyComponentSerializer.legacyAmpersand().deserialize(instance.config().successful()));
				} else {
					sender.sendMessage(LegacyComponentSerializer.legacyAmpersand().deserialize(instance.config().noPermission()));
				}
			}
		});
	}

	@Subscribe
	public void onShutdown(ProxyShutdownEvent event) {
		instance.close();
	}
}
