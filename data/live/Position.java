package data.live;

/**
 * A position in 3D space (x, y, z).
 * 
 * @author Defossez Aurelien <defossez.a@gmail.com>
 */
public class Position {
	// --------------------------------------------------------------------------
	// Attributes
	// --------------------------------------------------------------------------

	/**
	 * The position on the X axis.
	 */
	public double x;

	/**
	 * The position on the Y axis.
	 */
	public double y;

	/**
	 * The position on the Z axis.
	 */
	public double z;

	// --------------------------------------------------------------------------
	// Constructor
	// --------------------------------------------------------------------------

	/**
	 * Creates a position object based on its x, y and z values.
	 * 
	 * @param x
	 *            the position on the X axis.
	 * @param y
	 *            the position on the Y axis.
	 * @param z
	 *            the position on the Z axis.
	 */
	public Position(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
}
