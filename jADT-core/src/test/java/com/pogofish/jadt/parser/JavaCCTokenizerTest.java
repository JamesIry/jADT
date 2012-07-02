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

import org.junit.Test;

import com.pogofish.jadt.parser.javacc.BaseJavaCCParserImplTokenManager;
import com.pogofish.jadt.parser.javacc.JavaCharStream;
import com.pogofish.jadt.parser.javacc.Token;
import com.pogofish.jadt.source.Source;
import com.pogofish.jadt.source.StringSource;

import static com.pogofish.jadt.parser.javacc.BaseJavaCCParserImplConstants.*;


/**
 * Test the tokenizer separately from the parser because it's easier that way
 *
 * @author jiry
 */
public class JavaCCTokenizerTest {
   
    /**
     * Create a tokenizer that will read from the given string
     */
    private BaseJavaCCParserImplTokenManager tokenizer(String testString) {
        final Source source = new StringSource("TokenizerTest", testString);
        return new BaseJavaCCParserImplTokenManager(new JavaCharStream(source.createReader()));
    }
    
    /**
     * Comments should be invisible in the output other than separating tokens
     */
    @Test
    public void testComments() {
        final BaseJavaCCParserImplTokenManager tokenizer = tokenizer("/*\nCopyright*/hello//comment\nworld/*another comment*/oh");
        check(tokenizer, "hello", IDENTIFIER, 2);
        check(tokenizer, "world", IDENTIFIER, 3);
        check(tokenizer, "oh", IDENTIFIER, 3);
        check(tokenizer, "<EOF>", EOF, 3);
    }
    
    /**
     * Whitespace should be invisible in the output other than separating tokens
     */
    @Test
    public void testWhitespace() {
        final BaseJavaCCParserImplTokenManager tokenizer = tokenizer("hello    world   \toh");
        check(tokenizer, "hello", IDENTIFIER, 1);
        check(tokenizer, "world", IDENTIFIER, 1);
        check(tokenizer, "oh", IDENTIFIER, 1);
        check(tokenizer, "<EOF>", EOF, 1);
    }

    /**
     * End of line should be invisible in the output other than separating tokens and incrementing the line number
     */
    @Test
    public void testEol() {
        final BaseJavaCCParserImplTokenManager tokenizer = tokenizer("hello\nworld\ryeah\r\noh");
        check(tokenizer, "hello", IDENTIFIER, 1);
        check(tokenizer, "world", IDENTIFIER, 2);
        check(tokenizer, "yeah", IDENTIFIER, 3);
        check(tokenizer, "oh", IDENTIFIER, 4);
        check(tokenizer, "<EOF>", EOF, 4);
    }

    /**
     * Various classes of non-keyword identifiers
     */
    @Test
    public void testIdentifiers() {
        final BaseJavaCCParserImplTokenManager tokenizer = tokenizer("hello \u00a5123\u00a512342");
        check(tokenizer, "hello", IDENTIFIER, 1);
        check(tokenizer, "\u00a5123\u00a512342", IDENTIFIER, 1);
        check(tokenizer, "<EOF>", EOF, 1);                
    }
    
    /**
     * Various types of invalid identifier
     */
    @Test
    public void testBadIdentifiers() {
        final BaseJavaCCParserImplTokenManager tokenizer = tokenizer("42 ?");
        // bad because identifiers can't start with numbers (jADT doesn't care about numbers)
        check(tokenizer, "42", UNKNOWN, 1);
        // just bad
        check(tokenizer, "?", UNKNOWN, 1);        
    }
    
    /**
     * Test various kinds of punctuation
     */
    @Test
    public void testPunctuation() {
        final BaseJavaCCParserImplTokenManager tokenizer = tokenizer("<>=(),[]|.");
        check(tokenizer, "<", LANGLE, 1);
        check(tokenizer, ">", RANGLE, 1);
        check(tokenizer, "=", EQUALS, 1);
        check(tokenizer, "(", LPAREN, 1);
        check(tokenizer, ")", RPAREN, 1);
        check(tokenizer, ",", COMMA, 1);
        check(tokenizer, "[", LBRACKET, 1);
        check(tokenizer, "]", RBRACKET, 1);
        check(tokenizer, "|", BAR, 1);
        check(tokenizer, ".", DOT, 1);
        check(tokenizer, "<EOF>", EOF, 1);                
    }
    
    @Test
    public void testUnknown() {
        final BaseJavaCCParserImplTokenManager tokenizer1 = tokenizer("~*/~");
        check(tokenizer1, "~*", UNKNOWN, 1);
        check(tokenizer1, "/~", UNKNOWN, 1);
        check(tokenizer1, "<EOF>", EOF, 1);
        
//        final BaseJavaCCParserImplTokenManager tokenizer2 = tokenizer("/");
//        check(tokenizer2, "/", UNKNOWN, 1);
//        check(tokenizer2, "<EOF>", EOF, 1);        
    }
    
