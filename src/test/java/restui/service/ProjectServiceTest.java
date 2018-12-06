package restui.service;

import java.io.File;

import org.junit.Test;

import restui.model.BaseUrl;
import restui.model.Endpoint;
import restui.model.Parameter;
import restui.model.Parameter.Direction;
import restui.model.Parameter.Location;
import restui.model.Parameter.Type;
import restui.model.Path;
import restui.model.Project;

public class ProjectServiceTest {

	@Test
	public void saveProject() {

		Project project = new Project("test");
		project.addBaseUrl(new BaseUrl("local", "http://localhost:8080", false));
		project.addBaseUrl(new BaseUrl("dev", "http://192.168.4.23/rest", true));
		
		Path application = new Path(project, "application");
		project.addChild(application);
		
		Endpoint getApplication = new Endpoint(application, "getApplication", "GET");
		application.addChild(getApplication);
		
		Path customers = new Path(project, "customers");
		application.addChild(customers);
		
		Endpoint getCustomers = new Endpoint(customers, "getCustomers", "GET");
		customers.addChild(getCustomers);
		Parameter parameter = new Parameter(true, Direction.REQUEST, Location.HEADER, Type.TEXT, "Authorization", "");
		getCustomers.addParameter(parameter);
		
		File projectFile = new File("D:\\oma\\dev\\workspace\\RestUI\\src\\test\\resources\\savedProject.xml");
		ProjectService.saveProject(project, projectFile.toURI());
	}

}
