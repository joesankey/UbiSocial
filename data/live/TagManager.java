package data.live;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import ubiEvents.UbiEventListener;

/**
 * The UbiSense class is the main class of the project. It manages the data
 * wrapper and the tags.
 * 
 * @author Defossez Aurelien <defossez.a@gmail.com>
 */
public class TagManager {
	// --------------------------------------------------------------------------
	// Constants
	// --------------------------------------------------------------------------

	private static final int DEFAULT_TIME_WINDOW = 5000;

	// --------------------------------------------------------------------------
	// Class attributes
	// --------------------------------------------------------------------------

	/**
	 * The log4j logger.
	 */
	private static Logger logger = Logger.getLogger(TagManager.class.getName());

	// --------------------------------------------------------------------------
	// Attributes
	// --------------------------------------------------------------------------

	/**
	 * The tag list.
	 */
	private final Map<BigInteger, Tag> tags;

	/**
	 * Decides whether the application should auto-detect and add new tags or
	 * not.
	 */
	private boolean autoDetect;

	/**
	 * The time in milliseconds a tag snapshot is kept in memory.
	 */
	private long timeWindow;

	private UbiEventListener ubiEventListener;

	// --------------------------------------------------------------------------
	// Constructors
	// --------------------------------------------------------------------------

	/**
	 * Creates the UbiSense application with the auto-detect feature turned off.
	 */
	public TagManager() {
		// Initialise attributes
		tags = new HashMap<BigInteger, Tag>();
		autoDetect = false;
		timeWindow = DEFAULT_TIME_WINDOW;
	}

	// --------------------------------------------------------------------------
	// Public methods
	// --------------------------------------------------------------------------

	/**
	 * Adds a tag to the list of followed tags.
	 * 
	 * @param tagId
	 *            the tag ID.
	 */
	public void addTag(BigInteger tagId) {
		if (getTag(tagId) == null) {
			if (logger.isDebugEnabled()) {
				logger.debug("Add tag: " + tagId);
			}

			final Tag tag = new Tag(tagId, this);
			tags.put(tagId, tag);

		} else {
			logger.warn("Tag " + tagId + " is already followed.");
		}
	}

	/**
	 * Adds a tag to the list of followed tags.
	 * 
	 * @param tagId
	 *            the tag ID.
	 * 
	 * @throws NumberFormatException
	 *             if the tag is not an integer.
	 */
	public void addTag(String tagId) throws NumberFormatException {
		try {
			addTag(new BigInteger(tagId));
		} catch (final NumberFormatException e) {
			logger.error("Invalid tag name: " + tagId);
			throw e;
		}
	}

	public void addUbiEventListener(UbiEventListener lis) {
		ubiEventListener = lis;	
	}

	/**
	 * Disables the auto-detect feature.
	 */
	public void disableAutoDetect() {
		autoDetect = false;
	}

	/**
	 * Enables the auto-detect feature.
	 */
	public void enableAutoDetect() {
		autoDetect = true;
	}

	public double getAverageRate() {
		return getAverageRate(getTimeWindow());
	}

	public double getAverageRate(long timeWindow) {
		double rateSum = 0.0;

		for (final Tag tag : tags.values()) {
			rateSum += tag.getRate(timeWindow);
		}

		return rateSum / tags.size();
	}

	/**
	 * Returns the tag having the specified ID.
	 * 
	 * @param tagId
	 *            the tag ID.
	 * @return the specified tag if exists, null otherwise.
	 */
	public Tag getTag(BigInteger tagId) {
		return tags.get(tagId);
	}

	/**
	 * Returns the tag having the specified ID.
	 * 
	 * @param tagId
	 *            the tag ID.
	 * @return the specified tag if exists, null otherwise.
	 * 
	 * @throws NumberFormatException
	 *             if the tag is not an integer.
	 */
	public Tag getTag(String tagId) throws NumberFormatException {
		try {
			return tags.get(new BigInteger(tagId));
		} catch (final NumberFormatException e) {
			logger.error("Invalid tag name: " + tagId);
			throw e;
		}
	}

	/**
	 * Returns the time window, meaning the time in milliseconds a tag snapshot
	 * is kept in memory.
	 * 
	 * @returns the time window in milliseconds.
	 */
	public long getTimeWindow() {
		return timeWindow;
	}

	/**
	 * Removes a tag from the list of followed tags.
	 * 
	 * If the tag does not match any followed tags, then nothing will be
	 * removed.
	 * 
	 * @param tagId
	 *            the tagId.
	 */
	public void removeTag(BigInteger tagId) {
		if (logger.isDebugEnabled()) {
			logger.debug("Remove tag: " + tagId);
		}

		tags.remove(tagId);
	}

	/**
	 * Removes a tag from the list of followed tags.
	 * 
	 * If the tag does not match any followed tags, then nothing will be
	 * removed.
	 * 
	 * @param tagId
	 *            the tagId.
	 * 
	 * @throws NumberFormatException
	 *             if the tag is not an integer.
	 */
	public void removeTag(String tagId) throws NumberFormatException {
		try {
			removeTag(new BigInteger(tagId));
		} catch (final NumberFormatException e) {
			logger.error("Invalid tag name: " + tagId);
			throw e;
		}
	}

	/**
	 * Changes the time window, meaning the time a tag snapshot is stored in
	 * memory.
	 * 
	 * A window of zero milliseconds means that only one tag is stored at a
	 * time.
	 * 
	 * @param timeWindow
	 *            the time window in milliseconds.
	 * 
	 * @throws IllegalArgumentException
	 *             if the time window is negative.
	 */
	public void setTimeWindow(long timeWindow) throws IllegalArgumentException {
		if (timeWindow >= 0) {
			this.timeWindow = timeWindow;
		} else {
			throw new IllegalArgumentException("Time window must be positive "
					+ "(timeWindow=" + timeWindow + ")");
		}
	}

	/**
	 * Updates the values of the specified tag.
	 * 
	 * If the auto-detect feature is turned off and if the tag does not match
	 * any followed tags, then nothing will be updated. Else, the tag will be
	 * updated, and if it isn't a currently followed tag, then it will be added
	 * automatically then updated.
	 * 
	 * @param tagId
	 *            the tag ID.
	 * @param newPosition
	 *            its new position.
	 */
	public void updateTag(BigInteger tagId, Position newPosition) {
		final Tag tag = tags.get(tagId);

		// Always let the GUI know
		//ubiEventListener.tagStream(tagId, newPosition);

		if (tag != null) {
			// Update tag position only if it is known
			tag.updatePosition(newPosition);
			ubiEventListener.tagUpdated(tag);
		
		} else if (autoDetect) {
			// If auto detect is on, add the new tag and update it
			if (logger.isDebugEnabled()) {
				logger.debug("Unknown tag detected: " + tagId + ", adding it.");
			}

			addTag(tagId);
			updateTag(tagId, newPosition);
		} else {
			if (logger.isTraceEnabled()) {
				logger.trace("Unknown tag detected: " + tagId
						+ ", ignoring it.");
			}
		}
	}
}
