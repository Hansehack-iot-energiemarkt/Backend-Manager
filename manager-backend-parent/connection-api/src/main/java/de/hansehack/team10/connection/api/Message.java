package de.hansehack.team10.connection.api;

public class Message {

	private final String deviceName;

	private final MessagePayload message;

	private final String topic;

	/**
	 * @param deviceName
	 * @param message
	 * @param topic
	 */
	public Message(final String deviceName, final MessagePayload message, final String topic) {
		super();
		this.deviceName = deviceName;
		this.message = message;
		this.topic = topic;
	}

	public String getMessage() {
		return this.deviceName + ":" + this.message.getMessage() + ":" + this.topic;
	}

	public String getTopic() {
		return this.topic;
	}

}
