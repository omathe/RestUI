package restui.model;

import java.nio.file.Paths;

public class Application {

	private String currentProject;
	private String styleFile;

	public String getCurrentProject() {
		return currentProject;
	}

	public void setCurrentProject(final String currentProject) {
		this.currentProject = currentProject;
	}

	public String getStyleFile() {
		return styleFile;
	}

	public void setStyleFile(final String styleFile) {
		this.styleFile = styleFile;
	}
	
	public String getStyleName() {
		return Paths.get(styleFile).getParent().getFileName().toString();
	}

	@Override
	public String toString() {
		return "Application [currentProject=" + currentProject + ", styleFile=" + styleFile + "]";
	}

}
