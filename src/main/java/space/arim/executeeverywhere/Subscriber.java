package space.arim.executeeverywhere;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPubSub;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class Subscriber extends JedisPubSub {

	private final boolean isProxy;
	private final CommandRunner commandRunner;

	static final String CHANNEL = "solarexecuteeverywhere";

	private static final byte REVISION = 1;

	public Subscriber(boolean isProxy, CommandRunner commandRunner) {
		this.isProxy = isProxy;
		this.commandRunner = commandRunner;
	}

	@Override
	public void onMessage(String channel, String message) {
		acceptMessageBytes(message.getBytes(StandardCharsets.UTF_8));
	}

	void acceptMessageBytes(byte[] messageBytes) {
		String command;
		try (ByteArrayInputStream inputStream = new ByteArrayInputStream(messageBytes);
			 DataInputStream dataInputStream = new DataInputStream(inputStream)) {
			byte revision = dataInputStream.readByte();
			if (revision != REVISION) {
				// Skip newer protocol version
				return;
			}
			boolean toProxy = dataInputStream.readBoolean();
			if (toProxy != isProxy) {
				// Wrong direction
				return;
			}
			command = dataInputStream.readUTF();
		} catch (IOException ex) {
			throw new RuntimeException(ex);
		}
		commandRunner.dispatchCommand(command);
	}

	static byte[] getMessageBytes(boolean toProxy, String command) {
		try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			 DataOutputStream dataOutputStream = new DataOutputStream(outputStream)) {
			dataOutputStream.writeByte(REVISION);
			dataOutputStream.writeBoolean(toProxy);
			dataOutputStream.writeUTF(command);

			return outputStream.toByteArray();
		} catch (IOException ex) {
			throw new RuntimeException(ex);
		}
	}

	static void writeMessage(JedisPool pool, boolean toProxy, String command) {
		try (Jedis jedis = pool.getResource()) {
			jedis.publish(CHANNEL, new String(getMessageBytes(toProxy, command), StandardCharsets.UTF_8));
		}
	}

}
