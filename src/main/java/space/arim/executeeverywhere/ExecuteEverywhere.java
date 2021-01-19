package space.arim.executeeverywhere;

import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.nio.file.Path;

public class ExecuteEverywhere implements AutoCloseable {

	private final Config config;
	private final Subscriber subscriber;
	private final JedisPool jedisPool;
	private final Thread subscribeThread;

	public ExecuteEverywhere(Config config, Subscriber subscriber, JedisPool jedisPool, Thread subscribeThread) {
		this.config = config;
		this.subscriber = subscriber;
		this.jedisPool = jedisPool;
		this.subscribeThread = subscribeThread;
	}

	public static ExecuteEverywhere setup(Path folder, boolean isProxy, CommandRunner commandRunner) {
		Config config = Config.load(folder, "config.yml");
		Config.ConnectionSettings connectionSettings = config.connectionSettings();
		JedisPool jedisPool = new JedisPool(new JedisPoolConfig(),
				connectionSettings.host(), connectionSettings.port(), 0, connectionSettings.password());
		Subscriber subscriber = new Subscriber(isProxy, commandRunner);

		return new ExecuteEverywhere(config, subscriber, jedisPool,
				new Thread(new SubscriptionRunnable(jedisPool, subscriber), "executeeverywhere-subscriber"));
	}

	public Config config() {
		return config;
	}

	public void start() {
		subscribeThread.start();
	}

	public void dispatchBackendBoundCommand(String command) {
		dispatchBoundCommand(false, command);
	}

	public void dispatchProxyBoundCommand(String command) {
		dispatchBoundCommand(true, command);
	}

	private void dispatchBoundCommand(boolean toProxy, String command) {
		Subscriber.writeMessage(jedisPool, toProxy, command);
	}

	@Override
	public void close() {
		subscriber.unsubscribe();
		jedisPool.destroy();
	}
}
