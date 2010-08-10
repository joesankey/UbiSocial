package contextLoader;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.log4j.Logger;

public class DBConnector {
	private final static String driver = "org.apache.derby.jdbc.EmbeddedDriver";
	private final static String dbName = "ContextDatabase1";
	private final static String connectionURL = "jdbc:derby:" + dbName
			+ ";create=true";

	private Connection conn;
	private static Statement stmt = null;

	private static Logger logger = Logger
			.getLogger(DBConnector.class.getName());

	public DBConnector() {

	}

	public void connect() {
		try {
			Class.forName(driver);
			conn = DriverManager.getConnection(connectionURL);
			logger.debug("Connected to database: " + dbName);

			/*
			 * //TODO: TEMP Statement s = conn.createStatement(); ResultSet r =
			 * s.executeQuery("SELECT * FROM ubizone"); while(r.next()) {
			 * System.out.println("zone "+r.getString(1)+" : "+r.getString(2));
			 * } // /TEMP
			 
			
			int results = 0;
			try {
				stmt = conn.createStatement();

				results = stmt.executeUpdate("INSERT INTO BUILDING VALUES(1,'SCHOOL OF COMPUTING2',2,6)");

			} catch (final SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			*/
			
			

		} catch (final ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (final SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void disconnect() {
		try {
			conn.close();
			logger.debug("Disconnected from database: " + dbName);
		} catch (final SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	ResultSet executeQuery(String query) {
		ResultSet results = null;
		try {
			stmt = conn.createStatement();

			results = stmt.executeQuery(query);

		} catch (final SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return results;
	}
}
