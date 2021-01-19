package space.arim.executeeverywhere;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

public class SubscriptionRunnable implements Runnable {

	private final JedisPool pool;
	private final Subscriber subscriber;

	public SubscriptionRunnable(JedisPool pool, Subscriber subscriber) {
		this.pool = pool;
		this.subscriber = subscriber;
	}

	@Override
	public void run() {
		try (Jedis jedis = pool.getResource()) {
			jedis.subscribe(subscriber, Subscriber.CHANNEL);
		}
	}
}
