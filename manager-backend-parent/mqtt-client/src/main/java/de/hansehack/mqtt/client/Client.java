package de.hansehack.mqtt.client;

import java.util.function.Consumer;

import org.eclipse.paho.client.mqttv3.MqttAsyncClient;
import org.eclipse.paho.client.mqttv3.MqttClientPersistence;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttPersistenceException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;

import de.hansehack.team10.connection.api.Connection;
import de.hansehack.team10.connection.api.Message;

public class Client extends MqttAsyncClient implements Connection {

	public static final int QOS = 2; // quality of service

	private final ObjectMapper mapper;

	public Client(final String broker, final String clientId, final MqttClientPersistence persistence)
			throws MqttException {
		super(broker, clientId, persistence);

		final MqttConnectOptions connectOptions = new MqttConnectOptions();
		connectOptions.setCleanSession(true); // do not remember the state of the client
		this.connect(connectOptions);
		this.mapper = new ObjectMapper();
		this.mapper.registerModule(new Jdk8Module());
	}

	public void sendMessage(final String topic, final String content) {
		try {
			final MqttMessage msg = new MqttMessage(content.getBytes());
			msg.setQos(Client.QOS);
			this.publish(topic, msg);
		} catch (final MqttException e) {
			Client.printException(e);
		}
	}

	public static void printException(final MqttException e) {
		System.out.println("reason: " + e.getReasonCode());
		System.out.println("   msg: " + e.getMessage());
		System.out.println("   loc: " + e.getLocalizedMessage());
		System.out.println(" cause: " + e.getCause());
		System.out.println(" excep: " + e);
		e.printStackTrace();
	}

	@Override
	public void messageReceivedSubscriber(final String topic, final Consumer<Message> messageConsumer) {
		try {
			this.subscribe(topic, Client.QOS, (final String recvTopic, final MqttMessage recvMessage) -> {
				final Message receivedMessage = this.mapper.readValue(recvMessage.getPayload(), Message.class);
				messageConsumer.accept(receivedMessage);
			});
		} catch (final MqttException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public void messageSend(final Message sendMessage) {
		try {
			final MqttMessage mqMessage = new MqttMessage(this.mapper.writeValueAsBytes(sendMessage));
			mqMessage.setQos(Client.QOS);
			this.publish(sendMessage.getTopic().getTopic(), mqMessage);
		} catch (final JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (final MqttPersistenceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (final MqttException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}

	
}
