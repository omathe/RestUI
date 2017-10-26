package restui.controller.cellFactory;

import java.io.File;

import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.FileChooser;
import restui.model.Parameter;
import restui.model.Parameter.Type;

public class BodyParameterValueCellFactory extends TableCell<Parameter, String> {

	private TextField textField;
	private Button button;
	private String type;

	public BodyParameterValueCellFactory() {
		super();
	}

	@Override
	public void startEdit() {
		super.startEdit();

		if (getTableRow().getItem() != null) {
			final Parameter parameter = (Parameter) getTableRow().getItem();
			this.type = parameter.getType();

			if (parameter.getType().equals(Type.TEXT.name())) {
				if (textField == null) {
					createTextField();
				}
				setText(null);
				setGraphic(textField);
				textField.requestFocus();
				textField.selectAll();

			} else if (parameter.getType().equals(Type.FILE.name())) {
				if (button == null) {
					createButton();
				}
				setText(null);
				setGraphic(button);
			}
		}
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
				if (type.equals(Type.TEXT.name())) {
					if (textField != null) {
						textField.setText(getString());
					}
					setText(null);
					setGraphic(textField);
				} else if (type.equals(Type.FILE.name())) {
					setText(null);
					setGraphic(button);
				}

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

	private void createButton() {

		button = new Button("select file...");

		button.setOnAction(event -> {

			final FileChooser fileChooser = new FileChooser();
			fileChooser.setTitle("Select file");

			final File file = fileChooser.showOpenDialog(null);
			if (file != null) {
				commitEdit(file.toURI().toString());
			}
		});
	}

}