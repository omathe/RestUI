package restui.controller.cellFactory;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableCell;
import javafx.scene.control.ToggleGroup;
import restui.model.BaseUrl;

public class RadioButtonCell extends TableCell<BaseUrl, Boolean> {

	private RadioButton radioButton;

	public RadioButtonCell(ToggleGroup group) {
		radioButton = new RadioButton();

		radioButton.selectedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> arg0, Boolean arg1, Boolean arg2) {
				if (getTableRow().getItem() != null) {
					final BaseUrl baseUrl = (BaseUrl) getTableRow().getItem();
					baseUrl.setEnabled(arg2);
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
