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
package com.pogofish.jadt.parser;

import static org.junit.Assert.assertSame;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.io.Reader;
import java.util.Collections;

import org.junit.Test;

import com.pogofish.jadt.ast.DataType;
import com.pogofish.jadt.ast.Doc;
import com.pogofish.jadt.parser.DummyParser;
import com.pogofish.jadt.parser.Parser;
import com.pogofish.jadt.source.Source;
import com.pogofish.jadt.source.StringSource;


/**
 * Tests to make sure the DummyParser misbehaves properly
 * Describe your class here.
 *
 * @author jiry
 */
public class DummyParserTest {
    /**
     * Test that when given the source, and string the dummy parser gives back the testDoc 
     */
    @Test
    public void testHappy() {
        final Doc testDoc = new Doc("some source", "", Collections.<String>emptyList(),Collections.<DataType>emptyList());

        final Parser parser = new DummyParser(testDoc, "some source", "some string");
        final Doc resultDoc = parser.parse(new StringSource("some source", "some string"));
        assertSame(testDoc, resultDoc);
    }
    
    /**
     * Test that when given a different source string an exception occurs 
     */
    @Test
    public void testWrongSourceString() {
        final Doc testDoc = new Doc("some source", "", Collections.<String>emptyList(),Collections.<DataType>emptyList());

        final Parser parser = new DummyParser(testDoc, "some source", "some string");
        try {
            final Doc resultDoc = parser.parse(new StringSource("some source", "some other string"));
            fail("did not get exception, got " + resultDoc);
        } catch (RuntimeException e) {
            // yay
        }
    }
    
    /**
     * Test that when given a different source info an exception occurs 
     */
    @Test
    public void testWrongSourceInfo() {
        final Doc testDoc = new Doc("some source", "", Collections.<String>emptyList(),Collections.<DataType>emptyList());

        final Parser parser = new DummyParser(testDoc, "some source", "some string");
        try {
            final Doc resultDoc = parser.parse(new StringSource("some other source", "some string"));
            fail("did not get exception, got " + resultDoc);
        } catch (RuntimeException e) {
            // yay
        }
    }
    
    /**
     * Test that when given an additional source line an exception occurs 
     */
    @Test
    public void testWrongExtraSource() {
        final Doc testDoc = new Doc("some source", "", Collections.<String>emptyList(),Collections.<DataType>emptyList());

        final Parser parser = new DummyParser(testDoc, "some source", "some string");
        try {
            final Doc resultDoc = parser.parse(new StringSource("some source", "some string\nmore string"));
            fail("did not get exception, got " + resultDoc);
        } catch (RuntimeException e) {
            // yay
        }
    }
    
    /**
     * Test that when given an IOException occurs it is handled properly 
     */
    @Test
    public void testIOException() {
        final Doc testDoc = new Doc("some source", "", Collections.<String>emptyList(),Collections.<DataType>emptyList());

        final Parser parser = new DummyParser(testDoc, "some source", "some string");
        try {
            final Doc resultDoc = parser.parse(new Source() {

                @Override
                public Reader getReader() {
                    return new Reader() {

                        @Override
                        public int read(char[] cbuf, int off, int len) throws IOException {
                            throw new IOException("eat me");
                        }

                        @Override
                        public void close() throws IOException {
                        }
                        
                    };
                }

                @Override
                public String getSrcInfo() {
                    return "some source";
                }

                @Override
                public void close() {
                }
                
            });
            fail("did not get exception, got " + resultDoc);
        } catch (RuntimeException e) {
            // yay
        }
    }
    
}
