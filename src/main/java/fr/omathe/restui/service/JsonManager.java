package fr.omathe.restui.service;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Map;
import java.util.stream.Collectors;

import com.jsoniter.JsonIterator;
import com.jsoniter.any.Any;

import fr.omathe.restui.model.Endpoint;

public class JsonManager {

	public static String openFile(final File file) {

		String json = "";

		try {
			json = Files.lines(file.toPath(), StandardCharsets.UTF_8)
					.collect(Collectors.joining());
		} catch (IOException e) {
			e.printStackTrace();
		}
		//System.out.println(json);
		return json;
	}

	public static void extract(final String json) {

		String endpointName = null;
		String path = null;
		String description = null;
		String method = null;

		Any any = JsonIterator.deserialize(json);

		// openapi
		//		String openapi = any.toString("openapi");
		//		System.out.println("openapi = " + openapi);

		// paths
		Map<String, Any> root = any.asMap();
		//System.out.println("root " + root);
		Any pathsAny = root.get("paths");
		//System.out.println("pathsAny " + pathsAny);
		//System.out.println("number of paths " + pathsAny.size());

		Map<String, Any> pathMap = pathsAny.asMap();
		//System.out.println("path " + pathMap);
		for (final Map.Entry<String, Any> entry : pathMap.entrySet()) {
			path = entry.getKey();
			//System.out.println("path = " + path);

			// GET
			Any get = entry.getValue().get("get");
			if (!get.toString().isEmpty()) {
				description = get.get("description").toString();
				method = "GET";
				endpointName = get.toString("operationId");
				Endpoint endpoint = new Endpoint(endpointName, path, method, description);
				System.out.println(endpoint.display());
				// parameters
				Map<String, Any> parametersMap = get.asMap();
				//System.out.println("parametersMap=" + parametersMap);
				for (final Map.Entry<String, Any> entry2 : parametersMap.entrySet()) {
					//System.out.println("> " + entry2.getKey());
					if (entry2.getKey().equals("parameters")) {
						//System.out.println("value = " + entry2.getValue());
						entry2.getValue().forEach(x -> {
							System.out.println("x=" + x);
							String name = x.toString("name");
							System.out.println("parameter name = " + name);
							String in = x.toString("in");
							System.out.println("in = " + in);
							boolean required = x.toBoolean("required");
							System.out.println("required = " + required);
						});
					}
				}
			}
			// POST
			Any post = entry.getValue().get("post");
			if (!post.toString().isEmpty()) {
				description = post.get("description").toString();
				method = "POST";
				endpointName = post.toString("operationId");
				Endpoint endpoint = new Endpoint(endpointName, path, method, description);
				System.out.println(endpoint.display());
			}

		}
	}

	public static void object() {

		@SuppressWarnings("preview")
		String json = """
				{
				  "id": 1,
				  "name": {
				    "firstName": "Joe",
				    "surname": "Blogg"
				  }
				}
				""";

		Any any = JsonIterator.deserialize(json);
		System.out.println(any);

		Integer id = any.toInt("id");
		System.out.println("id = " + id);

		String firstName = any.toString("name", "firstName");
		System.out.println("firstName = " + firstName);

	}

	public static void main(final String[] args) {

		//JsonManager.object();
		String json = JsonManager.openFile(new File("E:\\oma\\dev\\workspace\\RestUI\\src\\test\\resources\\openapi.json"));
		JsonManager.extract(json);
	}

}
