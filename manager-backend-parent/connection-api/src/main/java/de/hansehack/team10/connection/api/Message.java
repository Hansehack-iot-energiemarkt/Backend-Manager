package de.hansehack.team10.connection.api;

public  class Message {

	private final String deviceName;

	private final MessagePayload message;

	private final Topic topic;

	/**
	 * @param deviceName
	 * @param message
	 * @param topic
	 */
	public Message(final String deviceName, final MessagePayload message, final Topic topic) {
		super();
		this.deviceName = deviceName;
		this.message = message;
		this.topic = topic;
	}

	/**
	 * @return the deviceName
	 */
	public String getDeviceName() {
		return this.deviceName;
	}

	/**
	 * @return the message
	 */
	public MessagePayload getMessage() {
		return this.message;
	}

	/**
	 * @return the topic
	 */
	public Topic getTopic() {
		return this.topic;
	}

}
