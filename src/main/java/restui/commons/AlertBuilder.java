package restui.commons;

import java.util.Optional;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;

public interface AlertBuilder {

	static ButtonType confirm(final String title, final String header) {

		final Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle(title);
		alert.setHeaderText(header);
		final Optional<ButtonType> result = alert.showAndWait();
		return result.get();
	}

}
