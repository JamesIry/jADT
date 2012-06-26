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

import java.io.IOException;
import java.io.Reader;
import java.io.StreamTokenizer;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Modeled after the interface of a subset of Java's StreamTokenizer, but returns TokenTypes designed for
 * jADT instead of StreamTokenizer's generic tokens
 *
 * @author jiry
 */
class Tokenizer implements ITokenizer {
    /**
     * Regex for a valid java identifier
     */
    private static final Pattern IDENTIFIER_REGEX = Pattern.compile("\\p{javaJavaIdentifierStart}\\p{javaJavaIdentifierPart}*");
    
    /** 
     * Map from keywords to their token type.
     */
    private static final Map<String, TokenType> KEYWORDS = createKeywords();
    
    private static Map<String, TokenType> createKeywords() {       
        final Map<String, TokenType> keywords = new HashMap<String, TokenType>();
        
        // keywords actually used by jADT
        keywords.put("import", TokenType.IMPORT);
        keywords.put("package", TokenType.PACKAGE);

        // Java primitive types
        keywords.put("boolean", TokenType.BOOLEAN);
        keywords.put("double", TokenType.DOUBLE);
        keywords.put("char", TokenType.CHAR);
        keywords.put("float", TokenType.FLOAT);
        keywords.put("int", TokenType.INT);
        keywords.put("long", TokenType.LONG);
        keywords.put("short", TokenType.SHORT);
        keywords.put("final", TokenType.FINAL);
        
        // most Java keywords are unused but reserved so they can't be used in an ADT definition and thus screw up the generated Java        
        keywords.put("abstract", TokenType.JAVA_KEYWORD);
        keywords.put("assert", TokenType.JAVA_KEYWORD);
        keywords.put("break", TokenType.JAVA_KEYWORD);
        keywords.put("byte", TokenType.JAVA_KEYWORD);
        keywords.put("case", TokenType.JAVA_KEYWORD);
        keywords.put("catch", TokenType.JAVA_KEYWORD);
        keywords.put("class", TokenType.JAVA_KEYWORD);
        keywords.put("const", TokenType.JAVA_KEYWORD);
        keywords.put("continue", TokenType.JAVA_KEYWORD);
        keywords.put("default", TokenType.JAVA_KEYWORD);
        keywords.put("do", TokenType.JAVA_KEYWORD);
        keywords.put("else", TokenType.JAVA_KEYWORD);
        keywords.put("enum", TokenType.JAVA_KEYWORD);
        keywords.put("extends", TokenType.JAVA_KEYWORD);
        keywords.put("finally", TokenType.JAVA_KEYWORD);
        keywords.put("for", TokenType.JAVA_KEYWORD);
        keywords.put("goto", TokenType.JAVA_KEYWORD);
        keywords.put("if", TokenType.JAVA_KEYWORD);
        keywords.put("implements", TokenType.JAVA_KEYWORD);
        keywords.put("instanceof", TokenType.JAVA_KEYWORD);
        keywords.put("interface", TokenType.JAVA_KEYWORD);
        keywords.put("native", TokenType.JAVA_KEYWORD);
        keywords.put("new", TokenType.JAVA_KEYWORD);
        keywords.put("private", TokenType.JAVA_KEYWORD);
        keywords.put("protected", TokenType.JAVA_KEYWORD);
        keywords.put("public", TokenType.JAVA_KEYWORD);
        keywords.put("return", TokenType.JAVA_KEYWORD);
        keywords.put("static", TokenType.JAVA_KEYWORD);
        keywords.put("strictfp", TokenType.JAVA_KEYWORD);
        keywords.put("super", TokenType.JAVA_KEYWORD);
        keywords.put("switch", TokenType.JAVA_KEYWORD);
        keywords.put("synchronized", TokenType.JAVA_KEYWORD);
        keywords.put("this", TokenType.JAVA_KEYWORD);
        keywords.put("throw", TokenType.JAVA_KEYWORD);
        keywords.put("throws", TokenType.JAVA_KEYWORD);
        keywords.put("transient", TokenType.JAVA_KEYWORD);
        keywords.put("try", TokenType.JAVA_KEYWORD);
        keywords.put("void", TokenType.JAVA_KEYWORD);
        keywords.put("volatile", TokenType.JAVA_KEYWORD);
        keywords.put("while", TokenType.JAVA_KEYWORD);
        
        return Collections.unmodifiableMap(keywords);
    }
    
    private final String srcInfo;
    
    /**
     * Current implementation uses a StreamTokenizer under the hood
     */
    private final StreamTokenizer tokenizer;

    /**
     * The last symbol recognized
     */
    private String symbol = null;
    
