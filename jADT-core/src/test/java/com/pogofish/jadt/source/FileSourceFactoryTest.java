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
package com.pogofish.jadt.source;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.junit.Test;

import com.pogofish.jadt.source.FileSourceFactory;
import com.pogofish.jadt.source.Source;
import com.pogofish.jadt.source.SourceFactory;
import com.pogofish.jadt.util.Util;


/**
 * Make sure FileSourceFactory does its job properly
 *
 * @author jiry
 */
public class FileSourceFactoryTest {
    /**
     * A comparator that gives a predictable sort on sources based on their name
     * 
     * @author jiry
     */
    private static final class SourceComparator implements Comparator<Source> {
        @Override
        public int compare(Source source1, Source source2) {
            return source1.getSrcInfo().compareTo(source2.getSrcInfo());
        }
    }

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
            final List<? extends Source> sources = factory.createSources(temp.getAbsolutePath());
            assertEquals(1, sources.size());
            final Source source = sources.get(0);
            final BufferedReader reader = source.createReader();
            try {
                assertEquals("hello", reader.readLine());
                assertEquals(temp.getAbsolutePath(), source.getSrcInfo());
            } finally {
                reader.close();
            }
        } finally {
            temp.delete();
        }
    }

    /**
     * This test attempts to open a file that does not exist which should result in an exception.  It should not create any files
     */
    @Test
    public void testInValidFile() throws IOException {
        final File temp = File.createTempFile("testFactory", "java");
        temp.delete();
        
        final SourceFactory factory = new FileSourceFactory();
        final List<? extends Source> sources = factory.createSources(temp.getAbsolutePath());
        final Source source = sources.get(0);
        try {
            final BufferedReader reader = source.createReader();
            try {
                assertEquals("hello", reader.readLine());
                assertEquals(temp.getAbsolutePath(), source.getSrcInfo());
            } finally {
                reader.close();
            }
            fail("Did not get an exception");
        } catch (RuntimeException e) {
            // yay
        }
    }
    
    /**
     * Test to see if FileSourceFactoryTest works with a directory
     */
    @Test
    public void testDir() throws IOException {
        final File tempDir = Util.createTmpDir();
        try {
            final List<File> tempFiles = new ArrayList<File>();
            try {
                for (int i=0; i<5; i++) {
                    tempFiles.add(createTempFile(tempDir, i));
                }
                
                final SourceFactory factory = new FileSourceFactory();
                final List<? extends Source> sources = factory.createSources(tempDir.getAbsolutePath());
                assertEquals(5, sources.size());
                
                // sort them so that we look at them in 0...4 order
                Collections.sort(sources, new SourceComparator());
                
                for (int i=0; i<5; i++) {
                    checkSource(i, tempDir, sources.get(i));
                }
               
            } finally {
                for (File tempFile : tempFiles) {
                    tempFile.delete();
                }
            }
        } finally {
            tempDir.delete();
        }      
    }

    private void checkSource(int i, File parent, Source source) throws IOException {
        final BufferedReader reader = source.createReader();
        try {
            assertEquals(parent.getAbsolutePath() + "/" + i + ".jadt", source.getSrcInfo());
            final String line = reader.readLine();
            assertEquals("hello" + i, line);
        } finally {
            reader.close();
        }        
    }

    private File createTempFile(File parentDir, int i) {
        final File tempFile = new File(parentDir, i + ".jadt");
        try {
            final BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(tempFile), "UTF-8"));
            try {
                writer.write("hello" + i);
            } finally {
                writer.close();
            }
            return tempFile;
        } catch (Throwable t) {
            tempFile.delete();
            throw new RuntimeException(t);
        }
    }
}
