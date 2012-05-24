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
package pogofish.jadt.parser;

import java.io.IOException;
import java.io.StreamTokenizer;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import pogofish.jadt.source.Source;

/**
 * Modeled after the interface of a subset of Java's StreamTokenizer, but returns TokenTypes designed for
 * JADT instead of StreamTokenizer's generic tokens
 *
 * @author jiry
 */
class Tokenizer {
    /**
     * Regex for one piece of an identifier
     */
    private static final String IDENTIFIER_CHUNK = "\\p{javaJavaIdentifierStart}\\p{javaJavaIdentifierPart}*";
    /**
     * Regex for a valid java identifier
     */
    private static final Pattern IDENTIFIER_REGEX = Pattern.compile(IDENTIFIER_CHUNK);
    /**
     * Regex for a valid dotted java identifier, e.g. in a package name
     */
    private static final Pattern DOTTED_IDENTIFIER_REGEX = Pattern.compile("(" + IDENTIFIER_CHUNK + "\\.)+" + IDENTIFIER_CHUNK);
    
    /** 
     * Map from keywords to their token type
     */
    private static final Map<String, TokenType> KEYWORDS = new HashMap<String, TokenType>();
    
    {        
        // keywords actually used by JADT
        KEYWORDS.put("import", TokenType.IMPORT);
        KEYWORDS.put("package", TokenType.PACKAGE);

        // Java primitive types
        KEYWORDS.put("boolean", TokenType.BOOLEAN);
        KEYWORDS.put("double", TokenType.DOUBLE);
        KEYWORDS.put("char", TokenType.CHAR);
        KEYWORDS.put("float", TokenType.FLOAT);
        KEYWORDS.put("int", TokenType.INT);
        KEYWORDS.put("long", TokenType.LONG);
        KEYWORDS.put("short", TokenType.SHORT);
        
        // most Java keywords are unused but reserved so they can't be used in an ADT definition and thus screw up the generated Java        
        KEYWORDS.put("abstract", TokenType.JAVA_KEYWORD);
        KEYWORDS.put("assert", TokenType.JAVA_KEYWORD);
        KEYWORDS.put("break", TokenType.JAVA_KEYWORD);
        KEYWORDS.put("byte", TokenType.JAVA_KEYWORD);
        KEYWORDS.put("case", TokenType.JAVA_KEYWORD);
        KEYWORDS.put("catch", TokenType.JAVA_KEYWORD);
        KEYWORDS.put("class", TokenType.JAVA_KEYWORD);
        KEYWORDS.put("const", TokenType.JAVA_KEYWORD);
        KEYWORDS.put("continue", TokenType.JAVA_KEYWORD);
        KEYWORDS.put("default", TokenType.JAVA_KEYWORD);
        KEYWORDS.put("do", TokenType.JAVA_KEYWORD);
        KEYWORDS.put("else", TokenType.JAVA_KEYWORD);
        KEYWORDS.put("enum", TokenType.JAVA_KEYWORD);
        KEYWORDS.put("extends", TokenType.JAVA_KEYWORD);
        KEYWORDS.put("final", TokenType.JAVA_KEYWORD);
        KEYWORDS.put("finally", TokenType.JAVA_KEYWORD);
        KEYWORDS.put("for", TokenType.JAVA_KEYWORD);
        KEYWORDS.put("goto", TokenType.JAVA_KEYWORD);
        KEYWORDS.put("if", TokenType.JAVA_KEYWORD);
        KEYWORDS.put("implements", TokenType.JAVA_KEYWORD);
        KEYWORDS.put("instanceof", TokenType.JAVA_KEYWORD);
        KEYWORDS.put("interface", TokenType.JAVA_KEYWORD);
        KEYWORDS.put("native", TokenType.JAVA_KEYWORD);
        KEYWORDS.put("new", TokenType.JAVA_KEYWORD);
        KEYWORDS.put("private", TokenType.JAVA_KEYWORD);
        KEYWORDS.put("protected", TokenType.JAVA_KEYWORD);
        KEYWORDS.put("public", TokenType.JAVA_KEYWORD);
        KEYWORDS.put("return", TokenType.JAVA_KEYWORD);
        KEYWORDS.put("static", TokenType.JAVA_KEYWORD);
        KEYWORDS.put("strictfp", TokenType.JAVA_KEYWORD);
        KEYWORDS.put("super", TokenType.JAVA_KEYWORD);
        KEYWORDS.put("switch", TokenType.JAVA_KEYWORD);
        KEYWORDS.put("synchronized", TokenType.JAVA_KEYWORD);
        KEYWORDS.put("this", TokenType.JAVA_KEYWORD);
        KEYWORDS.put("throw", TokenType.JAVA_KEYWORD);
        KEYWORDS.put("throws", TokenType.JAVA_KEYWORD);
        KEYWORDS.put("transient", TokenType.JAVA_KEYWORD);
        KEYWORDS.put("try", TokenType.JAVA_KEYWORD);
        KEYWORDS.put("void", TokenType.JAVA_KEYWORD);
        KEYWORDS.put("volatile", TokenType.JAVA_KEYWORD);
        KEYWORDS.put("while", TokenType.JAVA_KEYWORD);
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
     * Constructs a Tokenizer that will tokenize the specified Reade
     * 
     * @param reader the reader with the JADT source to be tokenized
     */
    public Tokenizer(Source source) {
        srcInfo = source.getSrcInfo();
        tokenizer = new StreamTokenizer(source.getReader());
        tokenizer.resetSyntax();
        tokenizer.slashSlashComments(true);
        tokenizer.slashStarComments(true);
        tokenizer.wordChars(0, Integer.MAX_VALUE);
        tokenizer.ordinaryChar('<');
        tokenizer.ordinaryChar('>');
        tokenizer.ordinaryChar('=');
        tokenizer.ordinaryChar('(');
        tokenizer.ordinaryChar(')');
        tokenizer.ordinaryChar(',');
        tokenizer.ordinaryChar('|');
        tokenizer.ordinaryChar('[');
        tokenizer.ordinaryChar(']');
        tokenizer.ordinaryChar('{'); // added just for coverage of the unknown token type case
        tokenizer.whitespaceChars(' ', ' ');
        tokenizer.whitespaceChars('\t', '\t');
        tokenizer.whitespaceChars('\n', '\n');
        tokenizer.whitespaceChars('\r', '\r');
        tokenizer.eolIsSignificant(false);
    }

    /**
     * Get the next Token
     * 
     * @return a Token
     */
    private TokenType getTokenType() {
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
                final Matcher dottedIdentifierMatcher = DOTTED_IDENTIFIER_REGEX.matcher(symbol);
                if (dottedIdentifierMatcher.matches()) {
                    return TokenType.DOTTED_IDENTIFIER;
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
        default:
            symbol = "" + (char)tokenType;
            return TokenType.UNKNOWN;
        }
    }
    
    /**
     * Peeks at the next available token without removing it
     * 
     * @return
     */
    public TokenType peek() {
        final TokenType tokenType = getTokenType();
        tokenizer.pushBack();
        return tokenType;
    }

    /**
     * If the next token type i
     * @param expected
     * @return
     */
    public boolean accept(TokenType expected) {
        final TokenType token = getTokenType();
        if (token.equals(expected)) {
            return true;
        } else {
            tokenizer.pushBack();
            return false;
        }
    }
    
    /**
     * Returns the last symbol recognized by this Tokenizer
     * 
     * @return String 
     */
    public String lastSymbol() {
        return symbol;
    }

    /**
     * Returns the line number of the last token type returned by this Tokenizer
     * 
     * @return int 1 based line number
     */
    public int lineno() {
        return tokenizer.lineno();
    }
    
    /**
     * Return info about the source from which this tokenizer was created
     * @return
     */
    public String srcInfo() {
        return srcInfo;
    }

}
