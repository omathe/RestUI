package restui.service;

import org.junit.Assert;
import org.junit.Test;

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

}
