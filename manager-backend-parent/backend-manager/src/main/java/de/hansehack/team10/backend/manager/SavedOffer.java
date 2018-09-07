package de.hansehack.team10.backend.manager;

import java.time.LocalDateTime;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class SavedOffer {

	private UUID id;

	private LocalDateTime begin;

	private LocalDateTime end;

	private double price;
	@JsonIgnore
	private String address;

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
