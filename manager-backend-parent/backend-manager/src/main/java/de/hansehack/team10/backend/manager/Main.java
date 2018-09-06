package de.hansehack.team10.backend.manager;

import java.util.Scanner;

import org.eclipse.paho.client.mqttv3.MqttAsyncClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import de.hansehack.mqtt.client.Client;
import de.hansehack.team10.connection.api.Connection;

public class Main {
	
	private static String BROKER = "tcp://10.250.252.184:1883";
	
	
	public static void main(final String[] args) {
		try {
			final Connection mqttConnection = new Client(Main.BROKER, MqttAsyncClient.generateClientId(), new MemoryPersistence());
			final Connection i2cConnection = null;
			final BackendManagerClient backendManagerClient = new BackendManagerClient(mqttConnection, i2cConnection);
			backendManagerClient.start();
			final Scanner scanner = new Scanner(System.in);
			String key = "";
			System.out.println("Press any key to exit");
			while (key.isEmpty()) {
				 key = scanner.next();
				
			}
			scanner.close();
		} catch (final MqttException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
