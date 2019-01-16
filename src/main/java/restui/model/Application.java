package restui.model;

import java.net.URI;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

import javafx.collections.FXCollections;

public class Application {

	private String lastProjectUri;
	private String styleFile;
	private final List<BaseUrl> baseUrls;

	public Application() {
		super();
		this.baseUrls = FXCollections.observableArrayList();
	}

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

	public void addBaseUrl(final BaseUrl baseUrl) {

		baseUrls.add(baseUrl);
	}

	public void removeBaseUrl(final BaseUrl baseUrl) {

		baseUrls.remove(baseUrl);
	}

	public List<BaseUrl> getBaseUrls() {
		return baseUrls;
	}

	public String getBaseUrl() {
		String baseUrl = "";
		Optional<BaseUrl> optional = baseUrls.stream().filter(b -> b.getEnabled()).findFirst();
		if (optional.isPresent()) {
			baseUrl = optional.get().getUrl();
		}
		return baseUrl;
	}

	@Override
	public String toString() {
		return "Application [lastProjectUri=" + lastProjectUri + ", styleFile=" + styleFile + "]";
	}

}
