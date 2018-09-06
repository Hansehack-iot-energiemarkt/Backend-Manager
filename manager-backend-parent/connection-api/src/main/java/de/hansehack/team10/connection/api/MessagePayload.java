package de.hansehack.team10.connection.api;

import java.util.Optional;

import javax.annotation.Nullable;

final public class MessagePayload {
	/**
	 * Energie message
	 */
	private final Optional<Energie> energie;
	/**
	 * Task message
	 */
	private final Optional<Task> task;
	/**
	 * @param energie
	 * @param task
	 */
	public MessagePayload(@Nullable final Energie energie,@Nullable final Task task) {
		super();
		this.energie = Optional.ofNullable(energie);
		this.task = Optional.ofNullable(task);
		if(!this.energie.isPresent()&&!this.task.isPresent()) {
			throw new IllegalArgumentException("Einer der beiden Objekte muss present sein"); 
		}
	}
	/**
	 * @return the energie
	 */
	public Optional<Energie> getEnergie() {
		return this.energie;
	}
	/**
	 * @return the task
	 */
	public Optional<Task> getTask() {
		return this.task;
	}
	
	
	
	
	
	
	
}
