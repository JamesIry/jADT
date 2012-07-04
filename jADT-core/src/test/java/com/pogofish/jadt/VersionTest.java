package com.pogofish.jadt;

import static org.junit.Assert.*;

import java.io.FileNotFoundException;


import org.junit.Test;


/**
 * Test the Version thingy does its thingy
 * 
 * @author jiry
 *
 */
public class VersionTest {
	@Test
	public void testHappy() {
		final String version = new Version().getVersion();
		assertTrue("bad version information '" + version + "'. Did you set the resource directory as a source directory?", version.equals("${pom.version}") || version.matches("[0-9]+\\.[0-9]+\\.[0-9]+.*"));
	}
	
	@Test
	public void testMissingFile() throws Throwable {
	    try {
    	    final Version version = new Version();
    	    version.MODULE_PROPERTIES = "foo.bar";
    	    final String result = version.getVersion();
    	    fail("did not get exception, got " + result);
	    } catch (RuntimeException e) {
	        try {
	            throw(e.getCause());
	        } catch (FileNotFoundException e2) {
	            // what's expected, yay!
	        }
	    }
	}
	
    @Test
    public void testMissingProperty() throws Throwable {
        final Version version = new Version();
        version.MODULE_VERSION = "foo.bar";
        final String result = version.getVersion();
        assertEquals("unknown version, could not find property foo.bar in module.properties", result);
    }
}
