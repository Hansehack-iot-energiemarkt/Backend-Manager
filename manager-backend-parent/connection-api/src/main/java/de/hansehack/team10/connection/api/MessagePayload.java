package de.hansehack.team10.connection.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonIgnoreProperties(ignoreUnknown=true)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY)
@JsonSubTypes({
	@JsonSubTypes.Type(value=Task.class,name="task"),
	@JsonSubTypes.Type(value=Energie.class,name="energie"),
	@JsonSubTypes.Type(value=EnergieLevel.class,name="energieLevel"),
	@JsonSubTypes.Type(value=Offer.class,name="offer")
})
abstract public class MessagePayload {	
}
