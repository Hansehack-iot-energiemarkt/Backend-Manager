package de.hansehack.team10.backend.manager;

public class Adress {

	private String address;
	
	

	/**
	 * @return the adress
	 */
	public String getAddress() {
		return this.address;
	}

	/**
	 * @param adress the adress to set
	 */
	public void setAddress(final String address) {
		this.address = address;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Adress [address=" + this.address + "]";
	}
	
	

}
