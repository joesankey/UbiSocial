package main;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import data.live.TagManager;
import dataRecorder.DataRecorderApp;

public class UbiQuSe {
	// --------------------------------------------------------------------------
	// Class attributes
	// --------------------------------------------------------------------------

	/**
	 * The log4j logger.
	 */
	private static Logger logger = Logger.getLogger(TagManager.class.getName());

	// --------------------------------------------------------------------------
	// Main
	// --------------------------------------------------------------------------

	/**
	 * Initialises the application.
	 */
	public static void main(String[] args) {
		new UbiQuSe();
	}

	// --------------------------------------------------------------------------
	// Constructor
	// --------------------------------------------------------------------------

	/**
	 * Initialises the application.
	 */
	public UbiQuSe() {
		// Load log configuration
		PropertyConfigurator.configure("./log4j-config.txt");

		new DataRecorderApp();

		// Start the tag manager and pass the GUI to it, this allows the GUI to
		// receive tag information...
		// TagManager tagManager = new TagManager(gui);

		// ClientConnector clientConnector = new ClientConnector(tagManager);

		// Start the GUI

		// TEMP
		// tagManager.addTag("020000004097");
		// tagManager.addTag("020000007106");
		// clientConnector.connect("192.168.33.39", 12000);

		/*
		 * ContextLoader context = new ContextLoader(); context.loadBuilding(1);
		 * context.print();
		 */
	}
}
