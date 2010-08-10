package clientConnector;

import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import org.apache.log4j.Logger;

public class RequestReader {
	// --------------------------------------------------------------------------
	// Class attributes
	// --------------------------------------------------------------------------

	/**
	 * The log4j logger.
	 */
	private static Logger logger = Logger.getLogger(RequestReader.class
			.getName());

	// --------------------------------------------------------------------------
	// Attributes
	// --------------------------------------------------------------------------

	/**
	 * The client connector.
	 */
	private final ClientConnector connector;

	/**
	 * The XML stream reader.
	 */
	private final XMLStreamReader reader;

	// --------------------------------------------------------------------------
	// Constructor
	// --------------------------------------------------------------------------

	/**
	 * Creates the request reader so it is ready to parse XML requests.
	 * 
	 * @throws XMLStreamException
	 */
	public RequestReader(ClientConnector connector, XMLStreamReader reader)
			throws XMLStreamException {
		this.connector = connector;
		this.reader = reader;

		// Read requests tag
		while (!reader.hasNext()) {
			;
		}
		next();
	}

	// --------------------------------------------------------------------------
	// Private methods
	// --------------------------------------------------------------------------

	/**
	 * Get next parsing event for this XML stream, ignoring comments, white
	 * spaces and plain text.
	 * 
	 * @throws XMLStreamException
	 *             if the XML is not valid syntactically or semantically.
	 */
	private void next() throws XMLStreamException {
		do {
			reader.next();
		} while ((reader.getEventType() == XMLStreamConstants.SPACE)
				|| (reader.getEventType() == XMLStreamConstants.COMMENT)
				|| (reader.getEventType() == XMLStreamConstants.CHARACTERS));
	}

	/**
	 * Parse a single command of the XML stream.
	 * 
	 * @throws XMLStreamException
	 *             if the XML is not valid syntactically or semantically.
	 */
	private void parseCommand() throws XMLStreamException {
		if (logger.isTraceEnabled()) {
			logger.trace("Parse command");
		}

		// Get first attribute name
		final String attributeName = reader.getAttributeLocalName(0);

		// Check attribute name
		if (attributeName.equals("type")) {
			// Get command type (@type)
			final String commandType = reader.getAttributeValue(0);

			// Connection command
			if (commandType.equals("connection")) {
				parseConnectionCommand();
			}
			// Disconnection command
			else if (commandType.equals("disconnection")) {
				parseDisconnectionCommand();
			}
			// Unknown command type
			else {
				throw new XMLStreamException("Unknown command type "
						+ "encountered while parsing the command " + "("
						+ attributeName + "). "
						+ "A command type must be a 'connection' or a "
						+ "'disconnection'.");
			}
		}
		// Unknown attribute
		else {
			throw new XMLStreamException("Unknown attribute encountered "
					+ "while parsing the command, got '" + attributeName + "' "
					+ "instead of 'type'.");
		}
	}

	/**
	 * Parse a specific connection command.
	 * 
	 * @throws XMLStreamException
	 *             if the XML is not valid syntactically or semantically.
	 */
	private void parseConnectionCommand() throws XMLStreamException {
		String host = "";
		int port = 0;

		if (logger.isTraceEnabled()) {
			logger.trace("Parse command :: Connection");
		}

		// Read command tag
		next();

		while (reader.getEventType() != XMLStreamConstants.END_ELEMENT) {
			final String tagName = reader.getLocalName();

			// Read host
			if (tagName.equals("host")) {
				if (logger.isTraceEnabled()) {
					logger.trace("Parse host");
				}

				// Get host text (and by extension read host tag)
				host = reader.getElementText();

				// Read host end of tag
				next();
			}

			// Read port
			else if (tagName.equals("port")) {
				if (logger.isTraceEnabled()) {
					logger.trace("Parse port");
				}

				// Read port text (and by extension read port tag)
				port = Integer.parseInt(reader.getElementText());

				// Read port end of tag
				next();
			}

			// Unknown tag
			else {
				throw new XMLStreamException("Unknown tag encountered while "
						+ "parsing the connection command, got '" + tagName
						+ "'. "
						+ "A tag in a connection command must be either a "
						+ "'host' tag or a 'port' tag.");
			}
		}

		// Connect to a database
		connector.connect(host, port);

		// Read command end of tag
		next();
	}

	/**
	 * Parse a specific disconnection command.
	 * 
	 * @throws XMLStreamException
	 *             if the XML is not valid syntactically or semantically.
	 */
	private void parseDisconnectionCommand() throws XMLStreamException {
		if (logger.isTraceEnabled()) {
			logger.trace("Parse command :: Disconnection");
		}

		// Read command tag
		next();

		// Disconnect
		connector.disconnect();

		// Read command end of tag
		next();
	}

	/**
	 * Parse a single request of the XML stream.
	 * 
	 * @throws XMLStreamException
	 *             if the XML is not valid syntactically or semantically.
	 */
	public void parseRequest() throws XMLStreamException {
		if (logger.isTraceEnabled()) {
			logger.trace("Parse request");
		}

		// End of stream
		if (reader.isEndElement()) {
			// Read requests end of tag
			next();
		} else {
			// Get node name
			final String nodeName = reader.getLocalName();

			// Command tag
			if (nodeName.equals("command")) {
				parseCommand();
			}
			// Unknown tag
			else {
				throw new XMLStreamException("Unknown tag encountered while "
						+ "parsing requests (" + nodeName + "). "
						+ "A request tag must be either a 'command' tag or a "
						+ "'query' tag.");
			}
		}
	}

	/**
	 * Parse an entire XML request stream.
	 * 
	 * @throws XMLStreamException
	 *             if the XML is not valid syntactically or semantically.
	 */
	public void parseRequests() throws XMLStreamException {
		if (logger.isTraceEnabled()) {
			logger.trace("Parse requests");
		}

		if (reader.getLocalName().equals("requests")) {
			// Read requests tag
			next();

			while (reader.getEventType() != XMLStreamConstants.END_ELEMENT) {
				parseRequest();
			}

			// Read requests end of tag
			next();
		} else {
			throw new XMLStreamException("Root node must be a 'requests' node "
					+ "instead of '" + reader.getLocalName() + "'.");
		}
	}
}
