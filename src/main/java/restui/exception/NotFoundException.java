package restui.exception;

public class NotFoundException extends Exception {

	private static final long serialVersionUID = 1L;

	public NotFoundException(final String object, final String value) {
		
		super(object + " '" + value + "' not found");
	}
}