    /**
     * Set of all punctuation symbols, useful for syntax error recovery
     */
    final Set<TokenType> punctuation;

    /**
     * Constructs a Tokenizer that will tokenize the specified Reader
     * 
     * @param reader the reader with the jADT source to be tokenized
     */
    public Tokenizer(String srcInfo, Reader reader) {
        Set<TokenType> punctuation = new HashSet<TokenType>();
        
        this.srcInfo = srcInfo;
        tokenizer = new StreamTokenizer(reader);
        tokenizer.resetSyntax();
        tokenizer.wordChars(0, Integer.MAX_VALUE);
        tokenizer.ordinaryChar('<');
        punctuation.add(TokenType.LANGLE);
        tokenizer.ordinaryChar('>');
        punctuation.add(TokenType.RANGLE);
        tokenizer.ordinaryChar('=');
        punctuation.add(TokenType.EQUALS);
        tokenizer.ordinaryChar('(');
        punctuation.add(TokenType.LPAREN);
        tokenizer.ordinaryChar(')');
        punctuation.add(TokenType.RPAREN);
        tokenizer.ordinaryChar(',');
        punctuation.add(TokenType.COMMA);
        tokenizer.ordinaryChar('|');
        punctuation.add(TokenType.BAR);
        tokenizer.ordinaryChar('[');
        punctuation.add(TokenType.LBRACKET);
        tokenizer.ordinaryChar(']');
        punctuation.add(TokenType.RBRACKET);
        tokenizer.ordinaryChar('.');
        punctuation.add(TokenType.DOT);
        tokenizer.whitespaceChars(' ', ' ');
        tokenizer.whitespaceChars('\t', '\t');
        tokenizer.whitespaceChars('\n', '\n');
        tokenizer.whitespaceChars('\r', '\r');
        tokenizer.eolIsSignificant(false);
        tokenizer.ordinaryChar('/');
        tokenizer.ordinaryChar('*');
        tokenizer.slashSlashComments(true);
        tokenizer.slashStarComments(true);
        
        punctuation.add(TokenType.EOF);
        this.punctuation = Collections.unmodifiableSet(punctuation);
    }

    /**
     * Get the next Token
     * 
     * @return a Token
     */
    @Override
    public TokenType getTokenType() {
        final int tokenType;
        try {
            tokenType = tokenizer.nextToken();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        
        switch (tokenType) {
        case StreamTokenizer.TT_EOF:
            symbol = "<EOF>";
            return TokenType.EOF;
        case StreamTokenizer.TT_WORD:
            symbol = tokenizer.sval;
            if (KEYWORDS.containsKey(symbol)) {
                return KEYWORDS.get(symbol);
            } else {
                final Matcher identifierMatcher = IDENTIFIER_REGEX.matcher(symbol);
                if (identifierMatcher.matches()) {
                    return TokenType.IDENTIFIER;
                }
                return TokenType.UNKNOWN;
            }
        case '(':
            symbol = "(";
            return TokenType.LPAREN;
        case ')':
            symbol = ")";
            return TokenType.RPAREN;
        case '<':
            symbol = "<";
            return TokenType.LANGLE;
        case '>':
            symbol = ">";
            return TokenType.RANGLE;
        case '[':
            symbol = "[";
            return TokenType.LBRACKET;
        case ']':
            symbol = "]";
            return TokenType.RBRACKET;
        case '=':
            symbol = "=";
            return TokenType.EQUALS;
        case ',':
            symbol = ",";
            return TokenType.COMMA;
        case '|':
            symbol = "|";
            return TokenType.BAR;
        case '.':
            symbol = ".";
            return TokenType.DOT;
        default:
            symbol = "" + (char)tokenType;
            return TokenType.UNKNOWN;
        }
    }
    

    
    /* (non-Javadoc)
     * @see com.pogofish.jadt.parser.ITokenizer#lastSymbol()
     */
    @Override
    public String lastSymbol() {
        return symbol;
    }

    /* (non-Javadoc)
     * @see com.pogofish.jadt.parser.ITokenizer#lineno()
     */
    @Override
    public int lineno() {
        return tokenizer.lineno();
    }
    
    /* (non-Javadoc)
     * @see com.pogofish.jadt.parser.ITokenizer#srcInfo()
     */
    @Override
    public String srcInfo() {
        return srcInfo;
    }

    /* (non-Javadoc)
     * @see com.pogofish.jadt.parser.ITokenizer#pushBack()
     */
    @Override
    public void pushBack() {
        tokenizer.pushBack();        
    }

    @Override
    public Set<TokenType> punctuation() {
        return punctuation;
    }

    
}
