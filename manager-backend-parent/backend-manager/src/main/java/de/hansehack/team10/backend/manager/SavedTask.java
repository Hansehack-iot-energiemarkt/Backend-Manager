package de.hansehack.team10.backend.manager;

import de.hansehack.team10.connection.api.Task;

public class SavedTask {
	/**
	 * Der gespeicherte Task der aufgerufen werden kann
	 */
	private final Task task;
	/**
	 * Der name des devices der den Task laufen soll
	 */
	private final String deviceName;
	/**
	 * @param task
	 * @param deviceName
	 */
	public SavedTask(final Task task, final String deviceName) {
		super();
		this.task = task;
		this.deviceName = deviceName;
	}
	/**
	 * @return the task
	 */
	public Task getTask() {
		return this.task;
	}
	/**
	 * @return the deviceName
	 */
	public String getDeviceName() {
		return this.deviceName;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "SavedTask [task=" + this.task + ", deviceName=" + this.deviceName + "]";
	}
	
	
	
	
	
	

}
