package restui.controller.events;

import javafx.event.EventHandler;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class SearchItemEvents {

	public static EventHandler<KeyEvent> keyPressed = keyEvent -> {
		
		if (keyEvent.getCode() == KeyCode.ENTER) {
            System.out.println("ENTER was released");
        }
	};
}
