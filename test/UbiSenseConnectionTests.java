package test;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import data.live.TagManager;
import dataWrapper.DataWrapper;
import dataWrapper.UbiDataWrapper;
import exceptions.AlreadyConnectedException;

public class UbiSenseConnectionTests extends TestCase {
	// --------------------------------------------------------------------------
	// Class attributes
	// --------------------------------------------------------------------------

	/**
	 * The log4j logger.
	 */
	private static Logger logger = Logger
			.getLogger(UbiSenseConnectionTests.class.getName());

	// --------------------------------------------------------------------------
	// Attributes
	// --------------------------------------------------------------------------

	static public junit.framework.Test suite() {
		final TestSuite suite = new TestSuite();

		suite.addTest(new UbiSenseConnectionTests("testConnect"));
		suite.addTest(new UbiSenseConnectionTests("testConnectTwice"));
		suite
				.addTest(new UbiSenseConnectionTests(
						"testDisconnectNotConnected"));
		suite.addTest(new UbiSenseConnectionTests("testDisconnectTwice"));

		return suite;
	}

	// --------------------------------------------------------------------------
	// Constructor
	// --------------------------------------------------------------------------

	private DataWrapper wrapper;

	// --------------------------------------------------------------------------
	// Test suite
	// --------------------------------------------------------------------------

	public UbiSenseConnectionTests(String method) {
		super(method);
	}

	// --------------------------------------------------------------------------
	// SetUp and TearDown
	// --------------------------------------------------------------------------

	@Override
	@Before
	public void setUp() {
		logger.info("Running test '" + getName() + "'");

		wrapper = new UbiDataWrapper(new TagManager());
	}

	@Override
	@After
	public void tearDown() {
		// Do nothing
	}

	// --------------------------------------------------------------------------
	// Connection tests
	// --------------------------------------------------------------------------

	/**
	 * Good connection: OK
	 */
	@Test
	public void testConnect() throws IOException {
		wrapper.connect(InetAddress.getByName(Tests.UBI_SERVER_ADDRESS),
				Tests.UBI_SERVER_PORT);
		wrapper.disconnect();
	}

	/**
	 * Bad IP address connection: IOException
	 */
	@Test
	public void testConnectBadIp() {
		try {
			wrapper.connect(InetAddress.getByName("192.168.33.42"),
					Tests.UBI_SERVER_PORT);
			wrapper.disconnect();
			fail("IOException should have been thrown.");
		} catch (final IOException e) {
			// Expected IOException
		}
	}

	/**
	 * Bad IP format connection: IOException
	 */
	@Test
	public void testConnectBadIpFormat() {
		try {
			wrapper.connect(InetAddress.getByName("192.12"),
					Tests.UBI_SERVER_PORT);
			wrapper.disconnect();
			fail("IOException should have been thrown.");
		} catch (final IOException e) {
			// Expected IOException
		}
	}

	/**
	 * Bad port connection: IOException
	 */
	@Test
	public void testConnectBadPort() {
		try {
			wrapper.connect(InetAddress.getByName(Tests.UBI_SERVER_ADDRESS),
					12042);
			wrapper.disconnect();
			fail("IOException should have been thrown.");
		} catch (final IOException e) {
			// Expected IOException
		}
	}

	/**
	 * Double connection: AlreadyConnectedException
	 */
	@Test
	public void testConnectTwice() throws IOException {
		wrapper.connect(InetAddress.getByName(Tests.UBI_SERVER_ADDRESS),
				Tests.UBI_SERVER_PORT);

		try {
			wrapper.connect(InetAddress.getByName(Tests.UBI_SERVER_ADDRESS),
					Tests.UBI_SERVER_PORT);
			wrapper.disconnect();
			fail("AlreadyConnectedException should have been thrown.");
		} catch (final AlreadyConnectedException e) {
			// Expected AlreadyConnectedException
		}

		wrapper.disconnect();
	}

	/**
	 * Unknown host connection: UnknownHostException
	 */
	@Test
	public void testConnectUnknowmHost() throws IOException {
		try {
			wrapper.connect("www.comptuing.dcu.ie", Tests.UBI_SERVER_PORT);
			wrapper.disconnect();
			fail("UnknownHostException should have been thrown.");
		} catch (final UnknownHostException e) {
			// Expected UnknownHostException
		}
	}

	// --------------------------------------------------------------------------
	// Disconnection tests
	// --------------------------------------------------------------------------

	/**
	 * Not connected disconnection: OK
	 */
	@Test
	public void testDisconnectNotConnected() throws IOException {
		wrapper.disconnect();
	}

	/**
	 * Double disconnection: OK
	 */
	@Test
	public void testDisconnectTwice() throws IOException {
		wrapper.connect(InetAddress.getByName(Tests.UBI_SERVER_ADDRESS),
				Tests.UBI_SERVER_PORT);
		wrapper.disconnect();
		wrapper.disconnect();
	}
}
