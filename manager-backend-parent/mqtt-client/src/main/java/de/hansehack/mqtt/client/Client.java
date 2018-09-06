package de.hansehack.mqtt.client;

import org.eclipse.paho.client.mqttv3.MqttAsyncClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class Client extends MqttAsyncClient {
	
	public static final int QOS = 2; // quality of service
	
	public Client(String broker, String clientId) throws MqttException {
		super(broker, clientId);
		
		MqttConnectOptions connectOptions = new MqttConnectOptions();
		connectOptions.setCleanSession(true); // do not remember the state of the client
		this.connect(connectOptions);
	}
	
	public void sendMessage(String topic, String content) {
		try {
			MqttMessage msg = new MqttMessage(content.getBytes());
			msg.setQos(QOS);
			this.publish(topic, msg);
		} catch (MqttException e) {
			printException(e);
		}
	}
	
	public static void printException(MqttException e) {
		System.out.println("reason: " + e.getReasonCode());
        System.out.println("   msg: " + e.getMessage());
        System.out.println("   loc: " + e.getLocalizedMessage());
        System.out.println(" cause: " + e.getCause());
        System.out.println(" excep: " + e);
		e.printStackTrace();
	}
	
	public static void main(String[] args) {
		
		String broker = "tcp://http://test.mosquitto.org:1883";
		String clientId = "hansehack_verbraucher_01";
		
		String send_topic = "hansehack/verbraucher";
		
		try {
			final Client client = new Client(broker, clientId);
			
			client.subscribe("hansehack/+", QOS, (String recv_topic, MqttMessage recv_message) -> {
				// called when the client receives a message from the server
				client.sendMessage(send_topic, "Hello World");
			});
			
			client.disconnect();
		} catch (MqttException e) {
			printException(e);
		}
	}
}
