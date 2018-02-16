package restui.model;

import java.util.List;
import java.util.Optional;

import javafx.collections.FXCollections;

public class Project extends Item {

	private static final long serialVersionUID = 1L;

	private List<BaseUrl> baseUrls;

	public Project(final String name) {
		super(null, name);
		this.baseUrls = FXCollections.observableArrayList();
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

}
