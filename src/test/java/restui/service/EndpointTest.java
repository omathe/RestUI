package restui.service;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import restui.model.Endpoint;

public class EndpointTest {

	@Test
	public void getPaths() {

		Endpoint endpoint = new Endpoint("getPerson", "/persons/{id}", "GET");
		List<String> paths = endpoint.getPaths();
		Assert.assertEquals(2, paths.size());
		Assert.assertEquals("persons", paths.get(0));
		Assert.assertEquals("{id}", paths.get(1));

		endpoint = new Endpoint("getPerson", "persons/{id}/", "GET");
		paths = endpoint.getPaths();
		Assert.assertEquals(2, paths.size());
		Assert.assertEquals("persons", paths.get(0));
		Assert.assertEquals("{id}", paths.get(1));

		endpoint = new Endpoint("getPerson", "", "GET");
		paths = endpoint.getPaths();
		Assert.assertEquals(0, paths.size());

		endpoint = new Endpoint("getPerson", null, "GET");
		paths = endpoint.getPaths();
		Assert.assertEquals(0, paths.size());
	}

}
