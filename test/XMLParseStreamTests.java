package test;

import java.io.IOException;

import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Before;

import clientConnector.ClientConnector;
import data.live.TagManager;

public class XMLParseStreamTests extends TestCase {
	// --------------------------------------------------------------------------
	// Class attributes
	// --------------------------------------------------------------------------

	/**
	 * The log4j logger.
	 */
	private static Logger logger = Logger.getLogger(XMLParseStreamTests.class
			.getName());

	// --------------------------------------------------------------------------
	// Attributes
	// --------------------------------------------------------------------------

	static public junit.framework.Test suite() {
		final TestSuite suite = new TestSuite();
		suite.addTestSuite(XMLParseStreamTests.class);

		return suite;
	}

	private TagManager tm;

	// --------------------------------------------------------------------------
	// Constructor
	// --------------------------------------------------------------------------

	private ClientConnector client;

	// --------------------------------------------------------------------------
	// Test suite
	// --------------------------------------------------------------------------

	public XMLParseStreamTests(String method) {
		super(method);
	}

	// --------------------------------------------------------------------------
	// SetUp and TearDown
	// --------------------------------------------------------------------------

	@Override
	@Before
	public void setUp() throws IOException {
		logger.info("Running test '" + getName() + "'");

		tm = new TagManager();
		client = new ClientConnector(tm);
	}

	@Override
	@After
	public void tearDown() {
		// Do nothing
	}

	// --------------------------------------------------------------------------
	// TODO: tests
	// --------------------------------------------------------------------------
}
