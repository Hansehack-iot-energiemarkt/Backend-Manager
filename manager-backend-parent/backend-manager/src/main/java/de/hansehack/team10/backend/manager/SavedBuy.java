package de.hansehack.team10.backend.manager;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class SavedBuy {

	private UUID id;

	private int amount;
	@JsonIgnore
	private String deviceName;
	
	
	
	private final String type="buy";
	
	
	
	
	
	

	/**
	 * @return the type
	 */
	public String getType() {
		return this.type;
	}

	

	/**
	 * @return the deviceName
	 */
	public String getDeviceName() {
		return this.deviceName;
	}

	/**
	 * @param deviceName the deviceName to set
	 */
	public void setDeviceName(final String deviceName) {
		this.deviceName = deviceName;
	}

	/**
	 * @return the id
	 */
	public UUID getId() {
		return this.id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(final UUID id) {
		this.id = id;
	}

	/**
	 * @return the amount
	 */
	public int getAmount() {
		return this.amount;
	}

	/**
	 * @param amount the amount to set
	 */
	public void setAmount(final int amount) {
		this.amount = amount;
	}



	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "SavedBuy [id=" + this.id + ", amount=" + this.amount + ", deviceName=" + this.deviceName + ", type=" + this.type + "]";
	}
	
	

}
