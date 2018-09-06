package de.hansehack.mqtt.client;

import org.eclipse.paho.client.mqttv3.MqttAsyncClient;
import org.eclipse.paho.client.mqttv3.MqttClientPersistence;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class Client extends MqttAsyncClient {

	public static final int QOS = 2; // quality of service

	public Client(final String broker, final String clientId, final MqttClientPersistence persistence) throws MqttException {
		super(broker, clientId, persistence);

		final MqttConnectOptions connectOptions = new MqttConnectOptions();
		connectOptions.setCleanSession(true); // do not remember the state of the client
		this.connect(connectOptions);
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
}
