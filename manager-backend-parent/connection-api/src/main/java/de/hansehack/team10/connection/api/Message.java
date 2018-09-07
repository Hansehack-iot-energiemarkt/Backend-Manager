package de.hansehack.team10.connection.api;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonCreator.Mode;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Message {
	
	private final LocalDateTime timestamp;

	private final String deviceName;

	private final MessagePayload message;

	private final Topic topic;

	/**
	 * @param deviceName
	 * @param message
	 * @param topic
	 */
	@JsonCreator(mode=Mode.PROPERTIES)
	public Message(@JsonProperty(value="deviceName") final String deviceName, @JsonProperty(value="message") final MessagePayload message,
			@JsonProperty(value="topic") final Topic topic,@JsonProperty(value="timestamp") final LocalDateTime timestamp) {
		super();
		this.deviceName = deviceName;
		this.message = message;
		this.topic = topic;
		this.timestamp=timestamp;
	}
	
	

	/**
	 * @return the timestamp
	 */
	public LocalDateTime getTimestamp() {
		return this.timestamp;
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
