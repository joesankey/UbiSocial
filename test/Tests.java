package test;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.apache.log4j.PropertyConfigurator;

public abstract class Tests extends TestCase {
	// --------------------------------------------------------------------------
	// Constants
	// --------------------------------------------------------------------------

	/**
	 * If true, then all tests will be executed. Otherwise, only the fast tests
	 * will be executed.
	 */
	public static final boolean ALL_TESTS = false;

	/**
	 * The IP address of the remote host used for the tests.
	 */
	public static final String UBI_SERVER_ADDRESS = "192.168.33.39";

	/**
	 * The port of the remote host used for the tests.
	 */
	public static final int UBI_SERVER_PORT = 12000;

	/**
	 * The host name of the UbiQuSe server.
	 */
	public static final String UBIQUSE_SERVER_ADDRESS = "localhost";

	/**
	 * The host port of the UbiQuSe server.
	 */
	public static final int UBIQUSE_SERVER_PORT = 4242;

	// --------------------------------------------------------------------------
	// Test suite
	// --------------------------------------------------------------------------

	/**
	 * Check whether the caught exception by InvocationTargetException is the
	 * one expected or not.
	 * 
	 * @param targetException
	 *            the target exception.
	 * @param expected
	 *            the expected exception class.
	 * @return true if the excepted exception is the one that have been thrown.
	 */
	public static boolean checkThrownException(
			InvocationTargetException targetException,
			Class<? extends Exception> expected) {
		return (targetException.getTargetException().getClass() == expected);
	}

	// --------------------------------------------------------------------------
	// Static methods
	// --------------------------------------------------------------------------

	/**
	 * Get a private field of a given object.
	 * 
	 * Every error (null pointer exception, unknown field, ...) prints the stack
	 * trace on the output.
	 * 
	 * @param target
	 *            the targeted object.
	 * @param field
	 *            the private field name.
	 * @return the value of the specified field as an instance of Object.
	 */
	public static Object getPrivateField(Object target, String field) {
		try {
			// Get private field
			final Field f = target.getClass().getDeclaredField(field);

			// Set it accessible
			f.setAccessible(true);

			// Extract it
			return f.get(target);
		} catch (final Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Invoke a private method of a given object with the given parameters.
	 * 
	 * @param target
	 *            the targeted object.
	 * @param method
	 *            the private method name.
	 * @param args
	 *            the list of arguments of the method.
	 * @return the result of the method as an instance of Object, or null if the
	 *         method is void.
	 * 
	 * @throws NoSuchMethodException
	 * @throws SecurityException
	 * @throws InvocationTargetException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 */
	public static Object invokePrivateMethod(Object target, String method,
			Object[] args) throws SecurityException, NoSuchMethodException,
			IllegalArgumentException, IllegalAccessException,
			InvocationTargetException {
		// Extract meta data (Object classes)
		final Class<?>[] argClasses = new Class<?>[args.length];
		for (int i = 0; i < args.length; i++) {
			argClasses[i] = args[i].getClass();
		}

		// Get private method
		final Method m = target.getClass()
				.getDeclaredMethod(method, argClasses);

		// Set it accessible
		m.setAccessible(true);

		// Invoke it and return the result
		return m.invoke(target, args);
	}

	static public junit.framework.Test suite() {
		final TestSuite suite = new TestSuite();

		// Load test log configuration
		PropertyConfigurator.configure("./log4j-config-test.txt");

		// Execute all tests
		if (ALL_TESTS) {
			suite.addTestSuite(UbiSenseConnectionTests.class);
			suite.addTestSuite(UbiSenseTagManagementTests.class);
			suite.addTestSuite(DataWrapperParseStreamTests.class);
		}
		// Execute only fast tests
		else {
			suite.addTest(UbiSenseConnectionTests.suite());
			suite.addTest(UbiSenseTagManagementTests.suite());
			suite.addTest(DataWrapperParseStreamTests.suite());
		}

		return suite;
	}
}
