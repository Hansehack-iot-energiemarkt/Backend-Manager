package de.hansehack.team10.connection.api;

import java.time.Duration;

public class Task extends MessagePayload{

	private final Duration durationOfTask;

	private final int energyAmount;

	/**
	 * @param durationOfTask
	 * @param energyAmount
	 */
	public Task(final Duration durationOfTask, final int energyAmount) {
		super();
		this.durationOfTask = durationOfTask;
		this.energyAmount = energyAmount;
	}

	/**
	 * @return the durationOfTask
	 */
	public Duration getDurationOfTask() {
		return this.durationOfTask;
	}

	/**
	 * @return the energyAmount
	 */
	public int getEnergyAmount() {
		return this.energyAmount;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Task [durationOfTask=" + this.durationOfTask + ", energyAmount=" + this.energyAmount + "]";
	}
	

}
