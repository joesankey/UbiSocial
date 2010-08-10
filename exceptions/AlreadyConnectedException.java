package exceptions;

/**
 * Thrown when the application is already connected to a remote host.
 * 
 * @author Defossez Aurelien <defossez.a@gmail.com>
 */
public class AlreadyConnectedException extends IllegalStateException {
	private static final long serialVersionUID = 1;

	/**
	 * Constructs a AlreadyConnectedException with no detail message.
	 */
	public AlreadyConnectedException() {
		super();
	}

	/**
	 * Constructs a AlreadyConnectedException with the specified detail message.
	 * 
	 * @param message
	 *            the detail message.
	 */
	public AlreadyConnectedException(String message) {
		super(message);
	}
}
