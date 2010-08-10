package test;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.math.BigInteger;
import java.net.InetAddress;
import java.util.Date;

import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import data.live.Tag;
import data.live.TagManager;
import dataWrapper.DataWrapper;
import dataWrapper.UbiDataWrapper;
import exceptions.InvalidStreamException;

public class DataWrapperParseStreamTests extends TestCase {
	// --------------------------------------------------------------------------
	// Class attributes
	// --------------------------------------------------------------------------

	/**
	 * The log4j logger.
	 */
	private static Logger logger = Logger
			.getLogger(DataWrapperParseStreamTests.class.getName());

	// --------------------------------------------------------------------------
	// Attributes
	// --------------------------------------------------------------------------

	static public junit.framework.Test suite() {
		final TestSuite suite = new TestSuite();
		suite.addTestSuite(DataWrapperParseStreamTests.class);

		return suite;
	}

	private DataWrapper wrapper;

	// --------------------------------------------------------------------------
	// Constructor
	// --------------------------------------------------------------------------

	private TagManager tm;

	// --------------------------------------------------------------------------
	// Test suite
	// --------------------------------------------------------------------------

	public DataWrapperParseStreamTests(String method) {
		super(method);
	}

	// --------------------------------------------------------------------------
	// SetUp and TearDown
	// --------------------------------------------------------------------------

	/**
	 * Invoke the private method parseData of the dataWrapper object.
	 * 
	 * @param rawLine
	 *            the raw stream line.
	 */
	private void invokeParseData(String rawLine)
			throws InvocationTargetException {
		final Object[] args = { rawLine };

		try {
			Tests.invokePrivateMethod(wrapper, "parseData", args);
		} catch (final SecurityException e) {
			e.printStackTrace();
		} catch (final IllegalArgumentException e) {
			e.printStackTrace();
		} catch (final NoSuchMethodException e) {
			e.printStackTrace();
		} catch (final IllegalAccessException e) {
			e.printStackTrace();
		}
	}

	@Override
	@Before
	public void setUp() throws IOException {
		logger.info("Running test '" + getName() + "'");

		tm = new TagManager();
		wrapper = new UbiDataWrapper(tm);
		wrapper.connect(InetAddress.getByName(Tests.UBI_SERVER_ADDRESS),
				Tests.UBI_SERVER_PORT);

		tm.addTag("20000007106");
		tm.addTag("20000007142");
		tm.addTag(new BigInteger("20000001986"));
		tm.addTag(new BigInteger("20000002010"));
		tm.addTag(new BigInteger("2134854321567351567231537"));
	}

	// --------------------------------------------------------------------------
	// Parse stream tests
	// --------------------------------------------------------------------------

	@Override
	@After
	public void tearDown() {
		wrapper.disconnect();
	}

	/**
	 * Parse stream of an existing tag: OK
	 */
	@Test
	public void testParseStream() throws InvocationTargetException {
		final Date beforeDate = new Date();
		final Tag tag = tm.getTag("20000007106");

		invokeParseData("000000000000000020000007106 " + "33.8458709716797 "
				+ "0.822576940059662 " + "-2.53210282325745");

		assertTrue(tag.getCurrentTimestamp().after(beforeDate)
				|| tag.getCurrentTimestamp().equals(beforeDate));
		assertEquals(-2.53210282325745, tag.getCurrentPosition().x);
		assertEquals(33.8458709716797, tag.getCurrentPosition().y);
		assertEquals(0.822576940059662, tag.getCurrentPosition().z);
	}

	/**
	 * Parse invalid stream (ID not integer): InvalidStreamException
	 */
	@Test
	public void testParseStreamIdNotInteger() throws Exception {
		try {
			invokeParseData("0.00000000000000020000007106 "
					+ "33.8458709716797 " + "0.822576940059662 "
					+ "-2.53210282325745");
			fail("InvalidStreamException should have been thrown.");
		} catch (final InvocationTargetException e) {
			if (Tests.checkThrownException(e, InvalidStreamException.class)) {
				// Expected InvalidStreamException
			} else {
				// Not expected exception
				fail("InvalidStreamException (by InvocationTargetException) "
						+ "should have been thrown.");
				e.getTargetException().printStackTrace();
			}
		}
	}

