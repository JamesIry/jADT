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

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;

import org.junit.Test;

import com.pogofish.jadt.parser.TokenType;
import com.pogofish.jadt.parser.Tokenizer;
import com.pogofish.jadt.source.Source;
import com.pogofish.jadt.source.StringSource;


/**
 * Test the tokenizer separately from the parser because it's easier that way
 *
 * @author jiry
 */
public class TokenizerTest {

    /**
     * Test that an IOException in the tokenizer is converted to a RuntimeException
     */
    @Test
    public void testIOException() {
        // a dummy reader that throws an exception on any action
        final BufferedReader reader = new BufferedReader(new Reader() {
            @Override
            public int read(char[] cbuf, int off, int len) throws IOException {
                throw new IOException("TestException");
            }

            @Override
            public void close() throws IOException {
                throw new IOException("TestException");
            }
        });
        
        // a dummy source that uses that reader
        final Source source = new Source() {            
            @Override
            public String getSrcInfo() {
                return "TokenizerTest";
            }
            
            @Override
            public BufferedReader createReader() {
                return reader;
            }
        };
        
        final ITokenizer tokenizer = new Tokenizer(source.getSrcInfo(), source.createReader());

        try {
            // should throw
            final TokenType result = tokenizer.getTokenType();
            fail("Did not get an exception from tokenizer, got " + result);
        } catch (RuntimeException e) {
            assertTrue("Got wrong exception " + e, e.getCause() instanceof IOException);
        }
    }
    
    /**
     * Create a tokenizer that will read from the given string
     */
    private ITokenizer tokenizer(String testString) {
        final Source source = new StringSource("TokenizerTest", testString);
        return new Tokenizer(source.getSrcInfo(), source.createReader());
    }
    
    /**
     * Comments should be invisible in the output other than separating tokens
     */
    @Test
    public void testComments() {
        final ITokenizer tokenizer = tokenizer("/*\nCopyright*/hello//comment\nworld/*another comment*/oh");
        check(tokenizer, "hello", TokenType.IDENTIFIER, 2);
        check(tokenizer, "world", TokenType.IDENTIFIER, 3);
        check(tokenizer, "oh", TokenType.IDENTIFIER, 3);
        check(tokenizer, "<EOF>", TokenType.EOF, 3);
    }
    
    /**
     * Whitespace should be invisible in the output other than separating tokens
     */
    @Test
    public void testWhitespace() {
        final ITokenizer tokenizer = tokenizer("hello    world   \toh");
        check(tokenizer, "hello", TokenType.IDENTIFIER, 1);
        check(tokenizer, "world", TokenType.IDENTIFIER, 1);
        check(tokenizer, "oh", TokenType.IDENTIFIER, 1);
        check(tokenizer, "<EOF>", TokenType.EOF, 1);
    }

    /**
     * End of line should be invisible in the output other than separating tokens and incrementing the line number
     */
    @Test
    public void testEol() {
        final ITokenizer tokenizer = tokenizer("hello\nworld\ryeah\r\noh");
        check(tokenizer, "hello", TokenType.IDENTIFIER, 1);
        check(tokenizer, "world", TokenType.IDENTIFIER, 2);
        check(tokenizer, "yeah", TokenType.IDENTIFIER, 3);
        check(tokenizer, "oh", TokenType.IDENTIFIER, 4);
        check(tokenizer, "<EOF>", TokenType.EOF, 4);
    }

    /**
     * Various classes of non-keyword identifiers
     */
    @Test
    public void testIdentifiers() {
        final ITokenizer tokenizer = tokenizer("hello \u00a5123\u00a512342");
        check(tokenizer, "hello", TokenType.IDENTIFIER, 1);
        check(tokenizer, "\u00a5123\u00a512342", TokenType.IDENTIFIER, 1);
        check(tokenizer, "<EOF>", TokenType.EOF, 1);                
    }
    
    /**
     * Various types of invalid identifier
     */
    @Test
    public void testBadIdentifiers() {
        final ITokenizer tokenizer = tokenizer("42 ?");
        // bad because identifiers can't start with numbers (jADT doesn't care about numbers)
        check(tokenizer, "42", TokenType.UNKNOWN, 1);
        // just bad
        check(tokenizer, "?", TokenType.UNKNOWN, 1);        
    }
    
    /**
     * Test various kinds of punctuation
     */
    @Test
    public void testPunctuation() {
        final ITokenizer tokenizer = tokenizer("<>=(),[]|.~*/~");
        check(tokenizer, "<", TokenType.LANGLE, 1);
        check(tokenizer, ">", TokenType.RANGLE, 1);
        check(tokenizer, "=", TokenType.EQUALS, 1);
        check(tokenizer, "(", TokenType.LPAREN, 1);
        check(tokenizer, ")", TokenType.RPAREN, 1);
        check(tokenizer, ",", TokenType.COMMA, 1);
        check(tokenizer, "[", TokenType.LBRACKET, 1);
        check(tokenizer, "]", TokenType.RBRACKET, 1);
        check(tokenizer, "|", TokenType.BAR, 1);
        check(tokenizer, ".", TokenType.DOT, 1);
        // this one is tested to provide coverage of a default case in the Tokenizer
        check(tokenizer, "~*", TokenType.UNKNOWN, 1);
        check(tokenizer, "/~", TokenType.UNKNOWN, 1);
        check(tokenizer, "<EOF>", TokenType.EOF, 1);        
    }
    
