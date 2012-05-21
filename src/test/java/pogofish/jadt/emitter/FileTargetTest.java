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
package pogofish.jadt.emitter;

import static org.junit.Assert.assertEquals;

import java.io.*;

import org.junit.Test;


public class FileTargetTest {
    @Test
    public void testMissingFile() throws IOException {
        final File temp = File.createTempFile("testFactory", "java");
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
    }
    
    @Test
    public void testExistingFile() throws IOException {
        final File temp = File.createTempFile("testFactory", "java");
        temp.createNewFile();
        final FileTarget target = new FileTarget(temp.getAbsolutePath());
        try {
            target.write("hello");
        } finally {
            target.close();
        }
        
        final BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(temp), "UTF-8"));
        final String contents = reader.readLine();
        assertEquals("hello", contents);
    }
    
}
