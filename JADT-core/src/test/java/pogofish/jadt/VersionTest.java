package pogofish.jadt;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Test the Version thingy does its thingy
 * 
 * @author jiry
 *
 */
public class VersionTest {
	@Test
	public void test() {
		try {
			final String version = new Version().getVersion();
			assertTrue("bad version information " + version, version.equals("${pom.version}") || version.matches("[0-9]+\\.[0-9]+\\.[0-9]+.*"));
		} catch (NullPointerException e) {			
			assertNotNull("Got a null pointer exception.  If you are running in an IDE the most likely cause is that the main/src/resrouces directory isn't a source path.", e);
		}
	}
}
