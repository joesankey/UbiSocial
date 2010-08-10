package clientConnector;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import data.live.TagManager;
import dataWrapper.DataWrapper;
import dataWrapper.UbiDataWrapper;
import exceptions.AlreadyConnectedException;

public class ClientConnector extends Thread {
	// --------------------------------------------------------------------------
	// Constants
	// --------------------------------------------------------------------------

	/**
	 * UbiQuSe port.
	 */
	private static final int UBIQUSE_PORT = 4242;

	// --------------------------------------------------------------------------
	// Class attributes
	// --------------------------------------------------------------------------

	/**
	 * The log4j logger.
	 */
	private static Logger logger = Logger.getLogger(ClientConnector.class
			.getName());

	// --------------------------------------------------------------------------
	// Attributes
	// --------------------------------------------------------------------------

	/**
	 * The data wrapper, normalising data from the sensors.
	 */
	private final DataWrapper dataWrapper;

	/**
	 * The server socket.
	 */
	private ServerSocket server;

	// --------------------------------------------------------------------------
	// Constructor
	// --------------------------------------------------------------------------

	/**
	 * Creates the client connector which waits for client commands and queries.
	 */
	public ClientConnector(TagManager tagManager) {
		// Load log configuration
		PropertyConfigurator.configure("./log4j-config.txt");

		dataWrapper = new UbiDataWrapper(tagManager);
		// this.contextLoader = new ContextLoader();

		// Create socket
		try {
			server = new ServerSocket(UBIQUSE_PORT);
			start();
		} catch (final IOException e) {
			logger.fatal("Can't create socket on port " + UBIQUSE_PORT);
			e.printStackTrace();
		}
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
	 */
	public void connect(InetAddress address, int port) {
		try {
			// Connect the data wrapper
			dataWrapper.connect(address, port);
		} catch (final IOException e) {
			// TODO
		} catch (final AlreadyConnectedException e) {
			// TODO
		}
	}

	/**
	 * Connects to the specified remote host on the specified remote port.
	 * 
	 * @param host
	 *            the name of the remote host.
	 * @param port
	 *            the remote port.
	 */
	public void connect(String host, int port) {
		try {
			// Connect the data wrapper
			dataWrapper.connect(host, port);
		} catch (final UnknownHostException e) {
			// TODO
		} catch (final IOException e) {
			// TODO
		} catch (final AlreadyConnectedException e) {
			// TODO
		}
	}

	/**
	 * Disconnects from the remote host.
	 */
	public void disconnect() {
		// Disconnect the data wrapper
		dataWrapper.disconnect();
	}

	@Override
	public void run() {
		while (true) { // TODO: manage clean disconnection
			try {
				final Socket client = server.accept();
				final XMLInputFactory factory = XMLInputFactory.newInstance();

				try {
					final XMLStreamReader reader = factory
							.createXMLStreamReader(client.getInputStream());

					// XMLStreamReader reader = factory.createXMLStreamReader(
					// new FileReader(new File("test.xml")));
					final RequestReader requestReader = new RequestReader(this,
							reader);

					while (true) { // TODO: Manage multiple clients
						while (reader.hasNext()) { // TODO: Blocking wait
							requestReader.parseRequest();
						}
					}
				} catch (final XMLStreamException e) {
					e.printStackTrace();
				}
			} catch (final IOException e) {

			}
		}
	}
}
