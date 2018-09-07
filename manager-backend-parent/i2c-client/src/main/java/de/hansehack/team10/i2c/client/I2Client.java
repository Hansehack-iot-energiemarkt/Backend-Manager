package de.hansehack.team10.i2c.client;

import java.util.function.Consumer;

import com.pi4j.io.gpio.GpioController;

import de.hansehack.team10.connection.api.Connection;
import de.hansehack.team10.connection.api.Message;

public class I2Client  implements Connection{
	
	 private GpioController gpioController;

	@Override
	public void messageReceivedSubscriber(final String topic, final Consumer<Message> messageConsumer) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void messageSend(final Message sendMessage) {
		// TODO Auto-generated method stub
		
	}
	 

	
	 
	 
	 
	 
	
	
}
