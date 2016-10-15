package restui.service;

import java.io.File;

import restui.model.Project;

public class ApplicationService {

	public static final String APPLICATION_HOME = ".restui";

	public void saveProject(final Project project) {

	}

	public static void createApplication() {

		final String userHome = System.getProperty("user.home");
		final File applicationDirectory = new File(userHome + File.separator + APPLICATION_HOME);
		if (!applicationDirectory.exists()) {
			applicationDirectory.mkdir();
		}
	}

}
