package de.hansehack.team10.backend.manager;

public class AllOffers {
	
	private String address;
	
	private SavedOffer message;

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
	 * @return the offer
	 */
	public SavedOffer getMessage() {
		return this.message;
	}

	/**
	 * @param offer the offer to set
	 */
	public void setMessage(final SavedOffer message) {
		this.message = message;
	}
	
	
	

}
