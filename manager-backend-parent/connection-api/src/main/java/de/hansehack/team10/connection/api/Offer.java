package de.hansehack.team10.connection.api;

import java.time.LocalDateTime;

public class Offer extends MessagePayload{
	
	private double price;
	
	private LocalDateTime begin;
	
	private LocalDateTime end;
	
	private int amount;

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

	/**
	 * @return the price
	 */
	public double getPrice() {
		return this.price;
	}

	/**
	 * @param price the price to set
	 */
	public void setPrice(final double price) {
		this.price = price;
	}

	/**
	 * @return the begin
	 */
	public LocalDateTime getBegin() {
		return this.begin;
	}

	/**
	 * @param begin the begin to set
	 */
	public void setBegin(final LocalDateTime begin) {
		this.begin = begin;
	}

	/**
	 * @return the end
	 */
	public LocalDateTime getEnd() {
		return this.end;
	}

	/**
	 * @param end the end to set
	 */
	public void setEnd(final LocalDateTime end) {
		this.end = end;
	}
	
	
	
	

}