    @Test
    public void testEOF() {
        final BaseJavaCCParserImplTokenManager tokenizer = tokenizer("");
        check(tokenizer, "<EOF>", EOF, 1);  
    }

    /**
     * Test all the reserved keywords
     */
    @Test
    public void testKeywords() {
        final BaseJavaCCParserImplTokenManager tokenizer = tokenizer(
                        "import package boolean byte double char float int long short final abstract assert break case catch class const continue "
                                + "default do else enum extends finally for goto if implements instanceof interface native new private protected public return "
                                + "static strictfp super switch synchronized this throw throws transient try void volatile while");

        // keywords used by jADT
        check(tokenizer, "import", IMPORT, 1);
        check(tokenizer, "package", PACKAGE, 1);
        
        // primitive Java types
        check(tokenizer, "boolean", BOOLEAN, 1);
        check(tokenizer, "byte", BYTE, 1);
        check(tokenizer, "double", DOUBLE, 1);
        check(tokenizer, "char", CHAR, 1);
        check(tokenizer, "float", FLOAT, 1);
        check(tokenizer, "int", INT, 1);
        check(tokenizer, "long", LONG, 1);
        check(tokenizer, "short", SHORT, 1);
        check(tokenizer, "final", FINAL, 1);
        
        // Java keywords not used by jADT but reserved to prevent bad Java generation
        check(tokenizer, "abstract", JAVA_KEYWORD, 1);
        check(tokenizer, "assert", JAVA_KEYWORD, 1);
        check(tokenizer, "break", JAVA_KEYWORD, 1);
        check(tokenizer, "case", JAVA_KEYWORD, 1);
        check(tokenizer, "catch", JAVA_KEYWORD, 1);
        check(tokenizer, "class", JAVA_KEYWORD, 1);
        check(tokenizer, "const", JAVA_KEYWORD, 1);
        check(tokenizer, "continue", JAVA_KEYWORD, 1);
        check(tokenizer, "default", JAVA_KEYWORD, 1);
        check(tokenizer, "do", JAVA_KEYWORD, 1);
        check(tokenizer, "else", JAVA_KEYWORD, 1);
        check(tokenizer, "enum", JAVA_KEYWORD, 1);
        check(tokenizer, "extends", JAVA_KEYWORD, 1);
        check(tokenizer, "finally", JAVA_KEYWORD, 1);
        check(tokenizer, "for", JAVA_KEYWORD, 1);
        check(tokenizer, "goto", JAVA_KEYWORD, 1);
        check(tokenizer, "if", JAVA_KEYWORD, 1);
        check(tokenizer, "implements", JAVA_KEYWORD, 1);
        check(tokenizer, "instanceof", JAVA_KEYWORD, 1);
        check(tokenizer, "interface", JAVA_KEYWORD, 1);
        check(tokenizer, "native", JAVA_KEYWORD, 1);
        check(tokenizer, "new", JAVA_KEYWORD, 1);
        check(tokenizer, "private", JAVA_KEYWORD, 1);
        check(tokenizer, "protected", JAVA_KEYWORD, 1);
        check(tokenizer, "public", JAVA_KEYWORD, 1);
        check(tokenizer, "return", JAVA_KEYWORD, 1);
        check(tokenizer, "static", JAVA_KEYWORD, 1);
        check(tokenizer, "strictfp", JAVA_KEYWORD, 1);
        check(tokenizer, "super", JAVA_KEYWORD, 1);
        check(tokenizer, "switch", JAVA_KEYWORD, 1);
        check(tokenizer, "synchronized", JAVA_KEYWORD, 1);
        check(tokenizer, "this", JAVA_KEYWORD, 1);
        check(tokenizer, "throw", JAVA_KEYWORD, 1);
        check(tokenizer, "throws", JAVA_KEYWORD, 1);
        check(tokenizer, "transient", JAVA_KEYWORD, 1);
        check(tokenizer, "try", JAVA_KEYWORD, 1);
        check(tokenizer, "void", JAVA_KEYWORD, 1);
        check(tokenizer, "volatile", JAVA_KEYWORD, 1);
        check(tokenizer, "while", JAVA_KEYWORD, 1);
        check(tokenizer, "<EOF>", EOF, 1);
    }

    /**
     * Check that the next tokenType, symbol, and lineNo from the tokenizer are as expected
     */
    private void check(BaseJavaCCParserImplTokenManager tokenizer, String expectedSymbol, int expectedTokenType, int expectedLineNo) {
        final Token token = tokenizer.getNextToken();
        final String actualSymbol = token.kind == EOF ? "<EOF>" : token.image;
        assertEquals("Expected token type " + expectedTokenType + " with symbol " + expectedSymbol + " but got " + token.kind
                + " with symbol " + actualSymbol, expectedTokenType, token.kind);
        assertEquals("Got correct token type " + expectedTokenType + " but got wrong symbol", expectedSymbol, actualSymbol);
        assertEquals("Got correct token type and symbol but wrong line number", expectedLineNo, token.beginLine);
    }
}
