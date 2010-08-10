package data.context;

import java.util.Vector;

import data.live.Tag;

public class Space {
	private final int id;
	private final String name;
	private final double length;
	private final double width;
	private final double height;
	private final Vector<Zone> zones;

	public Space(int id, String name, double length, double width,
			double height, Vector<Zone> zones) {
		this.id = id;
		this.name = name;
		this.length = length;
		this.width = width;
		this.height = height;
		this.zones = zones;
	}

	public boolean containsZone(Zone zone) {
		if (zones.contains(zone)) {
			return true;
		} else {
			return false;
		}
	}

	public double getHeight() {
		return height;
	}

	public int getId() {
		return id;
	}

	public double getLength() {
		return length;
	}

	public String getName() {
		return name;
	}

	public double getWidth() {
		return width;
	}

	public Vector<Zone> getZones() {
		return zones;
	}

	public void print() {
		System.out.println("Space - ID:" + id + " - Name: " + name
				+ " - Length: " + length + " - Width: " + width + " - Height: "
				+ height);
		System.out.println("Space " + id + " zones:");
		for (final Zone zone : zones) {
			zone.print();
		}
	}

	/**
	 * @param tag
	 * @return
	 */
	public String containsTag(Tag tag) {
		String vid;
		for (final Zone zone : zones) {
			if((vid = zone.containsTag(tag)) != null)
				return vid;
		}
		
		return null;
	}

}
