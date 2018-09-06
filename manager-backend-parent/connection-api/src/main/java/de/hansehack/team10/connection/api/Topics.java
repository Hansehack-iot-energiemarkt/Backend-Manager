package de.hansehack.team10.connection.api;

public enum Topics {
	/**
	 * Maschine darf einen Task ausführen
	 */
	Task("hansehack/task"),
	/**
	 * Einen neuen Task anmelden. passiert in der regel beim ersten connect
	 */
	TaskNew("hansehack/newTask");
	/**
	 * Topic string representation
	 */
	private String topic;

	/**
	 * @param topic
	 */
	private Topics(final String name) {
		this.topic = name;
	}
	
	

}
