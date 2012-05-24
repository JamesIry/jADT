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
package pogofish.jadt.source;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.*;

import org.junit.Test;

/**
 * Make sure FileSourceFactory does its job properly
 *
 * @author jiry
 */
public class FileSourceFactoryTest {
    /**
     * On a valid file should get the data from the file.  This test must write to the file system
     * @throws IOException
     */
    @Test
    public void testValidFile() throws IOException {
        final File temp = File.createTempFile("testFactory", "java");
        try {
            final BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(temp), "UTF-8"));
            try {
                writer.write("hello");
            } finally {
                writer.close();
            }
            
            final SourceFactory factory = new FileSourceFactory();
            final Source source = factory.createSource(temp.getAbsolutePath());
            try {
                final BufferedReader reader = new BufferedReader(source.getReader());
                assertEquals("hello", reader.readLine());
                assertEquals(temp.getAbsolutePath(), source.getSrcInfo());
            } finally {
                source.close();
            }
        } finally {
            temp.delete();
        }
    }

    /**
     * This tst attempts to open a file that does not exist which should result in an exception.  It should not create any files
     */
    @Test
    public void testInValidFile() throws IOException {
        final File temp = File.createTempFile("testFactory", "java");
        temp.delete();
        
        final SourceFactory factory = new FileSourceFactory();
        try {
            final Source source = factory.createSource(temp.getAbsolutePath());
            source.close();
            fail("Did not get an exception");
        } catch (RuntimeException e) {
            // yay
        }
    }
    
}
