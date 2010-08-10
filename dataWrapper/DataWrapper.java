package dataWrapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import org.apache.log4j.Logger;

import data.live.TagManager;
import exceptions.AlreadyConnectedException;
import exceptions.InvalidStreamException;

/**
 * The data wrapper connects to the remote host and then reads and parses data
 * from it.
 * 
 * @author Defossez Aurelien <defossez.a@gmail.com>
 */
public abstract class DataWrapper extends Thread {
	// --------------------------------------------------------------------------
	// Constants
	// --------------------------------------------------------------------------

	/**
	 * Polling time in milliseconds when synchronising with the stream reader in
	 * order to disconnect properly.
	 */
	protected static final int DISCONNECTION_SYNC_POLLING_PERIOD = 100;

	// --------------------------------------------------------------------------
	// Class attributes
	// --------------------------------------------------------------------------

	/**
	 * The log4j logger.
	 */
	protected static Logger logger = Logger.getLogger(DataWrapper.class
			.getName());

	// --------------------------------------------------------------------------
	// Attributes
	// --------------------------------------------------------------------------

	/**
	 * The UbiSense object.
	 */
	protected TagManager ubiSense;

	/**
	 * The socket bond to the server sending data.
	 */
	protected Socket socket;

	/**
	 * The reader that read streams coming from the server.
	 */
	protected BufferedReader reader;

	/**
	 * If true, then the socket is connected to a server.
	 */
	protected boolean connected;

	/**
	 * If true, then the reader is currently reading a stream.
	 */
	protected boolean reading;

	// --------------------------------------------------------------------------
	// Constructor
	// --------------------------------------------------------------------------

	/**
	 * Creates the data wrapper.
	 * 
	 * @param us
	 *            the UbiSense object.
	 */
	public DataWrapper(TagManager us) {
		super("StreamReader");
		ubiSense = us;
		connected = false;
		reading = false;
	}

	// --------------------------------------------------------------------------
	// Public methods
	// --------------------------------------------------------------------------

	/**
	 * Connects to the specified remote host on the specified remote port.
	 * 
	 * @param address
	 *            the IP address of the remote host.
	 * @param port
	 *            the remote port.
	 * 
	 * @throws IOException
	 *             if an I/O error occurs when creating the socket.
	 * @throws AlreadyConnectedException
	 *             if there is already a connection in place to a remote host.
	 */
	public void connect(InetAddress address, int port) throws IOException,
			AlreadyConnectedException {
		if (connected) {
			logger.warn("Already connected.");
			throw new AlreadyConnectedException("Already connected to "
					+ socket.getInetAddress() + ":" + socket.getPort());
		} else {
			if (logger.isDebugEnabled()) {
				logger.debug("Connecting to " + address + ":" + port + "...");
			}

			// Create and open socket
			socket = new Socket(address, port);

			if (logger.isTraceEnabled()) {
				logger.trace("Socket open.");
			}

			connectReader();

			if (logger.isInfoEnabled()) {
				logger.info("Connected to " + address + ":" + port + ".");
			}
		}
	}

	/**
	 * Connects to the specified remote host on the specified remote port.
	 * 
	 * @param host
	 *            the name of the remote host.
	 * @param port
	 *            the remote port.
	 * 
	 * @throws UnknownHostException
	 *             if the IP address of the host could not be determined.
	 * @throws IOException
	 *             if an I/O error occurs when creating the socket.
	 * @throws AlreadyConnectedException
	 *             if there is already a connection in place to a remote host.
	 */
	public void connect(String host, int port) throws UnknownHostException,
			IOException, AlreadyConnectedException {
		if (connected) {
			logger.warn("Already connected.");
			throw new AlreadyConnectedException("Already connected to "
					+ socket.getInetAddress() + ":" + socket.getPort());
		} else {
			if (logger.isDebugEnabled()) {
				logger.debug("Connecting to " + host + ":" + port + "...");
			}

			try {
				// Create and open socket
				socket = new Socket(host, port);

				if (logger.isTraceEnabled()) {
					logger.trace("Socket open.");
				}

				connectReader();

				if (logger.isInfoEnabled()) {
					logger.info("Connected to " + host + ":" + port + ".");
				}
			}

			// Unknown host exception
			catch (final UnknownHostException e) {
				logger.error("Server connection error, unknown host: " + host
						+ ":" + port);
				throw e;
			}

			// IO exception
			catch (final IOException e) {
				logger.error("Server IO error. (" + e.getMessage() + ")");
				throw e;
			}
		}
	}

