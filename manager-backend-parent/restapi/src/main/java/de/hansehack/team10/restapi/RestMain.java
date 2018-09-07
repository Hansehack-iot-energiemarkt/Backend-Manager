package de.hansehack.team10.restapi;

import java.util.Scanner;

import org.eclipse.paho.client.mqttv3.MqttAsyncClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import de.hansehack.mqtt.client.Client;

public class RestMain {

	public static void main(final String[] args) {
		try {
			final Api api = new Api(new Client("tcp://10.250.252.184:1883", MqttAsyncClient.generateClientId(),
					new MemoryPersistence()));
			final Scanner scanner = new Scanner(System.in);
			String key = "";
			System.out.println("Press any key to exit");
			while (key.isEmpty()) {
				key = scanner.next();

			}
			scanner.close();

			api.stop();
		} catch (final MqttException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
