package gov.df.fap.util;

/**
 * <p>
 * DESCRIPTION:
 * <p>
 * DESCRIPTION:
 * <p>
 * CALLED BY:
 * <p>
 * CREATE DATE: Nov 27, 2014
 * <p>
 * HISTORY: 1.0
 * 
 * @version 1.0
 * @author peak.edge
 * @since jdk 1.6.0_31
 * @see
 */
public class ClassUtils {

	public static ClassLoader getDefaultClassLoader() {
		ClassLoader cl = null;
		try {
			cl = Thread.currentThread().getContextClassLoader();
		} catch (Throwable ex) {
		}
		if (cl == null) {
			cl = ClassUtils.class.getClassLoader();
			Thread.currentThread().setContextClassLoader(cl);
		} 
		return cl;
	}

	public static Class forName(String name) throws ClassNotFoundException {
		return forName(name, getDefaultClassLoader());
	}

	public static Class forName(String name, ClassLoader classLoader) throws ClassNotFoundException {
		if (null == name || name.trim().length() <= 0) {
			throw new IllegalArgumentException("Name must not be null");
		}
		if (null == classLoader) {
			classLoader = getDefaultClassLoader();
		}
		return Class.forName(name, true, classLoader);
	}
}
