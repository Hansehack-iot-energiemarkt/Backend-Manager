import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class Client {
	public static void main(String[] args) {
		
		String broker = "tcp://";
		String clientId = "test01";
		
		String topic = "Verbraucher";
		String content = "Hello World!";
		int qos = 2; // quality of service
		
		try {
			MqttClient client = new MqttClient(broker, clientId);
			MqttConnectOptions cOpts = new MqttConnectOptions();
			cOpts.setCleanSession(true); // do not remember state of client
			client.connect();
			
			MqttMessage msg = new MqttMessage(content.getBytes());
			msg.setQos(qos);
			client.publish(topic, msg);
			
			client.disconnect();
		} catch (MqttException e) {
			System.out.println("reason "+e.getReasonCode());
            System.out.println("msg "+e.getMessage());
            System.out.println("loc "+e.getLocalizedMessage());
            System.out.println("cause "+e.getCause());
            System.out.println("excep "+e);
			e.printStackTrace();
		}
	}
}
