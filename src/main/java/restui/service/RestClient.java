package restui.service;

import java.util.List;

import javax.ws.rs.core.MultivaluedMap;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.client.urlconnection.URLConnectionClientHandler;
import com.sun.jersey.core.util.MultivaluedMapImpl;

import restui.model.Parameter;

public class RestClient {

	/**
	 * Get resource
	 * 
	 * @param uri
	 * @param parameters
	 * @return
	 */
	public static ClientResponse get(final String uri, final List<Parameter> parameters) {

		ClientResponse response = null;
		final Client client = Client.create();

		try {
			final WebResource webResource = client.resource(uriWithoutQueryParams(uri)).queryParams(buildParams(parameters));
			final WebResource.Builder builder = webResource.getRequestBuilder();

			addHeaders(builder, parameters);

			response = builder.get(ClientResponse.class);
		} catch (final Exception e) {
			e.printStackTrace();
		} finally {
			client.destroy();
		}
		return response;
	}

	/**
	 * Post resource
	 * 
	 * @param uri
	 * @param body
	 * @param parameters
	 * @return
	 */
	public static ClientResponse post(final String uri, final String body, final List<Parameter> parameters) {

		ClientResponse response = null;
		final Client client = Client.create();

		try {
			final WebResource webResource = client.resource(uriWithoutQueryParams(uri)).queryParams(buildParams(parameters));
			final WebResource.Builder builder = webResource.getRequestBuilder();

			addHeaders(builder, parameters);

			response = builder.post(ClientResponse.class, body);
		} catch (final Exception e) {
			e.printStackTrace();
		} finally {
			client.destroy();
		}
		return response;
	}

	public static ClientResponse put(final String uri, final String body, final List<Parameter> parameters) {

		ClientResponse response = null;
		final Client client = Client.create();

		try {
			final WebResource webResource = client.resource(uriWithoutQueryParams(uri)).queryParams(buildParams(parameters));
			final WebResource.Builder builder = webResource.getRequestBuilder();

			addHeaders(builder, parameters);

			response = builder.put(ClientResponse.class, body);
		} catch (final Exception e) {
			e.printStackTrace();
		} finally {
			client.destroy();
		}
		return response;
	}

	public static ClientResponse patch(final String uri, final String body, final List<Parameter> parameters) {

		ClientResponse response = null;
		final DefaultClientConfig config = new DefaultClientConfig();
		config.getProperties().put(URLConnectionClientHandler.PROPERTY_HTTP_URL_CONNECTION_SET_METHOD_WORKAROUND, true);
		final Client client = Client.create(config);

		try {

			final WebResource webResource = client.resource(uriWithoutQueryParams(uriWithoutQueryParams(uri))).queryParams(buildParams(parameters));
			final WebResource.Builder builder = webResource.getRequestBuilder();

			addHeaders(builder, parameters);

			response = builder.method("PATCH", ClientResponse.class, body);

		} catch (final Exception e) {
			e.printStackTrace();
		} finally {
			client.destroy();
		}
		return response;
	}

	public static ClientResponse delete(final String uri, final List<Parameter> parameters) {

		ClientResponse response = null;
		final Client client = Client.create();

		try {
			final WebResource webResource = client.resource(uriWithoutQueryParams(uri)).queryParams(buildParams(parameters));
			final WebResource.Builder builder = webResource.getRequestBuilder();

			addHeaders(builder, parameters);

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

	private static MultivaluedMap<String, String> buildParams(final List<Parameter> parameters) {

		final MultivaluedMap<String, String> params = new MultivaluedMapImpl();
		for (final Parameter parameter : parameters) {
			params.add(parameter.getName(), parameter.getValue());
		}
		return params;
	}

	private static String uriWithoutQueryParams(final String uri) {

		return uri.split("[?]")[0];
	}
}
