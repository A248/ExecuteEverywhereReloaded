package space.arim.executeeverywhere.sponge;

import org.slf4j.Logger;
import org.spongepowered.api.Game;
import org.spongepowered.api.command.CommandCallable;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.config.ConfigDir;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameInitializationEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;
import space.arim.executeeverywhere.ExecuteEverywhere;

import javax.annotation.Nullable;
import javax.inject.Inject;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Plugin(id = "executeeverywhere", name = "ExecuteEverywhere", version = "0.1.0", authors = "A248")
public class ExecuteEverywhereSponge {

	private final Game server;
	private final Logger logger;
	private final Path dataFolder;

	private ExecuteEverywhere instance;

	@Inject
	public ExecuteEverywhereSponge(Game server, Logger logger, @ConfigDir(sharedRoot = false) Path dataFolder) {
		this.server = server;
		this.logger = logger;
		this.dataFolder = dataFolder;
	}

	@Listener
	public void onInitialize(GameInitializationEvent event) {
		instance = ExecuteEverywhere.setupAndStart(dataFolder, false, (command) -> {
			server.getScheduler().createSyncExecutor(ExecuteEverywhereSponge.this).execute(() -> {
				server.getCommandManager().process(server.getServer().getConsole(), command);
			});
		});
		server.getCommandManager().register(this, new CommandCallable() {
			@Override
			public CommandResult process(CommandSource sender, String arguments) throws CommandException {
				if (sender.hasPermission("executeeverywhere.use")) {
					server.getScheduler().createAsyncExecutor(ExecuteEverywhereSponge.this).execute(() -> {
						instance.dispatchProxyBoundCommand(arguments);
					});
					sender.sendMessage(Text.of(instance.config().successful()));
				} else {
					sender.sendMessage(Text.of(instance.config().noPermission()));
				}
				return CommandResult.success();
			}

			@Override
			public List<String> getSuggestions(CommandSource source, String arguments, @Nullable Location<World> targetPosition) throws CommandException {
				return Arrays.asList();
			}

			@Override
			public boolean testPermission(CommandSource source) {
				return true;
			}

			@Override
			public Optional<Text> getShortDescription(CommandSource source) {
				return Optional.empty();
			}

			@Override
			public Optional<Text> getHelp(CommandSource source) {
				return Optional.empty();
			}

			@Override
			public Text getUsage(CommandSource source) {
				return Text.of();
			}
		}, "eesponge");
	}
}
