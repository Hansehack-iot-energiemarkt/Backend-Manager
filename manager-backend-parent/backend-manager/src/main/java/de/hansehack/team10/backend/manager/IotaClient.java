package de.hansehack.team10.backend.manager;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;

public class IotaClient implements Runnable {

	private final BackendManagerClient backendClient;

	private final static String IOTACLIENT_URL = "http://10.250.252.59:5000";

	private final static int PORT = 5000;

	private final Map<Adress, SavedBuy> buyAddressToBuy;

	private final Map<Adress, SavedOffer> offerAddressToOffer;

	private final ObjectMapper mapper;

	private final OkHttpClient client;

	/**
	 * @param backendClient
	 */
	public IotaClient(final BackendManagerClient backendClient) {
		super();
		this.backendClient = backendClient;
		this.buyAddressToBuy = new LinkedHashMap<>();
		this.offerAddressToOffer = new LinkedHashMap<>();
		this.mapper = new ObjectMapper();
		this.mapper.findAndRegisterModules();
		this.client = new OkHttpClient.Builder().connectTimeout(1, TimeUnit.HOURS).writeTimeout(1, TimeUnit.HOURS)
				.readTimeout(1, TimeUnit.HOURS).build();
		final JavaTimeModule javaTimeModule = new JavaTimeModule();
		javaTimeModule.addDeserializer(LocalDateTime.class,
				new LocalDateTimeDeserializer(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
		this.mapper.registerModule(javaTimeModule);
		this.mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

	}

	private Optional<Adress> sell(final SavedOffer savedOffer) {
		if (savedOffer == null) {
			System.out.println(savedOffer);
			return Optional.empty();
		}
		RequestBody body;
		try {
			body = RequestBody.create(MediaType.parse("application/json"), this.mapper.writeValueAsBytes(savedOffer));
			final Request request = new Request.Builder().url(IotaClient.IOTACLIENT_URL + "/sell/").post(body).build();
			return Optional
					.of(this.mapper.readValue(this.client.newCall(request).execute().body().bytes(), Adress.class));
		} catch (final Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return Optional.empty();
	}

	private Optional<AllOffers[]> getOffers() {
		final Request request = new Request.Builder().url(IotaClient.IOTACLIENT_URL + "/sell/").get().build();
		try {
			final String json = this.client.newCall(request).execute().body().string();
			return Optional.of(this.mapper.readValue(json.getBytes(), AllOffers[].class));
		} catch (final IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return Optional.empty();
	}

	private Optional<BuyMessage[]> checkForSell(final AllOffers savedOffer) {
		final Request request = new Request.Builder()
				.url(IotaClient.IOTACLIENT_URL + "/sell/" + savedOffer.getAddress()).get().build();
		try {
			final ResponseBody body = this.client.newCall(request).execute().body();
			return Optional.of(this.mapper.readValue(body.bytes(), BuyMessage[].class));
		} catch (final IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return Optional.empty();
	}

	private void confirmSell(final Confirmation checkForSell, final ConfirmMessage confirmMessage) {
		try {
			final RequestBody body = RequestBody.create(MediaType.parse("application/json"),
					this.mapper.writeValueAsBytes(confirmMessage));
			final Request request = new Request.Builder()
					.url(IotaClient.IOTACLIENT_URL + "/confirm/" + confirmMessage.getAddress()).post(body).build();
			this.client.newCall(request).execute();

		} catch (final IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private Optional<ConfirmMessage> buy(final BuyMessage savedBuy, final String address) {
		try {
			System.out.println(savedBuy);
			final RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"),
					this.mapper.writeValueAsBytes(savedBuy));
			final Request request = new Request.Builder().url(IotaClient.IOTACLIENT_URL + "/buy/" + address+"/")
					.post(requestBody).build();
			final ResponseBody body = this.client.newCall(request).execute().body();
			return Optional.of(this.mapper.readValue(body.bytes(), ConfirmMessage.class));
		} catch (final IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return Optional.empty();
	}
	
	

	@Override
	public void run() {
		while (true) {
			System.out.println("Server gestartet");
			final List<SavedBuy> buys = this.backendClient.getBuys();
			final List<SavedOffer> offers = this.backendClient.getOffers();
			final List<Object> objectsToRemove = new LinkedList<>();
			System.out.println(buys);
			System.out.println(offers);
			try {
				Thread.sleep(1000);
			} catch (final InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			if (!offers.isEmpty() && !buys.isEmpty()) {
				final SavedOffer offer = offers.get(0);
				

				System.out.println(offer);
				this.sell(offer).ifPresent(address -> {
					System.out.println(offer + " address:" + address);
					this.offerAddressToOffer.put(address, offer);

				});
				System.out.println(offer + " verkauft");

				try {
					Thread.sleep(500);
				} catch (final InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				// Verkauf
				final Optional<AllOffers[]> offers2 = this.getOffers();
				if (offers2.isPresent() && offers2.get().length > 0 && !buys.isEmpty()) {
					final BuyMessage buyMessage= new BuyMessage();
					buyMessage.setAddress(offers2.get()[0].getAddress());
					buyMessage.setMessage(buys.get(0));
					final Optional<ConfirmMessage> conFirmBuy = this.buy(buyMessage, offers2.get()[0].getAddress());
					System.out.println(offers2);
					final Optional<BuyMessage[]> checkForSell = this.checkForSell(offers2.get()[0]);
					System.out.println(checkForSell);
					if (checkForSell.isPresent() && checkForSell.get().length > 0) {
						System.out.println("Kaufe");
						System.out.println(conFirmBuy);
						if (conFirmBuy.isPresent()) {
							final Confirmation confirmation = new Confirmation();
							confirmation.setId(this.backendClient.getId());
							this.confirmSell(confirmation, conFirmBuy.get());
							System.out.println("Konfirmed");
							offers.remove(offer);
							buys.remove(buys.get(0));
							
						}

					}
				}

			}
		}
	}

}
