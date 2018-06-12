package restui.service;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import restui.model.Parameter;
import restui.model.Parameter.Direction;
import restui.model.Parameter.Location;
import restui.model.Parameter.Type;

public class ProjectServiceTest {

	@Test
	public void buildExchangesUri() {

		String projectUri = "file:/home/olivier/.restui/project.xml";
		String exchangesUri = ProjectService.buildExchangesUri(projectUri);
		System.out.println(exchangesUri);
		Assert.assertEquals("file:/home/olivier/.restui/project-exchanges.xml", exchangesUri);

		projectUri = null;
		exchangesUri = ProjectService.buildExchangesUri(projectUri);
		System.out.println(exchangesUri);
		Assert.assertNull(exchangesUri);

		projectUri = "";
		exchangesUri = ProjectService.buildExchangesUri(projectUri);
		System.out.println(exchangesUri);
		Assert.assertNull(exchangesUri);

		projectUri = "no file separator";
		exchangesUri = ProjectService.buildExchangesUri(projectUri);
		System.out.println(exchangesUri);
		Assert.assertNull(exchangesUri);

		projectUri = "file:/home/olivier/.restui/project";
		exchangesUri = ProjectService.buildExchangesUri(projectUri);
		System.out.println(exchangesUri);
		Assert.assertEquals("file:/home/olivier/.restui/project-exchanges", exchangesUri);

		projectUri = "file:/home/olivier/.restui/";
		exchangesUri = ProjectService.buildExchangesUri(projectUri);
		System.out.println(exchangesUri);
		Assert.assertNull(exchangesUri);

	}

	@Test
	public void intersection() {

		List<Parameter> exchange = new ArrayList<>();
		exchange.add(new Parameter(true, Direction.REQUEST, Location.QUERY, Type.TEXT, "page", "1"));
		exchange.add(new Parameter(true, Direction.REQUEST, Location.QUERY, Type.TEXT, "pageSize", "50"));
		exchange.add(new Parameter(true, Direction.REQUEST, Location.QUERY, Type.TEXT, "to be removed", "50"));

		List<Parameter> endpoint = new ArrayList<>();
		endpoint.add(new Parameter(true, Direction.REQUEST, Location.QUERY, Type.TEXT, "page", null));
		endpoint.add(new Parameter(true, Direction.REQUEST, Location.QUERY, Type.TEXT, "pageSize", null));
		endpoint.add(new Parameter(true, Direction.REQUEST, Location.QUERY, Type.TEXT, "fields", null));

		List<Parameter> intersection = new ArrayList<>();

		intersection.addAll(exchange);
		intersection.stream().forEach(System.out::println);

		System.out.println("---");
		intersection.retainAll(endpoint);
		intersection.stream().forEach(System.out::println);

		System.out.println("---");

		endpoint.stream().filter(p -> !intersection.contains(p)).forEach(p -> {
			intersection.add(p);
		});
		intersection.stream().forEach(System.out::println);

	}

}
