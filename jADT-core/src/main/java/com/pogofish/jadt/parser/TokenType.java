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

/**
 * The set of token types returned by the tokenizer
 *
 * @author jiry
 */
public enum TokenType {
    // keywords
    /** "package" */
    PACKAGE,
    /** "import" */
    IMPORT, 
    /** "boolean" */
    BOOLEAN,
    /** "byte" */
    BYTE,
    /** "double" */
    DOUBLE,
    /** "char" */
    CHAR,
    /** "float" */
    FLOAT,
    /** "int" */
    INT,
    /** "long" */
    LONG,
    /** "short" */
    SHORT,
    /** "final" */
    FINAL,
    
    /** reserved java keyword unused by jadt */
    JAVA_KEYWORD,
    
    // word classes    
    /** valid java identifier */
    IDENTIFIER,
    /** some other unknown word */
    UNKNOWN,
    
    // punctuation
    /** "=" */
    EQUALS, 
    /** "," */
    COMMA, 
    /** "|" */
    BAR, 
    /** "(" */
    LPAREN, 
    /** ")" */
    RPAREN, 
    /** "<" */
    LANGLE, 
    /** ">" */
    RANGLE, 
    /** "[" */
    LBRACKET, 
    /** "]" */
    RBRACKET,
    /** "." */
    DOT,
    /** end of file */
    EOF;
}