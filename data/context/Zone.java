package data.context;

import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;

import data.live.Position;
import data.live.Tag;

public class Zone {
	public enum Status {
		OK
	};

	private final int id;
	private final String description;
	private final double beginX;
	private final double endX;
	private final double beginY;
	private final double endY;
	private final double beginZ;
	private final double endZ;
	private Rectangle2D.Double rec1;
	private Rectangle2D.Double rec2;
	private String vid;
	// private Status status;
	// private Vector<Product> products;
	// private Vector<Product> services;

	public Zone(int id, String description, double beginX, double endX,
			double beginY, double endY, double beginZ, double endZ,
			Status status, String vid) {
		this.id = id;
		this.description = description;
		this.beginX = beginX;
		this.endX = endX;
		this.beginY = beginY;
		this.endY = endY;
		this.beginZ = beginZ;
		this.endZ = endZ;
		this.vid = vid;
		// this.status = status;
		// this.products = new Vector<Product>(); 
		// this.services = new Vector<Product>();
		
		rec1 = new Rectangle2D.Double(beginX, beginY,
				(endX - beginX), (endY - beginY));
		
		rec2 = new Rectangle2D.Double(beginX , beginZ,
				(endX - beginX), (endZ - beginZ));
	}

	public boolean containsPosition(double x, double y, double z) {
		new Rectangle((int) beginX, (int) endY, (int) (endX - beginX),
				(int) (endY - beginY));
		
		
		new Rectangle((int) beginX, (int) beginY, (int) (endX - beginX),
				(int) (endY - beginY));

		// if(beginX)
		return true;
	}

	public double getBeginX() {
		return beginX;
	}

	public double getBeginY() {
		return beginY;
	}

	public double getBeginZ() {
		return beginZ;
	}

	public String getDescription() {
		return description;
	}

	public double getEndX() {
		return endX;
	}

	public double getEndY() {
		return endY;
	}

	public double getEndZ() {
		return endZ;
	}

	public int getId() {
		return id;
	}

	public void print() {
		System.out.println("Zone - ID: " + id + " - Description: "
				+ description + " - beginX: " + beginX + " - endX: " + endX
				+ " - beginY: " + beginY + " - endY: " + endY + " - beginZ: "
				+ beginZ + " - endZ: " + endZ);
	}
	
	/**
	 * @param tag
	 * @return
	 */
	public String containsTag(Tag tag) {
		Position pos = tag.getCurrentPosition();
		if(rec1.contains(pos.x, pos.y) && rec2.contains(pos.x, pos.y))
		{
			return vid;
		}
		else
			return null;
	}
}
