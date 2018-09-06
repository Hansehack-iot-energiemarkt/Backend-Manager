package de.hansehack.team10.backend.manager;

import java.util.ArrayList;
import java.util.List;

import de.hansehack.team10.connection.api.Connection;
import de.hansehack.team10.connection.api.Energie;
import de.hansehack.team10.connection.api.MessagePayload;
import de.hansehack.team10.connection.api.Task;
import de.hansehack.team10.connection.api.Topic;

public class BackendManagerClient {
	private final Connection mqttConnection;
	private final Connection i2cConnection;

	private final List<SavedTask> tasksToExecute;

	/**
	 * @param mqttConnection
	 * @param i2cConnection
	 */
	public BackendManagerClient(final Connection mqttConnection, final Connection i2cConnection) {
		super();
		this.mqttConnection = mqttConnection;
		this.i2cConnection = i2cConnection;
		this.tasksToExecute = new ArrayList<>();
	}

	public void start() {
		this.mqttConnection.messageReceivedSubscriber(Topic.Energie.getTopic(), (message) -> {
			final String deviceName = message.getDeviceName();
			final MessagePayload message2 = message.getMessage();
			if (message2 instanceof Energie) {
				final Energie energie = (Energie) message2;
				System.out.println("Kaufe strom für " + deviceName + "amount "+energie.getEnergieAmount());
			}
		});
		this.mqttConnection.messageReceivedSubscriber(Topic.TaskNew.getTopic(), (message)->{
			final MessagePayload messagePayload = message.getMessage();
			if(messagePayload instanceof Task) {
				final Task task = (Task) messagePayload;
				final SavedTask savedTask = new SavedTask(task, message.getDeviceName());
				this.tasksToExecute.add(savedTask);
				System.out.println("Neuer Task: "+savedTask);
			}
		});
		
		
	}

}
