package de.hansehack.team10.connection.api;

import java.time.Duration;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Energie extends MessagePayload{
	/**
	 * Energie needed per Hour
	 */
	private final int energieAmount;
	/**
	 * How long will the energie be needed.
	 */
	private final Duration energiePerHour;
	/**
	 * @param energieAmount
	 * @param energiePerHour
	 */
	@JsonCreator
	public Energie(@JsonProperty(value="energieAmount") final int energieAmount,@JsonProperty(value="energiePerHour") final Duration energiePerHour) {
		super();
		this.energieAmount = energieAmount;
		this.energiePerHour = energiePerHour;
	}
	/**
	 * @return the energieAmount
	 */
	public int getEnergieAmount() {
		return this.energieAmount;
	}
	/**
	 * @return the energiePerHour
	 */
	public Duration getEnergiePerHour() {
		return this.energiePerHour;
	}
	
	
	

}
