package de.hansehack.team10.connection.api;

import java.util.function.Consumer;

/**
 * Dieses Interface muss von allen Klassen implementiert werden, die vom
 * Backendmanager angesprochen werden sollen.
 * 
 * @author eiamnacken
 *
 */
public interface Connection {
	/**
	 * Wird augerufen jedesmal wenn eine Nachricht erhalten wurde
	 * 
	 * @param messageConsumer {@link Consumer} der aufgerufen wird wenn der Client
	 *                        eine Nachricht erhalten hat.
	 */
	void messageReceivedSubscriber(Consumer<Message> messageConsumer);

	/**
	 * Wird aufgerufen wenn eine Nachricht verschickt werden soll.
	 */
	Consumer<Message> messageSend();

}
