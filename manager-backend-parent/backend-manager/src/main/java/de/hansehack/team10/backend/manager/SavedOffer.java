package de.hansehack.team10.backend.manager;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class SavedOffer {
	
	private int amount;
	
	private String type="sell";
	
	

	/**
	 * @return the type
	 */
	public String getType() {
		return this.type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(final String type) {
		this.type = type;
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

	private UUID id;

	private LocalDateTime begin;

	private LocalDateTime end;

	private double price;
	@JsonIgnore
	private String address;
	
	Optional<Adress> adress;
	
	

	/**
	 * @return the adress
	 */
	public Optional<Adress> getAdress() {
		return this.adress;
	}

	/**
	 * @param adress the adress to set
	 */
	public void setAdress(final Optional<Adress> adress) {
		this.adress = adress;
	}

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

}
