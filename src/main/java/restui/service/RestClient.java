package restui.service;

import java.util.List;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.client.urlconnection.URLConnectionClientHandler;

import restui.model.Parameter;

public class RestClient {

	public static ClientResponse get(final String uri, final List<Parameter> parameters) {

		ClientResponse response = null;
		final Client client = Client.create();

		try {
			final WebResource webResource = Client.create(new DefaultClientConfig()).resource(uri);
			final WebResource.Builder builder = webResource.getRequestBuilder();
			addHeaders(builder, parameters);
			addParameters(webResource, parameters);
			response = builder.get(ClientResponse.class);
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
			final WebResource webResource = Client.create(new DefaultClientConfig()).resource(uri);
			final WebResource.Builder builder = webResource.getRequestBuilder();
			addHeaders(builder, parameters);
			addParameters(webResource, parameters);
			response = builder.post(ClientResponse.class, body);
		} catch (final Exception e) {
			e.printStackTrace();
		} finally {
			client.destroy();
		}
		return response;
	}

	public static ClientResponse patch(final String uri, final String body, final List<Parameter> parameters) {

		ClientResponse response = null;
		final Client client = Client.create();
		try {
			final DefaultClientConfig config = new DefaultClientConfig();
			config.getProperties().put(URLConnectionClientHandler.PROPERTY_HTTP_URL_CONNECTION_SET_METHOD_WORKAROUND,
					true);
			final WebResource webResource = Client.create(config).resource(uri);
			final WebResource.Builder builder = webResource.getRequestBuilder();
			addHeaders(builder, parameters);
			addParameters(webResource, parameters);
			response = builder.method("PATCH", ClientResponse.class, body);
		} catch (final Exception e) {
			e.printStackTrace();
		} finally {
			client.destroy();
		}
		return response;
	}
	
	public static ClientResponse delete(final String uri) {
		
		ClientResponse response = null;
		final Client client = Client.create();

		try {
			final WebResource webResource = Client.create(new DefaultClientConfig()).resource(uri);
			final WebResource.Builder builder = webResource.getRequestBuilder();
			response = builder.delete(ClientResponse.class);
		} catch (final Exception e) {
			e.printStackTrace();
		} finally {
			client.destroy();
		}
		return response;
	}

	private static WebResource.Builder addHeaders(final WebResource.Builder builder, final List<Parameter> parameters) {

		// add query parameters
		parameters.stream().filter(p -> p.getEnabled() && p.isHeaderParameter())
				.forEach(p -> builder.header(p.getName(), p.getValue()));

		return builder;
	}

	private static WebResource addParameters(final WebResource webResource, final List<Parameter> parameters) {

		// add query parameters
		parameters.stream().filter(p -> p.getEnabled() && p.isQueryParameter())
				.forEach(p -> webResource.queryParam(p.getName(), p.getValue()));
		return webResource;
	}

}
