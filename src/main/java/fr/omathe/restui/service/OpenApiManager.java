package fr.omathe.restui.service;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.jsoniter.JsonIterator;
import com.jsoniter.any.Any;

import fr.omathe.restui.model.Endpoint;
import fr.omathe.restui.model.Parameter;
import fr.omathe.restui.model.Parameter.Direction;
import fr.omathe.restui.model.Parameter.Location;
import fr.omathe.restui.model.Parameter.Type;

public interface OpenApiManager {

	public static String openFile(final File file) {

		String json = "";

		try {
			json = Files.lines(file.toPath(), StandardCharsets.UTF_8)
					.collect(Collectors.joining());
		} catch (IOException e) {
			Logger.error(e);
			Notifier.notifyError(e.getMessage());
		}
		return json;
	}

	static List<Endpoint> extract(final String json) {

		List<Endpoint> endpoints = new ArrayList<>();

		Any any = JsonIterator.deserialize(json);

		// paths
		Map<String, Any> root = any.asMap();
		Any pathsAny = root.get("paths");

		Map<String, Any> pathMap = pathsAny.asMap();
		for (final Map.Entry<String, Any> entry : pathMap.entrySet()) {
			String path = entry.getKey();

			// GET
			Any get = entry.getValue().get("get");
			Endpoint e = collectPath(get, path, "get");
			if (e != null) {
				endpoints.add(e);
			}
			// POST
			Any post = entry.getValue().get("post");
			e = collectPath(post, path, "post");
			if (e != null) {
				endpoints.add(e);
			}
			// PUT
			Any put = entry.getValue().get("put");
			e = collectPath(put, path, "put");
			if (e != null) {
				endpoints.add(e);
			}
			// PATCH
			Any patch = entry.getValue().get("patch");
			e = collectPath(patch, path, "patch");
			if (e != null) {
				endpoints.add(e);
			}
			// DELETE
			Any delete = entry.getValue().get("delete");
			e = collectPath(delete, path, "delete");
			if (e != null) {
				endpoints.add(e);
			}
		}
		return endpoints;
	}

	public static Endpoint collectPath(final Any any, final String path, final String method) {

		Endpoint endpoint = null;

		if (!any.toString().isEmpty()) {
			String description = any.get("description").toString();
			String endpointName = any.toString("operationId");
			endpoint = new Endpoint(endpointName, path, method, description);
			// parameters
			Map<String, Any> parametersMap = any.asMap();
			for (final Map.Entry<String, Any> entry : parametersMap.entrySet()) {
				if (entry.getKey().equals("parameters")) {
					for (Any p : entry.getValue()) {
						String name = p.toString("name");
						String in = p.toString("in");
						boolean required = p.toBoolean("required");
						Parameter parameter = new Parameter(required, Direction.REQUEST, Location.getByValue(in).get(), Type.TEXT, name, null);
						endpoint.addParameter(parameter);
					}
				}
			}
		}
		return endpoint;
	}

}
