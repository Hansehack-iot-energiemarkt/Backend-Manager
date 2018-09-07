package de.hansehack.team10.backend.manager;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import de.hansehack.team10.connection.api.Connection;
import de.hansehack.team10.connection.api.Energie;
import de.hansehack.team10.connection.api.MessagePayload;
import de.hansehack.team10.connection.api.Offer;
import de.hansehack.team10.connection.api.Task;
import de.hansehack.team10.connection.api.Topic;

public class BackendManagerClient {
	private final Connection mqttConnection;
	private final Connection i2cConnection;

	private final List<SavedTask> tasksToExecute;

	private final List<SavedOffer> offers;

	private final List<SavedBuy> buys;

	private final UUID managerId;

	/**
	 * @param mqttConnection
	 * @param i2cConnection
	 */
	public BackendManagerClient(final Connection mqttConnection, final Connection i2cConnection) {
		super();
		this.mqttConnection = mqttConnection;
		this.i2cConnection = i2cConnection;
		this.tasksToExecute = new ArrayList<>();
		this.managerId = UUID.randomUUID();
		this.offers = new ArrayList<>();
		this.buys = new ArrayList<>();
	}

	public void start() {
		this.mqttConnection.messageReceivedSubscriber(Topic.EnergieOffer.getTopic(), (message) -> {
			final MessagePayload messagPayLoad = message.getMessage();
			if (messagPayLoad instanceof Offer) {
				final Offer offer = (Offer) messagPayLoad;
				final SavedOffer savedOffer = new SavedOffer();
				savedOffer.setBegin(offer.getBegin());
				savedOffer.setEnd(offer.getEnd());
				savedOffer.setPrice(offer.getPrice());
				savedOffer.setId(this.managerId);
				this.offers.add(savedOffer);
			}
		});

		this.mqttConnection.messageReceivedSubscriber(Topic.Energie.getTopic(), (message) -> {
			final String deviceName = message.getDeviceName();
			final MessagePayload message2 = message.getMessage();
			if (message2 instanceof Energie) {
				final Energie energie = (Energie) message2;
				final SavedBuy buy = new SavedBuy();
				buy.setId(this.managerId);
				buy.setAmount(energie.getEnergieAmount());
				buy.setDeviceName(deviceName);
				this.buys.add(buy);
			}
		});
		this.mqttConnection.messageReceivedSubscriber(Topic.TaskNew.getTopic(), (message) -> {
			final MessagePayload messagePayload = message.getMessage();
			if (messagePayload instanceof Task) {
				final Task task = (Task) messagePayload;
				final SavedTask savedTask = new SavedTask(task, message.getDeviceName());
				this.tasksToExecute.add(savedTask);
				System.out.println("Neuer Task: " + savedTask);
			}
		});
	}

	/**
	 * @return the offers
	 */
	public List<SavedOffer> getOffers() {
		return this.offers;
	}

	/**
	 * @return the buys
	 */
	public List<SavedBuy> getBuys() {
		return this.buys;
	}
	
	public void removeBuy(final SavedBuy buy) {
		this.buys.remove(buy);
	}
	
	public void removeOffer(final SavedOffer offer) {
		this.offers.remove(offer);
	}

}
