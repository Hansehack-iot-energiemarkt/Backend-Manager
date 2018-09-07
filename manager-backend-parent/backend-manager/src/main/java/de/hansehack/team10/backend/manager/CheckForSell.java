package de.hansehack.team10.backend.manager;

public class CheckForSell {

	private boolean value;

	private SavedBuy savedBuy;
	
	private Adress adress;
	
	
	

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "CheckForSell [value=" + this.value + ", savedBuy=" + this.savedBuy + ", adress=" + this.adress + "]";
	}

	/**
	 * @return the adress
	 */
	public Adress getAdress() {
		return this.adress;
	}

	/**
	 * @param adress the adress to set
	 */
	public void setAdress(final Adress adress) {
		this.adress = adress;
	}

	/**
	 * @return the savedBuy
	 */
	public SavedBuy getSavedBuy() {
		return this.savedBuy;
	}

	/**
	 * @param savedBuy the savedBuy to set
	 */
	public void setSavedBuy(final SavedBuy savedBuy) {
		this.savedBuy = savedBuy;
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
