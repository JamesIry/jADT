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

import static com.pogofish.jadt.ast.JavaComment._JavaDocComment;
import static com.pogofish.jadt.ast.JavaComment._JavaEOLComment;
import static com.pogofish.jadt.ast.JavaComment._JavaMultiLineComment;

import java.io.Reader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import com.pogofish.jadt.ast.JavaComment;
import com.pogofish.jadt.ast.SyntaxError;
import com.pogofish.jadt.parser.ParserImpl;
import com.pogofish.jadt.parser.javacc.generated.BaseJavaCCParserImpl;
import com.pogofish.jadt.parser.javacc.generated.Token;
import com.pogofish.jadt.util.Util;

/**
 * Sub class for the JavaCC generated parser. This class exists just so most
 * Java code can live in a .java file instead of a much harder to edit .jj file
 * 
 * @author jiry
 */
public class JavaCCParserImpl extends BaseJavaCCParserImpl implements ParserImpl {
    private static final String COMMENT_NOT_ALLOWED = "a java comment, which is only allowed before 'package', 'import', data type definitions and constructor defintions";

    private static final String UNTERMINATED_COMMENT_STRING = "unterminated comment";

    private static final String EOF_STRING = "<EOF>";

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


    @Override
    protected void checkNoComments(String expected) {
        if (token.specialToken != null) {
            error(expected, COMMENT_NOT_ALLOWED);
        }

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
    protected String badIdentifier(String expected) {
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
    protected void recovered() {
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
    protected void error(String expected) {
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
            final String outputString = (EOF_STRING.equals(actual) || UNTERMINATED_COMMENT_STRING.equals(actual) || COMMENT_NOT_ALLOWED.equals(actual)) ? actual : "'"
                    + actual + "'";
            errors.add(SyntaxError._UnexpectedToken(expected, outputString,
                    lookahead(1).beginLine));
        }
    }

    /**
     * Turn a token into a user readable name
     */
    private String friendlyName(Token token) {
        return token.kind == EOF ? EOF_STRING : token.kind == UNTERMINATED_COMMENT ? UNTERMINATED_COMMENT_STRING : token.image;
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


    /**
     * Return all the comments attached to the current token (the last token matched)
     */
    @Override
    protected List<JavaComment> tokenComments() {
        final List<JavaComment> comments = new ArrayList<JavaComment>();
        Token comment = token.specialToken;
        while(comment != null) {
            switch(comment.kind) {
            case JAVA_EOL_COMMENT:
                comments.add(_JavaEOLComment(comment.image));
                break;
            case JAVA_ML_COMMENT:
                comments.add(_JavaMultiLineComment(comment.image));
                break;
            case JAVADOC_COMMENT:
                comments.add(_JavaDocComment(comment.image));
                break;
            default:
             // anything else is not a comment and not our problem here
            }
            comment = comment.specialToken;
        }
        Collections.reverse(comments);
        return comments;
    }
}
