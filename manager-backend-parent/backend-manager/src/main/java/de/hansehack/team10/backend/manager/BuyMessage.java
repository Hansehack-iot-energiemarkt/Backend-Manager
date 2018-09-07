package de.hansehack.team10.backend.manager;

public class BuyMessage {
	
	private String address;
	
	private SavedBuy message;

	/**
	 * @return the address
	 */
	public String getAddress() {
		return this.address;
	}

	/**
	 * @param address the address to set
	 */
	public void setAddress(final String address) {
		this.address = address;
	}

	/**
	 * @return the message
	 */
	public SavedBuy getMessage() {
		return this.message;
	}

	/**
	 * @param message the message to set
	 */
	public void setMessage(final SavedBuy message) {
		this.message = message;
	}
	
	

}
