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

import java.io.Reader;
import java.util.Collections;
import java.util.Set;

import com.pogofish.jadt.parser.javacc.JavaCharStream;
import com.pogofish.jadt.parser.javacc.StandardParser2Constants;
import com.pogofish.jadt.parser.javacc.StandardParser2TokenManager;
import com.pogofish.jadt.parser.javacc.Token;
import com.pogofish.jadt.util.Util;

/**
 * Modeled after the interface of a subset of Java's StreamTokenizer, but returns TokenTypes designed for
 * jADT instead of StreamTokenizer's generic tokens and uses a JavaCC generated TokenManager as its implementation
 *
 * @author jiry
 */
public class Tokenizer implements ITokenizer, StandardParser2Constants {    
    
    private final String srcInfo;
    private final StandardParser2TokenManager manager;
    private Token token = null;
    private boolean pushed = false;
    
    /**
     * Set of punctuation characters.  Useful for syntax error recovery
     */
    private static final Set<TokenType> punctuation = Collections.unmodifiableSet(Util.set(
            TokenType.LANGLE, TokenType.RANGLE, TokenType.EQUALS, TokenType.LPAREN, TokenType.RPAREN, 
            TokenType.COMMA, TokenType.BAR, TokenType.LBRACKET, TokenType.RBRACKET, TokenType.DOT, TokenType.EOF));

    public Tokenizer(String srcInfo, Reader reader) {
        this.srcInfo = srcInfo;
        this.manager = new StandardParser2TokenManager(new JavaCharStream(new JavaCCReader(reader)));
    }
    
    
    @Override
    public String lastSymbol() {
        if (token.kind == EOF) {
            return "<EOF>";
        }
        return token.image;
    }

    @Override
    public int lineno() {
        return token.beginLine;
    }

    @Override
    public String srcInfo() {
        return srcInfo;
    }

    @Override
    public void pushBack() {
        if (pushed) {
            throw new RuntimeException("already pushed back one token");
        }
        pushed = true;
        
    }

    @Override
    public TokenType getTokenType() {
        if (pushed) {
            pushed = false;
        } else {
            token = manager.getNextToken();
        }
        final int kind = token.kind;
        switch (kind) {
        case EOF: return TokenType.EOF;
        case LANGLE: return TokenType.LANGLE;
        case RANGLE: return TokenType.RANGLE;
        case EQUALS: return TokenType.EQUALS;
        case LPAREN: return TokenType.LPAREN;
        case RPAREN: return TokenType.RPAREN;
        case COMMA: return TokenType.COMMA;
        case BAR: return TokenType.BAR;
        case LBRACKET: return TokenType.LBRACKET;
        case RBRACKET: return TokenType.RBRACKET;
        case DOT: return TokenType.DOT;
        case IMPORT: return TokenType.IMPORT;
        case PACKAGE: return TokenType.PACKAGE;
        case FINAL: return TokenType.FINAL;
        case BOOLEAN: return TokenType.BOOLEAN;
        case CHAR: return TokenType.CHAR;
        case SHORT: return TokenType.SHORT;
        case INT: return TokenType.INT;
        case LONG: return TokenType.LONG;
        case FLOAT: return TokenType.FLOAT;
        case DOUBLE: return TokenType.DOUBLE;
        case JAVA_KEYWORD: return TokenType.JAVA_KEYWORD;
        case IDENTIFIER: return TokenType.IDENTIFIER;
        case UNKNOWN: return TokenType.UNKNOWN;
        default: throw new RuntimeException("unrecognized token " + kind + " " + token.image);
        }
    }


    @Override
    public Set<TokenType> punctuation() {
        return punctuation;
    }
}
