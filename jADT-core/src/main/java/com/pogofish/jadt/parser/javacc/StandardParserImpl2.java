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

import com.pogofish.jadt.ast.RefType;
import com.pogofish.jadt.ast.SyntaxError;
import com.pogofish.jadt.util.Util;

/**
 * Sub class for the JavaCC generated parser. This class exists just so most
 * Java code can live in a .java file instead of a much harder to edit .jj file
 * 
 * @author jiry
 */
public class StandardParserImpl2 extends BaseStandardParserImpl2 {
    public StandardParserImpl2(String srcInfo, Reader stream) {
        super(stream);
        this.srcInfo = srcInfo;
    }

    private final String srcInfo;;

    private final List<SyntaxError> errors = new ArrayList<SyntaxError>();
    
    private int nextId = 1;
    
    /**
     * Set of punctuation characters.  Useful for syntax error recovery
     */
    private static final Set<Integer> punctuation = Collections.unmodifiableSet(Util.set(
            LANGLE, RANGLE, EQUALS, LPAREN, RPAREN, 
            COMMA, BAR, LBRACKET, RBRACKET, DOT, EOF));
    
    @Override
    String badIdentifier(String expected) {
        error(expected);
        if (!peekPunctuation()) {
            final Token token = getNextToken();
            return "BAD_IDENTIFIER" + "_" + friendlyName(token) + nextId();
        } else {
            return "NO_IDENTIFIER" + nextId();
        }        
    }
    
    private boolean peekPunctuation() {
        return (punctuation.contains(lookahead(1).kind));
    }

    private String nextId() {
        return "@" + (nextId++);
    }

    public List<SyntaxError> errors() {
        return errors;
    }
    
    public String getSrcInfo() {
        return srcInfo;
    }
    
    /**
     * If not currently recovering, adds an error to the errors list and sets recovering to true.
     * Actual is assumed to be the last symbol from the tokenizer.

     * @param expected the kind of thing expected
     */
    public void error(String expected) {
        error(expected, friendlyName(lookahead(1)));
    }
    
    /**
     * If not currently recovering, adds an error to the errors list and sets recovering to true
     * @param expected the kind of thing expected
     */
    public void error(String expected, String actual) {
        if (!recovering) {
            recovering = true;
           final String outputString = "<EOF>".equals(actual) ? actual : "'" + actual + "'";
            errors.add(SyntaxError._UnexpectedToken(expected, outputString, lookahead(1).beginLine));
        }
    }
    
    public String friendlyName(Token token) {
        return token.kind == EOF ? "<EOF>" : token.image;
    }
    
    /**
     * look ahead n tokens.  0 is the current token, 1 is the next token, 2 the one after that, etc
     * @param n
     * @return
     */
    private Token lookahead(int n) {
        Token current = token;
        for (int i = 0 ; i < n ; i ++) {
            if (current.next == null) {
                current.next = token_source.getNextToken();
            }
            current = current.next;
        }
        return current;
    }
    
    private boolean peek(int tokenKind) {
        final Token next = lookahead(1);
        return next.kind == tokenKind;
    }
}
