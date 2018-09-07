package de.hansehack.team10.restapi;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;

import de.hansehack.team10.connection.api.Connection;
import de.hansehack.team10.connection.api.EnergieLevel;
import de.hansehack.team10.connection.api.Message;
import de.hansehack.team10.connection.api.MessagePayload;
import de.hansehack.team10.connection.api.Topic;
import io.javalin.Javalin;
import io.javalin.apibuilder.ApiBuilder;

public class Api {

	private final Javalin app;

	private final Map<String, Integer> energyLevelOfDevices;

	private final List<Message> offers;

	private final List<Message> buyers;

	private final Connection mqttConnection;
	
	private final ObjectMapper mapper;

	public Api(final Connection mqttConnection) {
		this.app = Javalin.create().enableCorsForAllOrigins().start(8080);
		this.energyLevelOfDevices = new LinkedHashMap<>();
		this.offers = new ArrayList<>();
		this.buyers = new ArrayList<>();
		this.mqttConnection = mqttConnection;
		this.mapper = new ObjectMapper();
		this.mapper.findAndRegisterModules();
		this.connectToMqtt();
		this.generateApi();
	}
	
	
	
	public void stop() {
		this.app.stop();
	}

	private void connectToMqtt() {
		this.mqttConnection.messageReceivedSubscriber(Topic.Energielevel.getTopic(), (message) -> {
			final MessagePayload payload = message.getMessage();
			if(payload instanceof EnergieLevel) {
				this.energyLevelOfDevices.put(message.getDeviceName(), ((EnergieLevel)payload).getEnergieLevel());
			}
		});
		this.mqttConnection.messageReceivedSubscriber(Topic.EnergieOffer.getTopic(), (message)->{
			
			this.offers.add(message);
		});
		
		this.mqttConnection.messageReceivedSubscriber(Topic.Energie.getTopic(), (message)->{
			this.buyers.add(message);
		});
		
		

	}

	private void generateApi() {
		this.app.routes(()->{
			ApiBuilder.path("energielevel",()->{
				ApiBuilder.get((context)->{
					final byte[] energieLevelOfDevicesJson = this.mapper.writeValueAsBytes(this.energyLevelOfDevices);
					context.result(new String(energieLevelOfDevicesJson));
				});
				
			});
			ApiBuilder.path("buyers", ()->{
				ApiBuilder.get((context)->{
					context.result( this.mapper.writeValueAsString(this.buyers));
				});
			});
			ApiBuilder.path("offers", ()->{
				ApiBuilder.get((context)->{
					context.result(this.mapper.writeValueAsString(this.offers));
				});
			});
			
		});

	}

}
