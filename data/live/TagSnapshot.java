package data.live;

import java.util.Date;

public class TagSnapshot {
	// --------------------------------------------------------------------------
	// Attributes
	// --------------------------------------------------------------------------

	/**
	 * The tag last known position (x, y, z).
	 */
	private final Position position;

	/**
	 * The time stamp of the last known position.
	 */
	private final Date timestamp;

	// --------------------------------------------------------------------------
	// Constructor
	// --------------------------------------------------------------------------

	/**
	 * Creates a snapshot of the tag based on its actual position.
	 */
	public TagSnapshot(Position position) {
		timestamp = new Date();
		this.position = position;
	}

	// --------------------------------------------------------------------------
	// Public methods
	// --------------------------------------------------------------------------

	/**
	 * Returns the position where the tag was when this snapshot was taken.
	 * 
	 * @return the tag position.
	 */
	public Position getPosition() {
		return position;
	}

	/**
	 * Returns the time stamp of this snapshot.
	 * 
	 * @return the time stamp.
	 */
	public Date getTimestamp() {
		return timestamp;
	}

	/**
	 * Converts this tag into a string.
	 * 
	 * @return a string representation of this tag.
	 */
	@Override
	public String toString() {
		return "[TagSnapshot " + "x=" + position.x + "; " + "y=" + position.y
				+ "; " + "z=" + position.z + " " + "(" + timestamp + ")]";
	}
}
