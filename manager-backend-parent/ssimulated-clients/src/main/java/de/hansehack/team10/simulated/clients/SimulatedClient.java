package de.hansehack.team10.simulated.clients;

import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Queue;
import java.util.Timer;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

import de.hansehack.team10.connection.api.Connection;
import de.hansehack.team10.connection.api.Energie;
import de.hansehack.team10.connection.api.Message;
import de.hansehack.team10.connection.api.MessagePayload;
import de.hansehack.team10.connection.api.Task;
import de.hansehack.team10.connection.api.Topic;

public class SimulatedClient implements Runnable {

	private final Connection mqttClient;
	/**
	 * Lookup für welcher consumer für welchen task ausgeführt werden soll
	 */
	private final Map<Task, Consumer<Message>> subscriberLookup;

	private final AtomicInteger energieLevel = new AtomicInteger(0);
	/**
	 * Fragen energie zu bekommen braucht nicht jeder client
	 */
	private final Optional<List<Message>> energie;
	/**
	 * Zwischenspeicher um energie nachrichten abarbeiten zu können
	 */
	private final Queue<Message> cache;


	private final Timer timer;
	
	private final String name;
	


	/**
	 * @param mqttClient
	 * @param topics
	 */
	public SimulatedClient(final Connection mqttClient, final List<Message> tasksAtStartup,
			final List<Message> energie, final String name) {
		super();
		this.mqttClient = mqttClient;
		this.subscriberLookup = new HashMap<>();
		this.energie = Optional.ofNullable(energie);
		this.cache = new ArrayDeque<>();
		this.timer = new Timer(name);
		this.name = name;

	}

	/**
	 * Zum starten des Clients
	 * @param tasksAtStartup
	 */
	public void startup(final List<Message> tasksAtStartup) {
		tasksAtStartup.stream().forEach(task -> this.mqttClient.messageSend(task));
		this.subscriberLookup.entrySet().forEach(entry->{
			this.mqttClient.messageReceivedSubscriber(Topic.Task.getTopic(), entry.getValue());
		});
		this.createNewMessages();
		

	}

	/**
	 * Neue Nachrichten erzeugen
	 */
	private void createNewMessages() {
		this.energie.ifPresent(messages -> {
			messages.stream().forEach(this.cache::add);
		});
	}

	@Override
	public void run() {
		if(!this.energie.isPresent()) {
			return;
		}
		
		
		if(this.cache.isEmpty()) {
			this.createNewMessages();
		}
		final Message nextEnergie = this.cache.poll();
		final MessagePayload messagePayload = nextEnergie.getMessage();
		if(messagePayload instanceof Energie) {
			final Energie newEnergie = (Energie) messagePayload;
			this.energieLevel.addAndGet(newEnergie.getEnergieAmount());
			System.out.println("Neuer energielevel"+this.energieLevel.get());
		}
		//TODO neuen energielevel publishen
		
		
		this.mqttClient.messageSend(nextEnergie);
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return this.name;
	}
	
	
	
	

}
