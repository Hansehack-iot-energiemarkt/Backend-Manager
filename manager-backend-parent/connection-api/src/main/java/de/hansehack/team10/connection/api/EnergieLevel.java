package de.hansehack.team10.connection.api;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class EnergieLevel extends MessagePayload{
	private final int energieLevel;

	/**
	 * @param energieLevel
	 */
	@JsonCreator
	public EnergieLevel(@JsonProperty(value="energieLevel") final int energieLevel) {
		super();
		this.energieLevel = energieLevel;
	}

	/**
	 * @return the energieLevel
	 */
	public int getEnergieLevel() {
		return this.energieLevel;
	}
	
	
	
	
	
	
}
