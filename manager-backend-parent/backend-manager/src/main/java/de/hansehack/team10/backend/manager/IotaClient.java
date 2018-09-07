package de.hansehack.team10.backend.manager;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

import com.fasterxml.jackson.databind.ObjectMapper;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;

public class IotaClient implements Runnable {

	private final BackendManagerClient backendClient;

	private final static String IOTACLIENT_URL = "";

	private final Map<String, SavedBuy> buyAddressToBuy;

	private final Map<String, SavedOffer> offerAddressToOffer;

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

	private Optional<String> sell(final SavedOffer savedOffer) {
		RequestBody body;
		try {
			body = RequestBody.create(MediaType.parse("application/json"), this.mapper.writeValueAsBytes(savedOffer));
			final Request request = new Request.Builder().url(IotaClient.IOTACLIENT_URL + "/sell").post(body).build();
			return Optional.of(this.client.newCall(request).execute().body().string());
		} catch (final IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return Optional.empty();
	}

	private Optional<Adress[]> getOffers() {
		final Request request = new Request.Builder().url(IotaClient.IOTACLIENT_URL + "/sell").get().build();
		try {
			final String json = this.client.newCall(request).execute().body().string();
			return Optional.of(this.mapper.readValue(json.getBytes(), Adress[].class));
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
			
		} catch (final  IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private Optional<Confirm> buy(final SavedBuy savedBuy){
		try {
			final RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), this.mapper.writeValueAsBytes(savedBuy));
			final Request request = new Request.Builder().url(IotaClient.IOTACLIENT_URL+"/buy/"+savedBuy.getAddress()).post(requestBody).build();
			final ResponseBody body = this.client.newCall(request).execute().body();
			return Optional.of( this.mapper.readValue(body.bytes(), Confirm.class));
		} catch (final IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return Optional.empty();
	}

	@Override
	public void run() {
		
	}

}
