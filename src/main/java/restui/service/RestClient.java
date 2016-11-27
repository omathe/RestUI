package restui.service;

import java.util.List;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

import restui.model.Parameter;

public class RestClient {

	public static ClientResponse get(final String uri, final List<Parameter> parameters) {

		ClientResponse response = null;
		final Client client = Client.create();

		try {
			final WebResource webResource = client.resource(uri);
			addParameters(webResource, parameters);
			response = webResource.get(ClientResponse.class);
		} catch (final Exception e) {
			e.printStackTrace();
		} finally {
			client.destroy();
		}
		return response;
	}
	
	public static ClientResponse post(final String uri, final String body, final List<Parameter> parameters) {
		
		ClientResponse response = null;
		final Client client = Client.create();
		
		try {
			final WebResource webResource = client.resource(uri);
			addParameters(webResource, parameters);
			response = webResource.type("application/json").post(ClientResponse.class, body);
		} catch (final Exception e) {
			e.printStackTrace();
		} finally {
			client.destroy();
		}
		return response;
	}

	private static WebResource addParameters(final WebResource webResource, final List<Parameter> parameters) {

		// add headers
		parameters.stream().filter(p -> p.isHeaderParameter())
				.forEach(p -> webResource.header(p.getName(), p.getValue()));

		// add query parameters
		parameters.stream().filter(p -> p.isQueryParameter())
				.forEach(p -> webResource.queryParam(p.getName(), p.getValue()));
		return webResource;
	}

}
