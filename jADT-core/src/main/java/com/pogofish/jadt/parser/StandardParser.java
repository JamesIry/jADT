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

import java.io.BufferedReader;
import java.util.logging.Logger;

import com.pogofish.jadt.ast.Doc;
import com.pogofish.jadt.ast.ParseResult;
import com.pogofish.jadt.source.Source;
import com.pogofish.jadt.util.ExceptionAction;
import com.pogofish.jadt.util.Util;

/**
 * The standard parser for jADT description files
 * 
 * @author jiry
 */
public class StandardParser implements Parser {
    private final ParserImplFactory factory;

    static final Logger logger = Logger.getLogger(StandardParser.class
            .toString());

    public StandardParser(ParserImplFactory factory) {
        super();
        this.factory = factory;
    }

    /*
     * (non-Javadoc)
     * 
     * @see sfdc.adt.IParser#parse(java.lang.String, java.io.Reader)
     */
    @Override
    public ParseResult parse(final Source source) {
        return Util.execute(new ExceptionAction<ParseResult>() {
            @Override
            public ParseResult doAction() throws Throwable {
                logger.fine("Parsing " + source.getSrcInfo());
                final BufferedReader reader = source.createReader();
                try {
                    final ParserImpl impl = factory.create(source.getSrcInfo(),
                            reader);
                    final Doc doc = impl.doc();
                    return new ParseResult(doc, impl.errors());
                } finally {
                    reader.close();
                }
            }
        });
    }
}
