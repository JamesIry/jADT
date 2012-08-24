/*
Copyright 2012 James Iry

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/
package com.pogofish.jadt.util;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.assertEquals;

/**
 * Utility methods only used in testing
 */
public class TestUtil {
    /**
     * Create a temporary directory and return it - Java 6 JDK doesn't have this functionality
     * 
     * @throws IOException
     */
    public static File createTmpDir() throws IOException {
        final File tmp = File.createTempFile("tmp", "" + System.nanoTime());

        if (!tmp.delete()) {
            throw new IOException("Couldn't delete tmp file " + tmp.getAbsolutePath()); 
         }

        if (!tmp.mkdir()) { 
            throw new IOException("Couldn't create tmp directory " + tmp.getAbsolutePath()); 
         }

        return tmp;
    }

    public static void assertEqualsBarringFileSeparators(String expected, String actual) {
        assertEquals(sanitize(expected), sanitize(actual));
    }

    private static String sanitize(String s) {
        return s.replaceAll("\\\\", "/").replaceAll("//", "/");
    }
}
