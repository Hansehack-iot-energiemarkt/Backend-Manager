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

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Confirm [value=" + this.value + ", type=" + this.type + "]";
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
