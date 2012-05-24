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
package pogofish.jadt.target;

import static org.junit.Assert.assertEquals;

import java.io.*;

import org.junit.Test;

/**
 * Test the file target.  That means writing real files to the real file system
 *
 * @author jiry
 */
public class FileTargetTest {
    /**
     * Test that when a target factory mentions a file that does not exist it gets created properly
     */
    @Test
    public void testMissingFile() throws IOException {
        final File temp = File.createTempFile("testFactory", "java");
        try {
            temp.delete();
            final FileTarget target = new FileTarget(temp.getAbsolutePath());
            try {
                target.write("hello");
            } finally {
                target.close();
            }
            
            final BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(temp), "UTF-8"));
            final String contents = reader.readLine();
            assertEquals("hello", contents);
        } finally {
            if (temp.exists()) {
                temp.delete();
            }
        }
    }
    
    /**
     * Test that when a target factory mentions a file that does exist it gets clobbered properly
     * @throws IOException
     */
    @Test
    public void testExistingFile() throws IOException {
            final File temp = File.createTempFile("testFactory", "java");
            try {
            temp.createNewFile();
            final BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(temp), "UTF-8"));
            try {
                writer.write("This nonsense better not exist when it gets read later\n");
            } finally {
                writer.close();
            }
            final FileTarget target = new FileTarget(temp.getAbsolutePath());
            try {
                target.write("hello");
            } finally {
                target.close();
            }
            
            final BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(temp), "UTF-8"));
            final String contents = reader.readLine();
            assertEquals("hello", contents);
        } finally {
            if(temp.exists()) {
                temp.delete();
            }
        }
    }
    
}
