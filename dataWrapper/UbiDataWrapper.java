package dataWrapper;

import java.math.BigInteger;

import data.live.Position;
import data.live.TagManager;
import exceptions.InvalidStreamException;

/**
 * The Ubi data wrapper is a specific data wrapper designed to parse the
 * UbiSense sensors stream.
 * 
 * @author Defossez Aurelien <defossez.a@gmail.com>
 */
public class UbiDataWrapper extends DataWrapper {
	// --------------------------------------------------------------------------
	// Constants
	// --------------------------------------------------------------------------

	/**
	 * Stream values separator.
	 */
	protected static final String VALUES_SEPARATOR = " ";

	// --------------------------------------------------------------------------
	// Constructor
	// --------------------------------------------------------------------------

	/**
	 * Creates the data wrapper specific to the UbiSense sensors.
	 * 
	 * @param us
	 *            the UbiSense object.
	 */
	public UbiDataWrapper(TagManager us) {
		super(us);
	}

	// --------------------------------------------------------------------------
	// Private methods
	// --------------------------------------------------------------------------

	/**
	 * Parses the raw line from the stream.
	 * 
	 * @param rawLine
	 *            the raw stream line.
	 * 
	 * @throws InvalidStreamException
	 *             if the stream is declared invalid ( invalid number of values
	 *             or invalid values format).
	 */
	@Override
	protected void parseData(String rawLine) throws InvalidStreamException {
		BigInteger tagId;
		double x;
		double y;
		double z;
		final String[] rawValues = rawLine.split(VALUES_SEPARATOR);

		if (logger.isDebugEnabled()) {
			logger.debug("Raw data='" + rawLine + "'");
		}

		// Invalid number of values in the stream
		if (rawValues.length != 4) {
			throw new InvalidStreamException(
					"Invalid number of values in the stream, " + "got "
							+ rawValues.length + " when expecting 4 "
							+ "(raw='" + rawLine + "')");
		}

		try {
			// Parse data
			tagId = new BigInteger(rawValues[0]);
			y = Double.parseDouble(rawValues[1]);
			z = Double.parseDouble(rawValues[2]);
			x = Double.parseDouble(rawValues[3]);

			// Update tag
			ubiSense.updateTag(tagId, new Position(x, y, z));
		} catch (final NumberFormatException e) {
			// Invalid format of values in the stream
			throw new InvalidStreamException(
					"Invalid format of values in the stream, " + "(raw='"
							+ rawLine + "')");
		}
	}
}
