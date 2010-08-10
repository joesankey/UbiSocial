package exceptions;

/**
 * Thrown when the received stream is invalid.
 * 
 * @author Defossez Aurelien <defossez.a@gmail.com>
 */
public class InvalidStreamException extends RuntimeException {
	private static final long serialVersionUID = 1;

	/**
	 * Constructs a InvalidStreamException with no detail message.
	 */
	public InvalidStreamException() {
		super();
	}

	/**
	 * Constructs a InvalidStreamException with the specified detail message.
	 * 
	 * @param message
	 *            the detail message.
	 */
	public InvalidStreamException(String message) {
		super(message);
	}
}
