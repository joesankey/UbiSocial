package data.context;

import java.util.Vector;

import data.live.Tag;

public class Building {
	private final int id;
	private final String name;
	private final Vector<Space> spaces;

	public Building(int id, String name, Vector<Space> spaces) {
		this.id = id;
		this.name = name;
		this.spaces = spaces;

	}

	public boolean containsSpace(Space space) {
		if (spaces.contains(space)) {
			return true;
		} else {
			return false;
		}
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public Vector<Space> getSpaces() {
		return spaces;
	}

	public void print() {
		System.out.println("Building - ID:" + id + " - Name: " + name);
		System.out.println("Building " + id + " spaces:");
		for (final Space space : spaces) {
			space.print();
		}
	}

	/**
	 * @param tag
	 * @return
	 */
	public String containsTag(Tag tag) {
		String vid;
		for (final Space space : spaces) {
			if((vid = space.containsTag(tag)) != null)
				return vid;
		}
		
		return null;
	}

}
