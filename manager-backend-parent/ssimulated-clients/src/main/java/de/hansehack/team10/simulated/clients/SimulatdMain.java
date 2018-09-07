package de.hansehack.team10.simulated.clients;

import java.time.Duration;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Timer;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.stream.IntStream;

import org.eclipse.paho.client.mqttv3.MqttAsyncClient;
import org.eclipse.paho.client.mqttv3.MqttClientPersistence;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import de.hansehack.mqtt.client.Client;
import de.hansehack.team10.connection.api.Connection;
import de.hansehack.team10.connection.api.Energie;
import de.hansehack.team10.connection.api.Message;
import de.hansehack.team10.connection.api.Task;
import de.hansehack.team10.connection.api.Topic;

public class SimulatdMain {
	private final static String BROKER= "tcp://10.250.252.184:1883";
	private final static String DEVICE="device";
	private final static MqttClientPersistence store = new MemoryPersistence();
	private static final List<SimulatedClient> clients = new ArrayList<>();
	private static final List<String> devices= new ArrayList<>();
	private static Map<Message, Consumer<Message>> tasks = new LinkedHashMap<>();
	private static List<Timer> timers = new ArrayList<>();
	public static void main(final String[] args) {
		SimulatedProducer producer;
		try {
			final Connection mqttConnection = new Client(SimulatdMain.BROKER, MqttAsyncClient.generateClientId(), SimulatdMain.store);
			final String deviceName = SimulatdMain.DEVICE+SimulatdMain.devices.size()+1;
			SimulatdMain.devices.add(deviceName);
			final SimulatedClient firstClient = new SimulatedClient(mqttConnection, SimulatdMain.generateStartupTasks(deviceName), SimulatdMain.generateEnergie(deviceName), deviceName);
			SimulatdMain.clients.add(firstClient);
			final String deviceNameSecond = SimulatdMain.DEVICE+SimulatdMain.devices.size()+1;
			SimulatdMain.devices.add(deviceNameSecond);
			final SimulatedClient secondClient = new SimulatedClient(mqttConnection, SimulatdMain.generateStartupTasks(deviceNameSecond), SimulatdMain.generateEnergie(deviceNameSecond), deviceNameSecond);
			SimulatdMain.clients.add(secondClient);
			producer = new SimulatedProducer(mqttConnection, "device03");
			
		} catch (final MqttException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		}
		final ScheduledExecutorService executorService = Executors.newScheduledThreadPool(2);
		SimulatdMain.clients.forEach(client->{
			
			final long nextLong = ThreadLocalRandom.current().nextLong(1, 20);
			final long nextLong2 = ThreadLocalRandom.current().nextLong(1, 20);
			executorService.scheduleAtFixedRate(client, nextLong, nextLong2, TimeUnit.SECONDS);
		});
		final long nextLong = ThreadLocalRandom.current().nextLong(1, 20);
		final long nextLong2 = ThreadLocalRandom.current().nextLong(1, 20);
		executorService.scheduleWithFixedDelay(producer, nextLong, nextLong2, TimeUnit.SECONDS);
		final Scanner scanner = new Scanner(System.in);
		String key = "";
		System.out.println("Press any key to exit");
		while (key.isEmpty()) {
			key = scanner.next();
			
		}
		scanner.close();
	}
	
	private static List<Message> generateStartupTasks(final String deviceName){
		final List<Message> messages = new ArrayList<>();
		
		IntStream.range(0, 3).forEach(i->{
			final int hours = ThreadLocalRandom.current().nextInt(1, 25);
			final int energieAmount = ThreadLocalRandom.current().nextInt(1, Integer.MAX_VALUE);
			final Task task = new Task(Duration.ofHours(hours), energieAmount);
			final Message message = new Message(deviceName, task,Topic.TaskNew);
			SimulatdMain.tasks.put(message, (canConsume)->{
				System.out.println("Maschine "+deviceName+" darf folgenden task zuende führen: "+task);
			});
			messages.add(message);
		});
		return messages;
	}
	
	private static List<Message> generateEnergie(final String deviceName) {
		final List<Message> messages = new ArrayList<>();
		IntStream.range(0, 3).forEach(i->{
			final int energieAmount = ThreadLocalRandom.current().nextInt(1, Integer.MAX_VALUE);
			final Duration energiePerHour = Duration.ofHours(ThreadLocalRandom.current().nextLong(3600,24*3600+1));
			final Energie energie = new Energie(energieAmount, energiePerHour);
			final Message message = new Message(deviceName, energie, Topic.Energie);
			messages.add(message);
		});
		return messages;
	}

}
