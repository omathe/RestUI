package restui.model;

public class Application {

	private String currentProject;
	private String styleDirectory;
	private String styleFile;

	public String getCurrentProject() {
		return currentProject;
	}

	public void setCurrentProject(final String currentProject) {
		this.currentProject = currentProject;
	}

	public String getStyleDirectory() {
		return styleDirectory;
	}

	public void setStyleDirectory(final String styleDirectory) {
		this.styleDirectory = styleDirectory;
	}

	public String getStyleFile() {
		return styleFile;
	}

	public void setStyleFile(final String styleFile) {
		this.styleFile = styleFile;
	}

	@Override
	public String toString() {
		return "Application [currentProject=" + currentProject + ", styleDirectory=" + styleDirectory + ", styleFile=" + styleFile + "]";
	}

}
