package de.hansehack.team10.backend.manager;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

import com.fasterxml.jackson.databind.ObjectMapper;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;

public class IotaClient implements Runnable {

	private final BackendManagerClient backendClient;

	private final static String IOTACLIENT_URL = "";

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
		this.client = new OkHttpClient();
	}

	private Optional<Adress> sell(final SavedOffer savedOffer) {
		RequestBody body;
		try {
			body = RequestBody.create(MediaType.parse("application/json"), this.mapper.writeValueAsBytes(savedOffer));
			final Request request = new Request.Builder().url(IotaClient.IOTACLIENT_URL + "/sell").post(body).build();
			return Optional
					.of(this.mapper.readValue(this.client.newCall(request).execute().body().bytes(), Adress.class));
		} catch (final IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return Optional.empty();
	}

	private Optional<SavedOffer[]> getOffers() {
		final Request request = new Request.Builder().url(IotaClient.IOTACLIENT_URL + "/sell").get().build();
		try {
			final String json = this.client.newCall(request).execute().body().string();
			return Optional.of(this.mapper.readValue(json.getBytes(), SavedOffer[].class));
		} catch (final IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return Optional.empty();
	}

	private Optional<CheckForSell> checkForSell(final SavedOffer savedOffer) {
		final Request request = new Request.Builder()
				.url(IotaClient.IOTACLIENT_URL + "/sell/" + savedOffer.getAddress()).get().build();
		try {
			final ResponseBody body = this.client.newCall(request).execute().body();
			return Optional.of(this.mapper.readValue(body.bytes(), CheckForSell.class));
		} catch (final IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return Optional.empty();
	}

	private void confirmSell(final CheckForSell checkForSell, final Confirm confirm) {
		try {
			final RequestBody body = RequestBody.create(MediaType.parse("application/json"),
					this.mapper.writeValueAsBytes(confirm));
			final Request request = new Request.Builder()
					.url(IotaClient.IOTACLIENT_URL + "/confirm/" + checkForSell.getAdress()).post(body).build();
			this.client.newCall(request).execute();

		} catch (final IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private Optional<Confirm> buy(final SavedBuy savedBuy) {
		try {
			final RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"),
					this.mapper.writeValueAsBytes(savedBuy));
			final Request request = new Request.Builder()
					.url(IotaClient.IOTACLIENT_URL + "/buy/" + savedBuy.getAddress()).post(requestBody).build();
			final ResponseBody body = this.client.newCall(request).execute().body();
			return Optional.of(this.mapper.readValue(body.bytes(), Confirm.class));
		} catch (final IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return Optional.empty();
	}

	@Override
	public void run() {
		while (true) {
			final List<SavedBuy> buys = this.backendClient.getBuys();
			final List<SavedOffer> offers = this.backendClient.getOffers();
			final List<Object> objectsToRemove = new LinkedList<>();
			offers.stream().forEach(offer -> {
				this.sell(offer).ifPresent(address -> this.offerAddressToOffer.put(address, offer));
			});
			// Verkauf
			CompletableFuture.runAsync(() -> {
				offers.stream().forEach(offer -> {
					final Optional<CheckForSell> checkForSell = this.checkForSell(offer);
					if (checkForSell.isPresent()) {
						final Confirm confirm = new Confirm();
						confirm.setValue(true);
						this.confirmSell(checkForSell.get(), confirm);
						objectsToRemove.add(offer);
					}
				});
			});
			CompletableFuture.runAsync(() -> {
				this.getOffers().ifPresent(offerArray -> {
					buys.stream().parallel().forEach(buy -> {
						final Optional<SavedOffer> findFirst = Stream.of(offerArray)
								.filter(offer -> offer.getAmount() >= buy.getAmount()).findFirst();
						if (findFirst.isPresent()) {
							final Optional<Confirm> confirmation = this.buy(buy);
							if (confirmation.isPresent()) {
								objectsToRemove.add(buy);
							}
						}
					});
				});

			});
		}
	}

}
