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

import com.pogofish.jadt.ast.ParseResult;
import com.pogofish.jadt.source.Source;


/**
 * Interface for the jADT description file parsers.
 *
 * @author jiry
 */
public interface Parser {

    /**
     * Parse a complete document
     * The project README.md has a BNF style grammar for the parser.
     * 
     * @param source Source to be parsed
     * @return ParseResult with a document and a list of syntax errors
     * @throws RuntimeException with an IOExceptino if there are any problems reading
     */
    public abstract ParseResult parse(Source source);

}