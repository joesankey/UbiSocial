package contextLoader;

// THis comment for git.
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import data.context.Building;
import data.context.Space;
import data.context.Zone;
import data.context.Zone.Status;
import data.live.Tag;

public class Context {
	private final DBConnector connector;
	final Vector<Building> buildings = new Vector<Building>();
	
	public Context() {
		connector = new DBConnector();
		connector.connect();
	}

	/*
	 * create table Building( id int, name varchar(40), spaceCount int, spaceID
	 * int );
	 */

	public Vector<Building> loadBuilding(int buildingID) {
		final String buildingStmt = "SELECT * FROM BUILDING WHERE ID ="
				+ buildingID;
		final ResultSet buildingResults = connector.executeQuery(buildingStmt);
		final Vector<Space> spaces = new Vector<Space>();

		try {
			int id = 0;
			String name = null;
			int spaceID;
			Building building;
			while (buildingResults.next()) {
				id = buildingResults.getInt(1);
				name = buildingResults.getString(2);
				buildingResults.getInt(3);
				spaceID = buildingResults.getInt(4);
				spaces.add(loadSpace(spaceID));
				building = new Building(id, name, spaces);
				buildings.add(building);
			}
			

		} catch (final SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return buildings;
	}

	/*
	 * create table Space( id int, name varchar(40), length float, width float,
	 * height float, zoneID int );
	 */
	Space loadSpace(int spaceID) {
		Space space = null;
		final String spaceStmt = "SELECT * FROM SPACES WHERE ID =" + spaceID;
		final ResultSet spaceResults = connector.executeQuery(spaceStmt);
		final Vector<Zone> zones = new Vector<Zone>();

		try {
			int id = 0;
			String name = null;
			float length = 0;
			float width = 0;
			float height = 0;
			int zoneID;
			while (spaceResults.next()) {
				id = spaceResults.getInt(1);
				name = spaceResults.getString(2);
				length = spaceResults.getFloat(3);
				width = spaceResults.getFloat(4);
				height = spaceResults.getFloat(5);
				zoneID = spaceResults.getInt(6);
				zones.add(loadZone(zoneID));
			}

			space = new Space(id, name, length, width, height, zones); // Space(String
			// id,
			// String
			// name,
			// double
			// length,
			// double
			// width,
			// double
			// height)

		} catch (final SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return space;
	}

	/*
	 * create table Zone( id int, Description varchar(40), BeginX float, EndX
	 * float, BeginY float, EndY float, BeginZ float, EndZ float );
	 */

	private Zone loadZone(int zoneID) {
		Zone zone = null;
		final String zoneStmt = "SELECT * FROM ZONES WHERE ID =" + zoneID;
		final ResultSet zoneResults = connector.executeQuery(zoneStmt);

		try {

			int id = 0;
			String description = null;
			double beginX = 0;
			double endX = 0;
			double beginY = 0;
			double endY = 0;
			double beginZ = 0;
			double endZ = 0;
			final Status status = null;
			String vid = null;
			while (zoneResults.next()) {
				id = zoneResults.getInt(1);
				description = zoneResults.getString(2);
				beginX = zoneResults.getDouble(3);
				endX = zoneResults.getDouble(4);
				beginY = zoneResults.getDouble(5);
				endY = zoneResults.getDouble(6);
				beginZ = zoneResults.getDouble(7);
				endZ = zoneResults.getDouble(8);
			    vid = zoneResults.getString(9);
			}

			zone = new Zone(id, description, beginX, endX, beginY, endY,
					beginZ, endZ, status, vid);

		} catch (final SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return zone;
	}

	public void print() {
		for (final Building building : buildings) {
			building.print();
		}
	}

	/**
	 * @param tag
	 * @return
	 */
	public String containsTag(Tag tag) {
		String vid;
		for (final Building building : buildings) {
			if((vid = building.containsTag(tag)) != null)
				return vid;
		}
		
		return null;
	}
}
