package de.hansehack.mqtt.client;

import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class Main {
	public static void main(final String[] args) {

		final String broker = "tcp://http://test.mosquitto.org:1883";
		final String clientId = "hansehack_verbraucher_01";

		final String send_topic = "hansehack/verbraucher";

		try {
			final Client client = new Client(broker, clientId);

			client.subscribe("hansehack/+", Client.QOS, (final String recv_topic, final MqttMessage recv_message) -> {
				// called when the client receives a message from the server
				client.sendMessage(send_topic, "Hello World");
			});

			client.disconnect();
		} catch (final MqttException e) {
			Client.printException(e);
		}
	}
}
