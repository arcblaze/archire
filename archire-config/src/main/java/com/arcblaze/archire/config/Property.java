package com.arcblaze.archire.config;

import java.io.File;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.ConversionException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.configuration.reloading.FileChangedReloadingStrategy;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Describes the individual properties that are recognized in this system.
 */
public enum Property {
	/**
	 * The configuration file used to load the system configuration properties.
	 */
	CONFIG_FILE("./conf/archire-config.properties") {
		@Override
		String getDefaultValue() {
			String configFile = System.getProperty("archire.configurationFile");
			if (!StringUtils.isBlank(configFile))
				return configFile;
			return super.getDefaultValue();
		}
	},

	/**
	 * The type of back-end database being used.
	 */
	DB_TYPE("jdbc"),

	/**
	 * The driver class name to use when creating JDBC connections to the
	 * database.
	 */
	DB_DRIVER("com.mysql.jdbc.Driver"),

	/**
	 * The JDBC connection URL to use when accessing the database.
	 */
	DB_URL("jdbc:mysql://localhost/archire"),

	/**
	 * The name of the user to use when authenticating with the database.
	 */
	DB_USERNAME("archire"),

	/**
	 * The password of the user to use when authenticating with the database (no
	 * default).
	 */
	DB_PASSWORD,

	/**
	 * Whether the server should run in development mode or not - when true,
	 * reloading of JSP pages is supported to make UI testing easier.
	 */
	SERVER_DEVELOPMENT_MODE("false"),

	/**
	 * Whether the server should run in insecure mode (HTTP) or not.
	 */
	SERVER_INSECURE_MODE("false"),

	/**
	 * The insecure (http) port on which the web server will listen.
	 */
	SERVER_PORT_INSECURE("80"),

	/**
	 * The secure (https) port on which the web server will listen.
	 */
	SERVER_PORT_SECURE("443"),

	/**
	 * The server host name published from the web server.
	 */
	SERVER_HOSTNAME("archire.arcblaze.com"),

	/**
	 * The name of the certificate alias in the key store.
	 */
	SERVER_CERTIFICATE_KEY_ALIAS("archire.arcblaze.com"),

	/**
	 * The key store file containing the server certificate.
	 */
	SERVER_KEYSTORE_FILE("conf/archire.jks"),

	/**
	 * The password to use when accessing the key store (no default).
	 */
	SERVER_KEYSTORE_PASS,

	/**
	 * The system administration email address to use for correspondence.
	 */
	EMAIL_SYSTEM_ADMIN("mday@arcblaze.com"),

	/**
	 * The server host to use when sending emails.
	 */
	EMAIL_SERVER("smtp.zoho.com"),

	/**
	 * The port to use when connecting to the mail server.
	 */
	EMAIL_SERVER_PORT("465"),

	/**
	 * Whether the email-sending capability should attempt to authenticate
	 * during connection.
	 */
	EMAIL_AUTHENTICATE_FIRST("true"),

	/**
	 * The user account to use when performing mail server authentication.
	 */
	EMAIL_AUTHENTICATE_USER("mailer@arcblaze.com"),

	/**
	 * The password to use when performing mail server authentication (no
	 * default).
	 */
	EMAIL_AUTHENTICATE_PASSWORD(),

	/**
	 * Whether the connection to the mail server should use an encrypted
	 * connection.
	 */
	EMAIL_USE_SSL("true"),

	;

	/** This will be used to log messages. */
	private final static Logger log = LoggerFactory.getLogger(Property.class);

	/** Holds all of the loaded configuration properties. */
	private static Configuration config = load();

	/**
	 * The default value to be returned when the requested configuration
	 * property is not available.
	 */
	private final String defaultValue;

	/**
	 * The default constructor is used to create properties that do not have a
	 * default value.
	 */
	private Property() {
		this.defaultValue = null;
	}

	/**
	 * @param defaultValue
	 *            the default value to use for the configuration property
	 */
	private Property(String defaultValue) {
		this.defaultValue = defaultValue;
	}

	/**
	 * @param value
	 *            the new value to use for this configuration property
	 */
	public void set(String value) {
		if (value == null)
			config.clearProperty(getKey());
		else
			config.setProperty(getKey(), value);
	}

	/**
	 * @return the configuration property key name used when accessing this
	 *         property from a property source
	 */
	private String getKey() {
		return this.name().toLowerCase().replaceAll("_", ".");
	}

	/**
	 * @return the default value for this configuration property
	 */
	String getDefaultValue() {
		return this.defaultValue;
	}

	/**
	 * @return the value for this property as a string, potentially the default
	 *         value if no specific value was configured
	 */
	public String getString() {
		return config.getString(getKey(), getDefaultValue());
	}

	/**
	 * @return the value for this property as an int, potentially the default
	 *         value if no specific value was configured
	 * 
	 * @throws ConversionException
	 *             if the configured value or the default value for the property
	 *             is not an integer
	 */
	public int getInt() {
		try {
			return config.getInt(getKey(), Integer.parseInt(getDefaultValue()));
		} catch (NumberFormatException badNumber) {
			throw new ConversionException("Not an integer value.", badNumber);
		}
	}

	/**
	 * @return the value for this property as a long, potentially the default
	 *         value if no specific value was configured
	 * 
	 * @throws ConversionException
	 *             if the configured value or the default value for the property
	 *             is not a long
	 */
	public long getLong() {
		try {
			return config.getLong(getKey(), Long.parseLong(getDefaultValue()));
		} catch (NumberFormatException badNumber) {
			throw new ConversionException("Not a long value.", badNumber);
		}
	}

	/**
	 * @return the value for this property as a boolean, potentially the default
	 *         value if no specific value was configured
	 * 
	 * @throws ConversionException
	 *             if the configured value or the default value for the property
	 *             is not a boolean
	 */
	public boolean getBoolean() {
		return config.getBoolean(getKey(),
				Boolean.parseBoolean(getDefaultValue()));
	}

	/**
	 * @param fileName
	 *            the file path containing the configuration information
	 * 
	 * @return the {@link Configuration} for the specified configuration file
	 */
	private static Configuration load() {
		File configFile = new File(CONFIG_FILE.getDefaultValue());

		if (configFile.exists()) {
			log.info("Loading configuration from "
					+ configFile.getAbsolutePath());
			try {
				PropertiesConfiguration conf = new PropertiesConfiguration(
						configFile);
				conf.setDelimiterParsingDisabled(true);
				conf.setReloadingStrategy(new FileChangedReloadingStrategy());
				return conf;
			} catch (ConfigurationException badConfig) {
				throw new RuntimeException(
						"Failed to load system configuration from "
								+ configFile.getAbsolutePath(), badConfig);
			}
		}

		throw new RuntimeException(
				"Failed to load system configuration from non-existent file: "
						+ configFile.getAbsolutePath());
	}
}
