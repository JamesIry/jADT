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


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.fail;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.List;

import org.junit.Test;

import com.pogofish.jadt.ast.ASTConstants;
import com.pogofish.jadt.ast.DataType;
import com.pogofish.jadt.ast.Doc;
import com.pogofish.jadt.ast.Imprt;
import com.pogofish.jadt.ast.ParseResult;
import com.pogofish.jadt.ast.SyntaxError;
import com.pogofish.jadt.source.Source;
import com.pogofish.jadt.source.StringSource;
import com.pogofish.jadt.util.Util;

/**
 * Test the functionality in the standard parser that isn't covered in
 * JavaCCParserImplTest r
 * 
 * @author jiry
 */
public class ParserTest {
    /**
     * Test handling of exceptions that occur during reader close
     */
    @Test
    public void testErrorClosing() {
        final IOException thrown = new IOException("oh yeah");
        final Source source = new Source() {
            
            @Override
            public String getSrcInfo() {
                return "whatever";
            }
            
            @Override
            public BufferedReader createReader() {
                return new BufferedReader(new Reader() { 
                    @Override
                    public int read(char[] cbuf, int off, int len) throws IOException {
                        throw new RuntimeException("This should not have been called");
                    }
                    
                    @Override
                    public void close() throws IOException {
                        throw thrown;
                    }
                });
            }
        };
        
        final Parser parser = new StandardParser(new ParserImplFactory() {
            
            @Override
            public ParserImpl create(String srcInfo, Reader reader) {
                return new BaseTestParserImpl() {
                    
                    @Override
                    public Doc doc() throws Exception {
                        return Doc._Doc("whatever", ASTConstants.EMPTY_PKG, Util.<Imprt>list(), Util.<DataType>list());
                    }

                    @Override
                    public List<SyntaxError> errors() {
                        return Util.<SyntaxError>list();
                    }
                };
            }
        });
        
        try {
            final ParseResult parseResult = parser.parse(source);
            fail("Did not get exception, got " + parseResult );
            
        } catch (RuntimeException caught) {
            assertEquals("Got wrong exception", thrown, caught.getCause());
        }
    }
    
    /**
     * Test handling of exceptions that occur during parsing
     */
    @Test
    public void testExceptionHandling() {
        final Throwable thrown1 = new RuntimeException("oh yeah");
        try {
            final Parser parser = new StandardParser(new ThrowingParserImplFactory(thrown1));
            final ParseResult result = parser.parse(new StringSource("whatever", "whatever"));
            fail("did not get exception, got " + result);
        } catch (RuntimeException caught) {
            assertSame("got wrong exception", thrown1, caught);
        }
        
        final Throwable thrown2 = new Error("oh yeah");
        try {
            final Parser parser = new StandardParser(new ThrowingParserImplFactory(thrown2));
            final ParseResult result = parser.parse(new StringSource("whatever", "whatever"));
            fail("did not get exception, got " + result);
        } catch (Error caught) {
            assertSame("got wrong exception", thrown2, caught);
        }
        
        final Throwable thrown3 = new Exception("oh yeah");
        try {
            final Parser parser = new StandardParser(new ThrowingParserImplFactory(thrown3));
            final ParseResult result = parser.parse(new StringSource("whatever", "whatever"));
            fail("did not get exception, got " + result);
        } catch (RuntimeException caught) {
            assertSame("got wrong exception", thrown3, caught.getCause());
        }
    }

    
    private final static class ThrowingParserImplFactory implements ParserImplFactory {
        final Throwable exception;

        public ThrowingParserImplFactory(Throwable exception) {
            super();
            this.exception = exception;
        }

        @Override
        public ParserImpl create(String srcInfo, Reader reader) {
            return new BaseTestParserImpl() {
                @Override
                public Doc doc() throws Exception {
                    try {
                        throw exception;
                    } catch (RuntimeException e) {
                        throw e;
                    } catch (Error e) {
                        throw e;
                    } catch (Exception e) {
                        throw e;
                    } catch (Throwable e) {
                        throw new RuntimeException(e);
                    }
                }
            };
        }
    }

}
