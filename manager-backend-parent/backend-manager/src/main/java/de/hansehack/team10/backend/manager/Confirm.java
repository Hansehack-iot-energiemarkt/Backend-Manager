package de.hansehack.team10.backend.manager;

public class Confirm {
	
	private boolean value;
	
	private final String type="confirm";
	
	
	
	

	/**
	 * @return the type
	 */
	public String getType() {
		return this.type;
	}

	/**
	 * @return the value
	 */
	public boolean isValue() {
		return this.value;
	}

	/**
	 * @param value the value to set
	 */
	public void setValue(final boolean value) {
		this.value = value;
	}
	
	

}
