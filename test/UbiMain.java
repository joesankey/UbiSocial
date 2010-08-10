package test;

import clientConnector.ClientConnector;
import data.live.TagManager;

public class UbiMain {
	public static final String UBI_SERVER_ADDRESS = "192.168.33.39";
	public static final int UBI_SERVER_PORT = 12000;
	public static final int EXECUTION_TIME = 10000000;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		final TagManager tm = new TagManager();
		final ClientConnector conn = new ClientConnector(tm);

		// Add UbiTags
		tm.addTag("20000007106");

		// Connect to the UbiSense server
		conn.connect(UBI_SERVER_ADDRESS, UBI_SERVER_PORT);

		try {
			Thread.sleep(EXECUTION_TIME);
		} catch (final InterruptedException e) {
			// Do nothing
		}

		// Disconnect
		conn.disconnect();
	}
}
