package test;

import java.io.IOException;
import java.math.BigInteger;
import java.util.Date;

import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import data.live.Position;
import data.live.Tag;
import data.live.TagManager;

public class UbiSenseTagManagementTests extends TestCase {
	// --------------------------------------------------------------------------
	// Class attributes
	// --------------------------------------------------------------------------

	/**
	 * The log4j logger.
	 */
	private static Logger logger = Logger
			.getLogger(UbiSenseTagManagementTests.class.getName());

	// --------------------------------------------------------------------------
	// Attributes
	// --------------------------------------------------------------------------

	static public junit.framework.Test suite() {
		final TestSuite suite = new TestSuite();
		suite.addTestSuite(UbiSenseTagManagementTests.class);

		return suite;
	}

	// --------------------------------------------------------------------------
	// Constructor
	// --------------------------------------------------------------------------

	private TagManager tm;

	// --------------------------------------------------------------------------
	// Test suite
	// --------------------------------------------------------------------------

	public UbiSenseTagManagementTests(String method) {
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

		tm.addTag("20000007106");
		tm.addTag("20000007142");
		tm.addTag(new BigInteger("20000001986"));
		tm.addTag(new BigInteger("20000002010"));
		tm.addTag(new BigInteger("2134854321567351567231537"));
	}

	@Override
	@After
	public void tearDown() {
		// Do nothing
	}

	// --------------------------------------------------------------------------
	// Add Tag tests
	// --------------------------------------------------------------------------

	/**
	 * Add a big integer tag ID: OK
	 */
	@Test
	public void testAddTagBigInteger() {
		tm.addTag(new BigInteger("21348543215673515672315374543548"));

		assertNotNull(tm.getTag("21348543215673515672315374543548"));
	}

	/**
	 * Add a big integer tag ID twice: OK
	 */
	@Test
	public void testAddTagBigIntegerTwice() {
		tm.addTag(new BigInteger("20000001234"));
		tm.addTag(new BigInteger("20000001234"));

		assertNotNull(tm.getTag("20000001234"));
	}

	/**
	 * Add a big string tag ID: OK
	 */
	@Test
	public void testAddTagBigString() {
		tm.addTag("1351321015531021548624324653435435435488673542131548556768");

		assertNotNull(tm
				.getTag("1351321015531021548624324653435435435488673542131548556768"));
	}

	/**
	 * Add string tag ID: OK
	 */
	@Test
	public void testAddTagString() {
		tm.addTag("20000001234");

		assertNotNull(tm.getTag("20000001234"));
	}

	/**
	 * Add a bad string (characters) tag ID: NumberFormatException
	 */
	@Test
	public void testAddTagStringCharacters() {
		try {
			tm.addTag("Lorem ipsum dolor sit amet, consectetur adipiscing...");
			fail("NumberFormatException should have been thrown.");
		} catch (final NumberFormatException e) {
			// Expected NumberFormatException
		}
	}

	/**
	 * Add a bad string (float) tag ID: NumberFormatException
	 */
	@Test
	public void testAddTagStringFloat() {
		try {
			tm.addTag("3.14159265");
			fail("NumberFormatException should have been thrown.");
		} catch (final NumberFormatException e) {
			// Expected NumberFormatException
		}
	}

	/**
	 * Add a string tag ID twice: OK
	 */
	@Test
	public void testAddTagStringTwice() {
		tm.addTag("20000001234");
		tm.addTag("20000001234");

		assertNotNull(tm.getTag("20000001234"));
	}

	// --------------------------------------------------------------------------
	// Get Tag tests
	// --------------------------------------------------------------------------

	/**
	 * Get existing big integer tag IDs: OK
	 */
	@Test
	public void testGetTagBigInteger() {
		assertEquals(new BigInteger("20000007106"), tm.getTag(
				new BigInteger("20000007106")).getId());
		assertEquals(new BigInteger("20000001986"), tm.getTag(
				new BigInteger("20000001986")).getId());
		assertEquals(new BigInteger("2134854321567351567231537"), tm.getTag(
				new BigInteger("2134854321567351567231537")).getId());
	}

	/**
	 * Get existing string tag IDs: OK
	 */
	@Test
	public void testGetTagString() {
		assertEquals(new BigInteger("20000007106"), tm.getTag("20000007106")
				.getId());
		assertEquals(new BigInteger("20000001986"), tm.getTag("20000001986")
				.getId());
	}

	/**
	 * Get existing string big tag ID: OK
	 */
	@Test
	public void testGetTagStringBig() {
		assertEquals(new BigInteger("2134854321567351567231537"), tm.getTag(
				"2134854321567351567231537").getId());
	}

	/**
	 * Get a bad string (characters) tag ID: NumberFormatException
	 */
	@Test
	public void testGetTagStringCharacters() {
		try {
			tm.getTag("Lorem ipsum dolor sit amet, consectetur adipiscing...");
			fail("NumberFormatException should have been thrown.");
		} catch (final NumberFormatException e) {
			// Expected NumberFormatException
		}
	}

