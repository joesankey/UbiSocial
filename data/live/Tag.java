package data.live;

import java.math.BigInteger;
import java.util.Date;
import java.util.LinkedList;
import java.util.ListIterator;

import org.apache.log4j.Logger;

/**
 * A tag represents an object we are following with sensors.
 * 
 * @author Defossez Aurelien <defossez.a@gmail.com>
 */
public class Tag {
	// --------------------------------------------------------------------------
	// Class attributes
	// --------------------------------------------------------------------------

	/**
	 * The log4j logger.
	 */
	private static Logger logger = Logger.getLogger(Tag.class.getName());

	// --------------------------------------------------------------------------
	// Attributes
	// --------------------------------------------------------------------------

	/**
	 * The tag ID.
	 */
	private final BigInteger id;

	/**
	 * The list of snapshots of this tag.
	 */
	private final LinkedList<TagSnapshot> snapshots;

	/**
	 * The time in milliseconds a snapshot is kept in memory.
	 */
	private final TagManager us;

	// --------------------------------------------------------------------------
	// Constructors
	// --------------------------------------------------------------------------

	/**
	 * Creates a new tag.
	 * 
	 * @param tagId
	 *            the tag ID.
	 */
	public Tag(BigInteger tagId, TagManager us) {
		id = tagId;
		this.us = us;
		snapshots = new LinkedList<TagSnapshot>();
	}

	/**
	 * Creates a new tag.
	 * 
	 * @param tagId
	 *            the tag ID.
	 * 
	 * @throws NumberFormatException
	 *             if the tag is in the wrong format (not integer).
	 */
	public Tag(String tagId, TagManager us) throws NumberFormatException {
		this(new BigInteger(tagId), us);
	}

	// --------------------------------------------------------------------------
	// Public methods
	// --------------------------------------------------------------------------

	private void deleteOldSnapshots() {
		final long now = new Date().getTime();

		while ((snapshots.size() > 1)
				&& ((now - snapshots.getLast().getTimestamp().getTime()) > us
						.getTimeWindow())) {

			if (logger.isDebugEnabled()) {
				logger.debug(snapshots.getLast() + " deleted.");
			}

			snapshots.removeLast();
		}
	}

	/**
	 * Average of all the snapshots.
	 * 
	 * @return a Postion object which contains the average of all the current snapshots.
	 */
	public Position getAverages() {
		double x = 0, y = 0, z = 0;
		final ListIterator<TagSnapshot> it = snapshots.listIterator();
		int counter = 0;
		Position current;
		while (it.hasNext()) {
			current = it.next().getPosition();
			x = x + current.x;
			y = y + current.y;
			z = z + current.z;
			counter++;
		}
		System.out.println("Number of tags:" + counter);

		x = x / counter;
		y = y / counter;
		z = z / counter;

		return new Position(x, y, z);
	}

	/**
	 * Returns the tag last known position.
	 * 
	 * @return the tag position.
	 */
	public Position getCurrentPosition() {
		return snapshots.getFirst().getPosition();
	}

	/**
	 * Returns the time stamp of the tag last known position.
	 * 
	 * @return the time stamp.
	 */
	public Date getCurrentTimestamp() {
		return snapshots.getFirst().getTimestamp();
	}

	/**
	 * Returns the tag ID.
	 * 
	 * @return the tag ID.
	 */
	public BigInteger getId() {
		return id;
	}

	/**
	 * Returns the rate of the data streams per seconds based on the whole
	 * window of values.
	 * 
	 * @return the rate in data streams per seconds.
	 */
	public double getRate() {
		return getRate(us.getTimeWindow());
	}

	/**
	 * Returns the rate of the data streams per seconds based on the given time
	 * window.
	 * 
	 * @param timeWindow
	 *            the time window in seconds.
	 * @return the rate in data streams per seconds.
	 */
	public double getRate(long timeWindow) {
		int nbStreams = 0;
		final long limit = new Date().getTime() - timeWindow;
		final ListIterator<TagSnapshot> it = snapshots.listIterator();

		// Count streams that are after the time limit
		while (it.hasNext() && (it.next().getTimestamp().getTime() > limit)) {
			nbStreams++;
		}

		// Compute rate in streams/s
		return 1000 * (double) nbStreams / timeWindow;
	}

	// --------------------------------------------------------------------------
	// Private methods
	// --------------------------------------------------------------------------

	/**
	 * Converts this tag into a string.
	 * 
	 * @return a string representation of this tag.
	 */
	@Override
	public String toString() {
		return (snapshots.size() > 0) ? "[Tag #" + id + ": "
				+ snapshots.getFirst() + "]" : "[Tag #" + id
				+ ": <no snapshot>]";
	}

	/**
	 * Updates the position of the tag.
	 * 
	 * @param newPosition
	 *            its new position.
	 */
	public void updatePosition(Position newPosition) {
		deleteOldSnapshots();

		snapshots.addFirst(new TagSnapshot(newPosition));

		if (logger.isDebugEnabled()) {
			logger.debug(this + " updated.");
		}

		logger.debug("rate[" + id + "] = " + getRate() + " / sec");
	}
}
