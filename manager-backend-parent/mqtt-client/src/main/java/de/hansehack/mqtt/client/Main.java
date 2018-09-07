package de.hansehack.mqtt.client;

import java.util.Timer;
import java.util.TimerTask;

import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class Main {
	public static void main(final String[] args) {

		final String broker = "tcp://test.mosquitto.org:1883";

		final String topic_base = "hansehack/";
		final String topic_verbraucher = topic_base + "verbraucher";
		final String topic_erzeuger = topic_base + "erzeuger";
		
		final MemoryPersistence mem = new MemoryPersistence();

		try {
			final Client client_1 = new Client(broker, "hansehack_verbraucher_01", mem);
			final Client client_2 = new Client(broker, "hansehack_erzeuger_01", mem);

			while (!client_1.isConnected() || !client_2.isConnected()) { }
			
			client_1.subscribe(topic_erzeuger, Client.QOS, (final String recv_topic, final MqttMessage recv_message) -> {
				// called when the client receives a message from the server
				System.out.println(recv_topic + " : " + new String(recv_message.getPayload()));
				new Timer().schedule(new TimerTask() {
					@Override
					public void run() {
						client_1.sendMessage(topic_verbraucher, "I'm using power");
					}
				}, 500);
			});
			client_2.subscribe(topic_verbraucher, Client.QOS, (final String recv_topic, final MqttMessage recv_message) -> {
				// called when the client receives a message from the server
				System.out.println(recv_topic + " : " + new String(recv_message.getPayload()));
				client_2.sendMessage(topic_erzeuger, "I have power");
			});
			System.out.println("Connected");
			
			client_1.sendMessage(topic_verbraucher, "OneTwoThree");
			
//			client_1.disconnect();
//			client_2.disconnect();
//			client_3.disconnect();
//			System.out.println("Disconnected");
		} catch (final MqttException e) {
			System.out.println("reason: " + e.getReasonCode());
			System.out.println("   msg: " + e.getMessage());
			System.out.println("   loc: " + e.getLocalizedMessage());
			System.out.println(" cause: " + e.getCause());
			System.out.println(" excep: " + e);
			e.printStackTrace();
		}
	}
}