	/**
	 * Get a bad string (float) tag ID: NumberFormatException
	 */
	@Test
	public void testGetTagStringFloat() {
		try {
			tm.getTag("3.14159265");
			fail("NumberFormatException should have been thrown.");
		} catch (final NumberFormatException e) {
			// Expected NumberFormatException
		}
	}

	/**
	 * Get an unknown big integer tag ID: OK
	 */
	@Test
	public void testGetUnknownTagBigInteger() {
		assertNull(tm.getTag(new BigInteger("123456789")));
	}

	/**
	 * Get an unknown string tag ID: OK
	 */
	@Test
	public void testGetUnknownTagString() {
		assertNull(tm.getTag("123456789"));
	}

	// --------------------------------------------------------------------------
	// Remove Tag tests
	// --------------------------------------------------------------------------

	/**
	 * Remove a big integer tag ID: OK
	 */
	@Test
	public void testRemoveTagBigInteger() {
		tm.removeTag(new BigInteger("20000007106"));
		tm.removeTag(new BigInteger("20000001986"));
		tm.removeTag(new BigInteger("2134854321567351567231537"));

		assertNull(tm.getTag("20000007106"));
		assertNull(tm.getTag("20000001986"));
		assertNull(tm.getTag("2134854321567351567231537"));
	}

	/**
	 * Remove a string tag ID: OK
	 */
	@Test
	public void testRemoveTagString() {
		tm.removeTag("20000007106");
		tm.removeTag("20000001986");

		assertNull(tm.getTag("20000007106"));
		assertNull(tm.getTag("20000001986"));
	}

	/**
	 * Remove a string big tag ID: OK
	 */
	@Test
	public void testRemoveTagStringBig() {
		tm.removeTag("2134854321567351567231537");

		assertNull(tm.getTag("2134854321567351567231537"));
	}

	/**
	 * Remove a bad string (characters) tag ID: NumberFormatException
	 */
	@Test
	public void testRemoveTagStringCharacters() {
		try {
			tm.removeTag("Lorem ipsum dolor sit amet, consectetur adipiscing.");
			fail("NumberFormatException should have been thrown.");
		} catch (final NumberFormatException e) {
			// Expected NumberFormatException
		}
	}

	/**
	 * Remove a bad string (float) tag ID: NumberFormatException
	 */
	@Test
	public void testRemoveTagStringFloat() {
		try {
			tm.removeTag("3.14159265");
			fail("NumberFormatException should have been thrown.");
		} catch (final NumberFormatException e) {
			// Expected NumberFormatException
		}
	}

	/**
	 * Remove a tag twice: OK
	 */
	@Test
	public void testRemoveTagTwice() {
		tm.removeTag("20000007106");
		tm.removeTag("20000007106");

		assertNull(tm.getTag("20000007106"));
	}

	/**
	 * Remove a tag that does not exists: OK
	 */
	@Test
	public void testRemoveTagUnknown() {
		tm.removeTag("12345678910");

		assertNull(tm.getTag("12345678910"));
	}

	// --------------------------------------------------------------------------
	// Update Tag tests
	// --------------------------------------------------------------------------

	/**
	 * Update a tag ID: OK
	 */
	@Test
	public void testUpdateTag() {
		final Tag tag = tm.getTag("20000007106");
		final Date beforeDate = new Date();

		tm.updateTag(tag.getId(), new Position(1.0, 2.5, 4.2));

		assertTrue(tag.getCurrentTimestamp().after(beforeDate)
				|| tag.getCurrentTimestamp().equals(beforeDate));
		assertEquals(1.0, tag.getCurrentPosition().x);
		assertEquals(2.5, tag.getCurrentPosition().y);
		assertEquals(4.2, tag.getCurrentPosition().z);
	}

	/**
	 * Update an unknown tag ID (without auto-detect): OK
	 */
	@Test
	public void testUpdateTagUnknown() {
		tm.disableAutoDetect();
		tm.updateTag(new BigInteger("12345678910"), new Position(1, 2, 3));

		assertNull(tm.getTag(new BigInteger("12345678910")));
	}

	/**
	 * Update an unknown tag ID (with auto-detect): OK
	 */
	@Test
	public void testUpdateTagUnknownAutoDetect() {
		final Date beforeDate = new Date();

		tm.enableAutoDetect();
		tm.updateTag(new BigInteger("12345678910"), new Position(1, 2, 3));

		final Tag tag = tm.getTag(new BigInteger("12345678910"));

		assertTrue(tag.getCurrentTimestamp().after(beforeDate)
				|| tag.getCurrentTimestamp().equals(beforeDate));
		assertEquals(1.0, tag.getCurrentPosition().x);
		assertEquals(2.0, tag.getCurrentPosition().y);
		assertEquals(3.0, tag.getCurrentPosition().z);
	}
}
