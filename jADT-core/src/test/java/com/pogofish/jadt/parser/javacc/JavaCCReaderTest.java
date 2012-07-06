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
package com.pogofish.jadt.parser.javacc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.fail;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

import org.junit.Test;

/**
 * Test the JavaCCReader
 *
 * @author jiry
 */
public class JavaCCReaderTest {
    @Test
    public void testRead() throws Exception {
        final StringReader stringReader = new StringReader("hello");
        final JavaCCReader javaccReader = new JavaCCReader(stringReader);
        final BufferedReader reader = new BufferedReader(javaccReader);
        assertEquals("hello", reader.readLine());
    }
    
    @Test
    public void testIOExceptionBeforeClose() throws Exception {
        final IOException thrown = new IOException("mmm, baby");
        final Reader throwingReader = createThrowingReader(thrown);
        final JavaCCReader reader = new JavaCCReader(throwingReader);
        try {
            reader.read();
            fail("didn't get an exception");
        } catch (RuntimeException caught) {
            assertSame(thrown,  caught.getCause());
        }
    }

    @Test
    public void testIOExceptionAfterClose() throws Exception {
        final IOException thrown = new IOException("mmm, baby");
        final Reader throwingReader = createThrowingReader(thrown);
        final JavaCCReader reader = new JavaCCReader(throwingReader);
        reader.close();
        try {
            reader.read();
            fail("didn't get an exception");
        } catch (IOException caught) {
            assertSame(thrown,  caught);
        }
    }
    
    private Reader createThrowingReader(final IOException thrown) {
        return new Reader() {
            
            @Override
            public int read(char[] cbuf, int off, int len) throws IOException {
                throw thrown;
            }
            
            @Override
            public void close() throws IOException {
            }
        };
    }
}
