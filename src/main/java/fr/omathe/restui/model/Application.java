package fr.omathe.restui.model;

import java.util.Optional;

import fr.omathe.restui.conf.App;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Application {

	private String lastProjectUri;
	private String style;
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

	public String getStyle() {
		return style;
	}

	public void setStyle(final String style) {

		if (App.getStyleUri(App.DEFAULT_STYLE).isPresent()) {
			this.style = style;
		}
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
		return "Application [lastProjectUri=" + lastProjectUri + ", style=" + style + "]";
	}

}