	/**
	 * Parse invalid stream (incomplete): InvalidStreamException
	 */
	@Test
	public void testParseStreamIncomplete() {
		try {
			invokeParseData("000000000000000020000007106 "
					+ "33.8458709716797 " + "0.822576940059662 ");
			fail("InvalidStreamException (by InvocationTargetException) "
					+ "should have been thrown.");
		} catch (final InvocationTargetException e) {
			if (Tests.checkThrownException(e, InvalidStreamException.class)) {
				// Expected InvalidStreamException
			} else {
				// Not expected exception
				fail("InvalidStreamException (by InvocationTargetException) "
						+ "should have been thrown.");
				e.getTargetException().printStackTrace();
			}
		}
	}

	/**
	 * Parse invalid stream (too many values): InvalidStreamException
	 */
	@Test
	public void testParseStreamTooManyValues() throws Exception {
		try {
			invokeParseData("000000000000000020000007106 "
					+ "33.8458709716797 " + "0.822576940059662 "
					+ "-2.53210282325745" + "12.42450002453002");
			fail("InvalidStreamException should have been thrown.");
		} catch (final InvocationTargetException e) {
			if (Tests.checkThrownException(e, InvalidStreamException.class)) {
				// Expected InvalidStreamException
			} else {
				// Not expected exception
				fail("InvalidStreamException (by InvocationTargetException) "
						+ "should have been thrown.");
				e.getTargetException().printStackTrace();
			}
		}
	}

	/**
	 * Parse stream of an unknown tag (auto-detect off): OK
	 */
	@Test
	public void testParseStreamUnknownTag() throws InvocationTargetException {
		tm.disableAutoDetect();
		invokeParseData("000000000000000020000007042 " + "33.8458709716797 "
				+ "0.822576940059662 " + "-2.53210282325745");

		assertNull(tm.getTag("20000007042"));
	}

	/**
	 * Parse stream of an unknown tag (auto-detect on): OK
	 */
	@Test
	public void testParseStreamUnknownTagAutoDetect()
			throws InvocationTargetException {
		final Date beforeDate = new Date();

		tm.enableAutoDetect();
		invokeParseData("000000000000000020000007042 " + "33.8458709716797 "
				+ "0.822576940059662 " + "-2.53210282325745");

		final Tag tag = tm.getTag("20000007042");

		assertTrue(tag.getCurrentTimestamp().after(beforeDate)
				|| tag.getCurrentTimestamp().equals(beforeDate));
		assertEquals(-2.53210282325745, tag.getCurrentPosition().x);
		assertEquals(33.8458709716797, tag.getCurrentPosition().y);
		assertEquals(0.822576940059662, tag.getCurrentPosition().z);
	}

	// --------------------------------------------------------------------------
	// Private methods
	// --------------------------------------------------------------------------

	/**
	 * Parse invalid stream (values not double): InvalidStreamException
	 */
	@Test
	public void testParseStreamValuesNotDouble() throws Exception {
		try {
			invokeParseData("000000000000000020000007106 "
					+ "33.8458709716797 " + "0.822A7F940059B62 "
					+ "-2.53210282325745");
			fail("InvalidStreamException should have been thrown.");
		} catch (final InvocationTargetException e) {
			if (Tests.checkThrownException(e, InvalidStreamException.class)) {
				// Expected InvalidStreamException
			} else {
				// Not expected exception
				fail("InvalidStreamException (by InvocationTargetException) "
						+ "should have been thrown.");
				e.getTargetException().printStackTrace();
			}
		}
	}
}
