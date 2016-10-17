package restui.service;

import java.io.File;
import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import restui.model.Project;

public class ApplicationService {

	public static final String APPLICATION_HOME = ".restui";

	public static void saveProject(final Project project) {
		
		createApplication();
		final String userHome = System.getProperty("user.home");
		final File projectFile = new File(userHome + File.separator + APPLICATION_HOME + File.separator + project.getName() + ".json");
		System.out.println("projectFile = " + projectFile.getAbsolutePath());
		
		final ObjectMapper mapper = new ObjectMapper();
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		
		try {
			mapper.writeValue(projectFile, project);

			final Project iot = mapper.readValue(projectFile, Project.class);
			System.out.println(iot.getName());
			System.out.println(iot.getBaseUrl());

		} catch (final JsonGenerationException e) {
			e.printStackTrace();
		} catch (final JsonMappingException e) {
			e.printStackTrace();
		} catch (final IOException e) {
			e.printStackTrace();
		}

	}

	public static void createApplication() {

		final String userHome = System.getProperty("user.home");
		final File applicationDirectory = new File(userHome + File.separator + APPLICATION_HOME);
		if (!applicationDirectory.exists()) {
			applicationDirectory.mkdir();
		}
	}

}
