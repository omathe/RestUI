package restui.controller.cellFactory;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableCell;
import javafx.scene.control.ToggleGroup;
import restui.controller.MainController;
import restui.model.BaseUrl;

public class RadioButtonCell extends TableCell<BaseUrl, Boolean> {

	private final RadioButton radioButton;

	public RadioButtonCell(final ToggleGroup group) {
		radioButton = new RadioButton();

		radioButton.selectedProperty().addListener(new ChangeListener<Boolean>() {
			
			@Override
			public void changed(final ObservableValue<? extends Boolean> arg0, final Boolean oldValue, final Boolean newValue) {

				if (getTableRow().getItem() != null) {
					final BaseUrl baseUrl = getTableRow().getItem();
					baseUrl.setEnabled(newValue);
					
					// Update the baseUrl used in MainController
					MainController.baseUrl.set(baseUrl.getUrl());
				}
			}
		});
		
		radioButton.setToggleGroup(group);
	}

	@Override
	public void updateItem(final Boolean item, final boolean empty) {
		super.updateItem(item, empty);

		if (empty) {
			setText(null);
			setGraphic(null);
		} else {
			radioButton.setSelected(item);
			setGraphic(radioButton);
		}
	}

}
