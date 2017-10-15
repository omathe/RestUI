package restui.controller.cellFactory;

import javafx.event.EventHandler;
import javafx.scene.control.TableCell;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import restui.model.Parameter;

public class BodyParameterValueCellFactory extends TableCell<Parameter, String> {

	private TextField textField;

	public BodyParameterValueCellFactory() {
		super();
	}

	@Override
	public void startEdit() {
		super.startEdit();

		if (textField == null) {
			createTextField();
		}
		setText(null);
		setGraphic(textField);
		textField.requestFocus();
		textField.selectAll();
	}

	@Override
	public void cancelEdit() {
		super.cancelEdit();

		setText(getString());
		setGraphic(null);
	}

	@Override
	public void updateItem(final String item, final boolean empty) {
		super.updateItem(item, empty);

		if (empty) {
			setText(null);
			setGraphic(null);
		} else {
			if (isEditing()) {
				if (textField != null) {
					textField.setText(getString());
				}
				setText(null);
				setGraphic(textField);
			} else {
				setText(getString());
				setGraphic(null);
			}
		}
	}

	private String getString() {
		return getItem() == null ? "" : getItem().toString();
	}

	private void createTextField() {

		textField = new TextField(getString());

		textField.setOnKeyReleased(new EventHandler<KeyEvent>() {

			@Override
			public void handle(final KeyEvent e) {

				if (e.getCode() == KeyCode.ENTER) {
					commitEdit(textField.getText());
				} else if (e.getCode() == KeyCode.ESCAPE) {
					cancelEdit();
				}
			}
		});
	}

}
