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

import java.io.Reader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import com.pogofish.jadt.ast.SyntaxError;
import com.pogofish.jadt.parser.ParserImpl;
import com.pogofish.jadt.util.Util;

/**
 * Sub class for the JavaCC generated parser. This class exists just so most
 * Java code can live in a .java file instead of a much harder to edit .jj file
 * 
 * @author jiry
 */
public class JavaCCParserImpl extends BaseJavaCCParserImpl implements ParserImpl {
    /**
     * Whether this parser is currently recovering from a problem.  While
     * recovering errors are not recorded
     */
    private boolean recovering = false;

    /**
     * Information about the source that created this parser
     */
    private final String srcInfo;;

    /**
     * Errors found while using this parser
     */
    private final List<SyntaxError> errors = new ArrayList<SyntaxError>();

    /**
     * The next id used to create a fake identifier
     */
    private int nextId = 1;

    /**
     * Set of punctuation tokens. Useful for syntax error recovery
     */
    private static final Set<Integer> punctuation = Collections
            .unmodifiableSet(Util.set(LANGLE, RANGLE, EQUALS, LPAREN, RPAREN,
                    COMMA, BAR, LBRACKET, RBRACKET, DOT, EOF));

    /**
     * Create a parser implementation based on the given srcInfo and input reader
     */
    public JavaCCParserImpl(String srcInfo, Reader stream) {
        super(new JavaCCReader(stream));
        this.srcInfo = srcInfo;
    }


    /**
     * Check to see if the next token is a punctuation character
     */
    private boolean peekPunctuation() {
        return (punctuation.contains(lookahead(1).kind));
    }    
    
    /**
     * Called by the parser whenever a required identifier is missing.
     * 
     * It always records a syntax error and then creates a fake identifier.
     * 
     * If the next token token isn't punctuation then get it and turn it into
     * a fake identifier. Otherwise create a new fake identifier and leave the
     * punctuation for somebody else to process
     */
    @Override
    String badIdentifier(String expected) {
        error(expected);

        final String id = "@" + (nextId++);
        if (!peekPunctuation()) {
            final Token token = getNextToken();
            return "BAD_IDENTIFIER" + "_" + friendlyName(token) + id;
        } else {
            return "NO_IDENTIFIER" + id;
        }
    }


    @Override
    /**
     * Called by the parser when ever a good token is recognized by normal parser
     * activity.
     */
    void recovered() {
        recovering = false;
    }


    @Override
    public List<SyntaxError> errors() {
        return errors;
    }

    @Override
    public String getSrcInfo() {
        return srcInfo;
    }

    /**
     * If not currently recovering, adds an error to the errors list and sets
     * recovering to true. Actual is assumed to be the last symbol from the
     * tokenizer.
     * 
     * @param expected
     *            the kind of thing expected
     */
    @Override
    void error(String expected) {
        error(expected, friendlyName(lookahead(1)));
    }

    /**
     * If not currently recovering, adds an error to the errors list and sets
     * recovering to true
     * 
     * @param expected
     *            the kind of thing expected
     */
    private void error(String expected, String actual) {
        if (!recovering) {
            recovering = true;
            final String outputString = "<EOF>".equals(actual) ? actual : "'"
                    + actual + "'";
            errors.add(SyntaxError._UnexpectedToken(expected, outputString,
                    lookahead(1).beginLine));
        }
    }

    /**
     * Turn a token into a user readable name
     */
    private String friendlyName(Token token) {
        return token.kind == EOF ? "<EOF>" : token.image;
    }

    /**
     * look ahead n tokens. 0 is the current token, 1 is the next token, 2 the
     * one after that, etc
     * 
     * @param n
     * @return
     */
    private Token lookahead(int n) {
        Token current = token;
        for (int i = 0; i < n; i++) {
            if (current.next == null) {
                current.next = token_source.getNextToken();
            }
            current = current.next;
        }
        return current;
    }
}