    @Test
    public void testEOF() {
        final ITokenizer tokenizer = tokenizer("");
        check(tokenizer, "<EOF>", TokenType.EOF, 1);  
    }

    /**
     * Test all the reserved keywords
     */
    @Test
    public void testKeywords() {
        final ITokenizer tokenizer = tokenizer(
                        "import package boolean byte double char float int long short final abstract assert break case catch class const continue "
                                + "default do else enum extends finally for goto if implements instanceof interface native new private protected public return "
                                + "static strictfp super switch synchronized this throw throws transient try void volatile while");

        // keywords used by jADT
        check(tokenizer, "import", TokenType.IMPORT, 1);
        check(tokenizer, "package", TokenType.PACKAGE, 1);
        
        // primitive Java types
        check(tokenizer, "boolean", TokenType.BOOLEAN, 1);
        check(tokenizer, "byte", TokenType.BYTE, 1);
        check(tokenizer, "double", TokenType.DOUBLE, 1);
        check(tokenizer, "char", TokenType.CHAR, 1);
        check(tokenizer, "float", TokenType.FLOAT, 1);
        check(tokenizer, "int", TokenType.INT, 1);
        check(tokenizer, "long", TokenType.LONG, 1);
        check(tokenizer, "short", TokenType.SHORT, 1);
        check(tokenizer, "final", TokenType.FINAL, 1);
        
        // Java keywords not used by jADT but reserved to prevent bad Java generation
        check(tokenizer, "abstract", TokenType.JAVA_KEYWORD, 1);
        check(tokenizer, "assert", TokenType.JAVA_KEYWORD, 1);
        check(tokenizer, "break", TokenType.JAVA_KEYWORD, 1);
        check(tokenizer, "case", TokenType.JAVA_KEYWORD, 1);
        check(tokenizer, "catch", TokenType.JAVA_KEYWORD, 1);
        check(tokenizer, "class", TokenType.JAVA_KEYWORD, 1);
        check(tokenizer, "const", TokenType.JAVA_KEYWORD, 1);
        check(tokenizer, "continue", TokenType.JAVA_KEYWORD, 1);
        check(tokenizer, "default", TokenType.JAVA_KEYWORD, 1);
        check(tokenizer, "do", TokenType.JAVA_KEYWORD, 1);
        check(tokenizer, "else", TokenType.JAVA_KEYWORD, 1);
        check(tokenizer, "enum", TokenType.JAVA_KEYWORD, 1);
        check(tokenizer, "extends", TokenType.JAVA_KEYWORD, 1);
        check(tokenizer, "finally", TokenType.JAVA_KEYWORD, 1);
        check(tokenizer, "for", TokenType.JAVA_KEYWORD, 1);
        check(tokenizer, "goto", TokenType.JAVA_KEYWORD, 1);
        check(tokenizer, "if", TokenType.JAVA_KEYWORD, 1);
        check(tokenizer, "implements", TokenType.JAVA_KEYWORD, 1);
        check(tokenizer, "instanceof", TokenType.JAVA_KEYWORD, 1);
        check(tokenizer, "interface", TokenType.JAVA_KEYWORD, 1);
        check(tokenizer, "native", TokenType.JAVA_KEYWORD, 1);
        check(tokenizer, "new", TokenType.JAVA_KEYWORD, 1);
        check(tokenizer, "private", TokenType.JAVA_KEYWORD, 1);
        check(tokenizer, "protected", TokenType.JAVA_KEYWORD, 1);
        check(tokenizer, "public", TokenType.JAVA_KEYWORD, 1);
        check(tokenizer, "return", TokenType.JAVA_KEYWORD, 1);
        check(tokenizer, "static", TokenType.JAVA_KEYWORD, 1);
        check(tokenizer, "strictfp", TokenType.JAVA_KEYWORD, 1);
        check(tokenizer, "super", TokenType.JAVA_KEYWORD, 1);
        check(tokenizer, "switch", TokenType.JAVA_KEYWORD, 1);
        check(tokenizer, "synchronized", TokenType.JAVA_KEYWORD, 1);
        check(tokenizer, "this", TokenType.JAVA_KEYWORD, 1);
        check(tokenizer, "throw", TokenType.JAVA_KEYWORD, 1);
        check(tokenizer, "throws", TokenType.JAVA_KEYWORD, 1);
        check(tokenizer, "transient", TokenType.JAVA_KEYWORD, 1);
        check(tokenizer, "try", TokenType.JAVA_KEYWORD, 1);
        check(tokenizer, "void", TokenType.JAVA_KEYWORD, 1);
        check(tokenizer, "volatile", TokenType.JAVA_KEYWORD, 1);
        check(tokenizer, "while", TokenType.JAVA_KEYWORD, 1);
        check(tokenizer, "<EOF>", TokenType.EOF, 1);
    }

    /**
     * Check that the next tokenType, symbol, and lineNo from the tokenizer are as expected
     */
    private void check(ITokenizer tokenizer, String symbol, TokenType expectedTokenType, int lineNo) {
        final TokenType actualTokenType = tokenizer.getTokenType();
        assertEquals("Expected token type " + expectedTokenType + " with symbol " + symbol + " but got " + actualTokenType
                + " with symbol " + tokenizer.lastSymbol(), expectedTokenType, actualTokenType);
        assertEquals("Got correct token type " + expectedTokenType + " but got wrong symbol", symbol, tokenizer.lastSymbol());
        assertEquals("Got correct token type and symbol but wrong line number", lineNo, tokenizer.lineno());
    }
}
