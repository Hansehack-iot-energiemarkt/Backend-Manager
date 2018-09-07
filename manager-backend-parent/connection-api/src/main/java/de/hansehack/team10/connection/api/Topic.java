package de.hansehack.team10.connection.api;

public enum Topic {
	/**
	 * Maschine darf einen Task ausführen
	 */
	Task("hansehack/task"),
	/**
	 * Einen neuen Task anmelden. passiert in der regel beim ersten connect
	 */
	TaskNew("hansehack/newTask"),
	/**
	 * Kann nach energie fragen. 
	 */
	Energie("hansehack/energie"),
	/**
	 * Gibt den momentanen Energielevel an
	 */
	Energielevel("hansehack/energielevel"),
	/**
	 * Energie bereitstellen
	 */
	EnergieOffer("hansehack/energieoffer")
	;
	/**
	 * Topic string representation
	 */
	private String topic;

	/**
	 * @param topic
	 */
	private Topic(final String name) {
		this.topic = name;
	}

	/**
	 * @return the topic
	 */
	public String getTopic() {
		return this.topic;
	}
	
	
	
	
	

}