	/**
	 * Connects the reader and starts the thread.
	 * 
	 * The thread will read and parse data from the remote host.
	 * 
	 * @throws IOException
	 *             if an I/O error occurs when creating the reader.
	 */
	private void connectReader() throws IOException {
		try {
			// Create input buffered reader
			reader = new BufferedReader(new InputStreamReader(socket
					.getInputStream()));

			connected = true;
			start();
		} catch (final IOException e) {
			logger.error("Stream reader creation error. (" + e.getMessage()
					+ ")");
			throw e;
		}
	}

	/**
	 * Disconnects from the remote host.
	 */
	public void disconnect() {
		connected = false;

		if (logger.isDebugEnabled()) {
			logger.debug("Disconnecting...");
		}

		try {
			// Wait for the reader to stop reading
			while (reading) {
				try {
					if (logger.isTraceEnabled()) {
						logger.trace("Waiting for the reader to release"
								+ " the socket...");
					}

					Thread.sleep(DISCONNECTION_SYNC_POLLING_PERIOD);
				} catch (final InterruptedException e) {
					// Do nothing
				}
			}

			// Close reader
			if (reader != null) {
				reader.close();
			}

			// Close socket
			if (socket != null) {
				socket.close();
			}

			if (logger.isTraceEnabled()) {
				logger.trace("Reader and socket closed normally.");
			}

		} catch (final IOException e) {
			logger.warn("Error while closing reader and socket.");
		}

		if (logger.isDebugEnabled()) {
			logger.debug("Disconnected...");
		}
	}

	/**
	 * Check whether the data wrapper is connected or not.
	 * 
	 * @return true if the data wrapper is currently connected to a remote host.
	 */
	public boolean isConnected() {
		return connected;
	}

	/**
	 * Parses the raw line from the stream.
	 * 
	 * @param rawLine
	 *            the raw stream line.
	 * 
	 * @throws InvalidStreamException
	 *             if the stream is declared invalid.
	 */
	protected abstract void parseData(String rawLine)
			throws InvalidStreamException;

	// --------------------------------------------------------------------------
	// Private methods
	// --------------------------------------------------------------------------

	/**
	 * Reads and parses the data from the remote host.
	 * 
	 * If the stream has a bad format (not enough values or wrong format for
	 * some values) then the whole packet is ignored properly. If a stream IO
	 * error appears, then the data wrapper automatically disconnects from the
	 * remote host.
	 */
	public void readData() {
		reading = true;

		try {
			if (reader.ready()) {
				if (logger.isTraceEnabled()) {
					logger.trace("Reading data from the stream...");
				}

				// Parse stream
				parseData(reader.readLine());

				// TEMP; TODO: Check for differences between Windows & Linux
				reader.readLine();
			}
		} catch (final IOException e) {
			logger.error("Stream IO error, disconnecting...");
			disconnect();
		} catch (final InvalidStreamException e) {
			logger.error(e.getMessage());
		}

		reading = false;
	}

	// --------------------------------------------------------------------------
	// Abstract methods
	// --------------------------------------------------------------------------

	/**
	 * Reads data from the remote host while connected.
	 */
	@Override
	public void run() {
		if (logger.isTraceEnabled()) {
			logger.trace("Thread started.");
		}

		// Read data while connected to the server
		while (connected) {
			readData();
		}

		if (logger.isTraceEnabled()) {
			logger.trace("Thread ended.");
		}
	}
}
