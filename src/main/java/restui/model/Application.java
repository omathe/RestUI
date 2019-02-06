package restui.model;

import java.io.File;
import java.net.URI;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

import javafx.collections.FXCollections;
import restui.service.ApplicationService;

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
		
		URI uri = URI.create(styleFile);
		if (!new File(uri).exists()) {
			this.styleFile = ApplicationService.DEFAULT_STYLE_URI;
		} else {
			this.styleFile = styleFile;
		}
		
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
	
	public Optional<BaseUrl> getEnabledBaseUrl() {
		
		return baseUrls.stream().filter(b -> b.getEnabled()).findFirst();
	}

	@Override
	public String toString() {
		return "Application [lastProjectUri=" + lastProjectUri + ", styleFile=" + styleFile + "]";
	}

}
