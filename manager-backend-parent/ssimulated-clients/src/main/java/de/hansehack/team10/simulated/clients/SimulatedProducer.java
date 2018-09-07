package de.hansehack.team10.simulated.clients;

import java.time.LocalDateTime;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;

import de.hansehack.team10.connection.api.Connection;
import de.hansehack.team10.connection.api.EnergieLevel;
import de.hansehack.team10.connection.api.Message;
import de.hansehack.team10.connection.api.Offer;
import de.hansehack.team10.connection.api.Topic;

public class SimulatedProducer implements Runnable {

	private final Connection mqttClient;

	private final AtomicInteger energieLoad;

	private static final int ENERGIE_START = Integer.MAX_VALUE;

	private final String name;

	/**
	 * @param mqttClient
	 */
	public SimulatedProducer(final Connection mqttClient, final String name) {
		super();
		this.mqttClient = mqttClient;
		this.energieLoad = new AtomicInteger(SimulatedProducer.ENERGIE_START);
		this.name = name;
	}

	public void offer() {
		if (this.energieLoad.get() > 0) {
			final int energieToOffer = this.energieLoad.get() / 10;
			this.energieLoad.addAndGet(-1 * energieToOffer);
			final Offer offer = new Offer();
			offer.setAmount(energieToOffer);
			offer.setBegin(LocalDateTime.now().plusSeconds(20));
			offer.setEnd(LocalDateTime.now().plusSeconds(200));
			offer.setPrice(ThreadLocalRandom.current().nextDouble(0.19, 0.30));
			final Message message = new Message(this.name, offer, Topic.EnergieOffer,LocalDateTime.now());
			this.mqttClient.messageSend(message);
			final EnergieLevel energieLevel = new EnergieLevel(this.energieLoad.get());
			final Message energieLevelMessage = new Message(this.name, energieLevel, Topic.Energielevel,LocalDateTime.now());
			this.mqttClient.messageSend(energieLevelMessage);
		}

	}

	@Override
	public void run() {
		this.offer();
	}

}
