/**
 * 
 */
package tn.esprit.voyage.demo.security.utils;

import java.util.Enumeration;
import java.util.Properties;
import java.util.ResourceBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConfigUtils {

	protected static Logger logger = LoggerFactory.getLogger(ConfigUtils.class);

	private final static String PROPERTIES_FILE_PATH = "applicationsRessources";

	public static Properties readPropertiesFile(String bundleFile) {
		ResourceBundle bundle = ResourceBundle.getBundle(bundleFile);
		Properties properties = new Properties();
		String key;
		for (Enumeration<String> keys = bundle.getKeys(); keys.hasMoreElements();) {
			key = (String) keys.nextElement();
			properties.setProperty(key, bundle.getString(key));
		}
		return properties;

	}

	public static String getProperty(String proporty) {
		ResourceBundle bundle = ResourceBundle.getBundle(PROPERTIES_FILE_PATH);
		return bundle.getString(proporty);

	}

}
