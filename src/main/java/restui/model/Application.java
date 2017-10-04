package restui.model;

import java.net.URI;
import java.nio.file.Paths;

public class Application {

	private String lastProjectUri;
	private String styleFile;

	public String getLastProjectUri() {
		return lastProjectUri;
	}

	public void setLastProjectUri(final String lastProjectUri) {
		this.lastProjectUri = lastProjectUri;
	}

	public String getStyleFile() {
		return styleFile;
	}

	public void setStyleFile(final String styleFile) {
		this.styleFile = styleFile;
	}
	
	public String getStyleName() {
		final URI uri = URI.create(styleFile);
		final java.nio.file.Path path = Paths.get(uri);
		return path.getParent().getFileName().toString();
	}

	@Override
	public String toString() {
		return "Application [lastProjectUri=" + lastProjectUri + ", styleFile=" + styleFile + "]";
	}

}
