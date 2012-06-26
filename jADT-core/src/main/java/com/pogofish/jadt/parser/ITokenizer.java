package com.pogofish.jadt.parser;

import java.util.Set;

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