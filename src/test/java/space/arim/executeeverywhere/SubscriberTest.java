package space.arim.executeeverywhere;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.concurrent.ThreadLocalRandom;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class SubscriberTest {

	@ParameterizedTest
	@ValueSource(strings = {"commandone", "other command", "lots 2 3 more commands"})
	public void writeCommand(String command) {
		CommandRunner runner = mock(CommandRunner.class);
		boolean toProxy = ThreadLocalRandom.current().nextBoolean();

		byte[] messageBytes = Subscriber.getMessageBytes(toProxy, command);
		Subscriber subscriber = new Subscriber(toProxy, runner);
		subscriber.acceptMessageBytes(messageBytes);

		verify(runner).dispatchCommand(command);
	}
}
