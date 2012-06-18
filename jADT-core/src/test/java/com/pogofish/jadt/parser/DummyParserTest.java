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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.Collections;

import org.junit.Test;

import com.pogofish.jadt.ast.DataType;
import com.pogofish.jadt.ast.Doc;
import com.pogofish.jadt.ast.ParseResult;
import com.pogofish.jadt.ast.SyntaxError;
import com.pogofish.jadt.source.Source;
import com.pogofish.jadt.source.StringSource;
import com.pogofish.jadt.util.Util;


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
        final ParseResult testResult = new ParseResult(new Doc("some source", "", Collections.<String>emptyList(),Collections.<DataType>emptyList()), Util.<SyntaxError>list());

        final Parser parser = new DummyParser(testResult, "some source", "some string");
        final ParseResult result = parser.parse(new StringSource("some source", "some string"));
        assertSame(testResult, result);
    }
    
    /**
     * Test that when given a different source string an exception occurs 
     */
    @Test
    public void testWrongSourceString() {
        final ParseResult testResult = new ParseResult(new Doc("some source", "", Collections.<String>emptyList(),Collections.<DataType>emptyList()), Util.<SyntaxError>list());

        final Parser parser = new DummyParser(testResult, "some source", "some string");
        try {
            final ParseResult result = parser.parse(new StringSource("some source", "some other string"));
            fail("did not get exception, got " + result);
        } catch (RuntimeException e) {
            // yay
        }
    }
    
    /**
     * Test that when given a different source info an exception occurs 
     */
    @Test
    public void testWrongSourceInfo() {
        final ParseResult testResult = new ParseResult(new Doc("some source", "", Collections.<String>emptyList(),Collections.<DataType>emptyList()), Util.<SyntaxError>list());

        final Parser parser = new DummyParser(testResult, "some source", "some string");
        try {
            final ParseResult result = parser.parse(new StringSource("some other source", "some string"));
            fail("did not get exception, got " + result);
        } catch (RuntimeException e) {
            // yay
        }
    }
    
    /**
     * Test that when given an additional source line an exception occurs 
     */
    @Test
    public void testWrongExtraSource() {
        final ParseResult testResult = new ParseResult(new Doc("some source", "", Collections.<String>emptyList(),Collections.<DataType>emptyList()), Util.<SyntaxError>list());

        final Parser parser = new DummyParser(testResult, "some source", "some string");
        try {
            final ParseResult result = parser.parse(new StringSource("some source", "some string\nmore string"));
            fail("did not get exception, got " + result);
        } catch (RuntimeException e) {
            // yay
        }
    }
    
    /**
     * Test that when given an IOException occurs it is handled properly 
     */
    @Test
    public void testIOException() {
        final ParseResult testResult = new ParseResult(new Doc("some source", "", Collections.<String>emptyList(),Collections.<DataType>emptyList()), Util.<SyntaxError>list());

        final Parser parser = new DummyParser(testResult, "some source", "some string");
        try {
            final ParseResult resultDoc = parser.parse(new Source() {

                @Override
                public BufferedReader createReader() {
                    return new BufferedReader(new Reader() {

                        @Override
                        public int read(char[] cbuf, int off, int len) throws IOException {
                            throw new IOException("eat me");
                        }

                        @Override
                        public void close() throws IOException {
                        }
                        
                    });
                }

                @Override
                public String getSrcInfo() {
                    return "some source";
                }
                
            });
            fail("did not get exception, got " + resultDoc);
        } catch (RuntimeException e) {
            // yay
        }
    }
    
}
