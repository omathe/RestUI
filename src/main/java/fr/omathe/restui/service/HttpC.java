package fr.omathe.restui.service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;

public class HttpC {

	public static void get() {

		HttpClient client = HttpClient.newHttpClient();
		
		HttpRequest request = HttpRequest.newBuilder()
				.uri(URI.create("http://192.168.4.23:8080/application"))
				.build();

		HttpResponse<String> response;
		try {
			response = client.send(request, BodyHandlers.ofString());
			System.out.println(response.body());
			System.out.println(response.statusCode());
			System.out.println(response.version().name());
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(final String[] args) {
		
		HttpC.get();
	}

}
