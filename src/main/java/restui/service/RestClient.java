package restui.service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

import javax.ws.rs.core.MultivaluedMap;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.client.urlconnection.URLConnectionClientHandler;
import com.sun.jersey.core.util.MultivaluedMapImpl;

import restui.model.Parameter;
import restui.model.Parameter.Location;
import restui.model.Parameter.Type;
import restui.model.Request;
import restui.model.Request.BodyType;

public class RestClient {

	/**
	 * Create a new resource
	 *
	 * @param request
	 *            - The request
	 */
	public static ClientResponse post(final Request request) {

		ClientResponse response = null;
		final Client client = Client.create();

		String body = null;
		try {
			String uri = request.getUri();
			List<Parameter> parameters = request.getParameters();

			final WebResource webResource = client.resource(uriWithoutQueryParams(uri)).queryParams(buildParams(parameters));
			final WebResource.Builder builder = webResource.getRequestBuilder();

			if (request.getBodyType().equals(BodyType.X_WWW_FORM_URL_ENCODED)) {
				request.addParameter(new Parameter(true, Type.TEXT, Location.HEADER, "content-type", "application/x-www-form-urlencoded"));
				body = parameters.stream()
						.filter(p -> p.getEnabled() && p.isBodyParameter())
						.map(p -> encode(p.getName()) + "=" + encode(p.getValue()))
						.collect(Collectors.joining("&"));
			}
			addHeaders(builder, parameters);
			response = builder.post(ClientResponse.class, body);
		} catch (final Exception e) {
			e.printStackTrace();
		} finally {
			client.destroy();
		}
		return response;
	}

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

			// multipart/form-data
			final StringBuilder stringBuilder = new StringBuilder();
			stringBuilder.append("--oma\r\n");
			// stringBuilder.append("Content-Disposition: form-data; name=\"file\";
			// filename=\"a.txt\"\r\n");
			parameters.stream().filter(p -> p.getEnabled() && p.isBodyParameter()).forEach(p -> {
				System.err.println("=> " + p.getValue());
				stringBuilder.append("Content-Disposition: form-data; name=\"" + p.getName() + "\"; filename=\"" + p.getValue() + "\r\n");
				final URI uri2 = URI.create(p.getValue());
				final Path path = Paths.get(uri2);
				byte[] content = null;
				try {
					content = Files.readAllBytes(path);
				} catch (final IOException e) {
				}
				try {
					stringBuilder.append(new String(content, "UTF-8"));
				} catch (final UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			});

			// final StringBuilder stringBuilder = new StringBuilder();
			// stringBuilder.append("--oma\r\n");
			// stringBuilder.append("Content-Disposition: form-data; name=\"file\";
			// filename=\"a.txt\"\r\n");
			// stringBuilder.append("Content-Type: application/octet-stream\r\n\r\n");
			stringBuilder.append("Content-Type: text/plain\r\n\r\n");

			// stringBuilder.append("Je suis Olivier MATHE !\r\n\r\n");

			// final File file = new File("/home/olivier/tmp/style.css");
			// final Path path = file.toPath();
			// byte[] content = null;
			// try {
			// content = Files.readAllBytes(path);
			// } catch (final IOException e) {
			// }

			stringBuilder.append("\r\n\r\n");
			stringBuilder.append("--oma");

			response = builder.post(ClientResponse.class, stringBuilder.toString());

			// response = builder.post(ClientResponse.class, body);
		} catch (final Exception e) {
			e.printStackTrace();
		} finally {
			client.destroy();
		}
		return response;
	}

	/**
	 * Put resource
	 *
	 * @param uri
	 * @param body
	 * @param parameters
	 * @return
	 */
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

	/**
	 * Patch resource
	 *
	 * @param uri
	 * @param body
	 * @param parameters
	 * @return
	 */
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

	/**
	 * Delete resource
	 *
	 * @param uri
	 * @param body
	 * @param parameters
	 * @return
	 */
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
		parameters.stream().filter(p -> p.getEnabled() && p.isHeaderParameter()).forEach(p -> builder.header(p.getName(), p.getValue()));

		return builder;
	}

	private static MultivaluedMap<String, String> buildParams(final List<Parameter> parameters) {

		final MultivaluedMap<String, String> params = new MultivaluedMapImpl();
		for (final Parameter parameter : parameters) {
			if (parameter.getEnabled() && (parameter.isQueryParameter() /* || parameter.isBodyParameter() */)) {
				try {
					final String encodedName = URLEncoder.encode(parameter.getName(), "UTF-8");
					final String encodedValue = URLEncoder.encode(parameter.getValue(), "UTF-8").replaceAll("[+]", "%20");
					params.add(encodedName, encodedValue);
				} catch (final UnsupportedEncodingException e) {
					e.printStackTrace();
				}
			}
		}
		return params;
	}

	private static String uriWithoutQueryParams(final String uri) {

		return uri.split("[?]")[0];
	}

	private static String encode(String value) {

		String encodedValue = null;
		try {
			encodedValue = URLEncoder.encode(value, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return encodedValue;
	}

	public static void main(final String[] args) {

		final StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("--oma\r\n");
		stringBuilder.append("Content-Disposition: form-data; name=\"file\"; filename=\"a.txt\"\r\n");
		stringBuilder.append("Content-Type: text/plain\r\n\r\n");

		stringBuilder.append("Je suis Olivier MATHE !\r\n\r\n");
		stringBuilder.append("--oma");

		System.out.println(stringBuilder.toString());
	}
}
