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

import java.util.Set;

/**
 * Interface for Tokenizers used by StandardParser
 * 
 * @author jiry
 */
interface ITokenizer {

    /**
     * Returns the last symbol recognized by this Tokenizer
     * 
     * @return String 
     */
    public abstract String lastSymbol();

    /**
     * Returns the line number of the last token type returned by this Tokenizer
     * 
     * @return int 1 based line number
     */
    public abstract int lineno();

    /**
     * Return info about the source from which this tokenizer was created
     * @return
     */
    public abstract String srcInfo();

    /**
     * Make it so that the last token returned will be the next token returned
     */
    public abstract void pushBack();

    /**
     * Get the next Token
     * 
     * @return a Token
     */
    public abstract TokenType getTokenType();

    /**
     * Set of all punctuation types
     * @return
     */
    public abstract Set<TokenType> punctuation();

}