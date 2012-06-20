package com.pogofish.jadt;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.util.Properties;

import com.pogofish.jadt.util.IOExceptionAction;


/**
 * Information about the version of jADT being used
 * 
 * @author jiry
 */
public class Version {
	private static final String MODULE_PROPERTIES = "module.properties";
	private static final String MODULE_VERSION = "module.version";

	public String getVersion() {
		return new IOExceptionAction<String>() {

			@Override
			public String doAction() throws IOException {
				final URL resource = Version.class.getClassLoader().getResource(MODULE_PROPERTIES);
				if (resource == null) {
				    throw new FileNotFoundException("Could not find internal resource " + MODULE_PROPERTIES);
				}
				final Reader reader = new InputStreamReader(
						resource.openStream(), "UTF-8");
				try {
					final Properties properties = new Properties();
					properties.load(reader);
					return properties.getProperty(MODULE_VERSION);
				} finally {
					reader.close();
				}

			}
		}.execute();
	}
}
