package fr.omathe.restui.model;

import java.io.File;
import java.net.URI;
import java.nio.file.Paths;
import java.util.Optional;

import fr.omathe.restui.conf.App;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Application {

	private String lastProjectUri;
	private String styleFile;
	private final ObservableList<BaseUrl> baseUrls;

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
		if (new File(uri).exists()) {
			this.styleFile = styleFile;
		} else {
			App.getStyleUri(App.DEFAULT_STYLE).ifPresent(style -> this.styleFile = style);
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

	public ObservableList<BaseUrl> getBaseUrls() {
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
