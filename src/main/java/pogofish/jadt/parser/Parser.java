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

import java.io.Reader;

import pogofish.jadt.ast.Doc;

/**
 * Interface for the JADT description file parsers.
 *
 * @author jiry
 */
public interface Parser {

    /**
     * Parse a complete document
     * The project README.md has a BNF style grammar for the parser.
     * 
     * @param srcInfo String information about the source used for error reporting
     * @param reader Reader with the source 
     * @return a Document
     * @throws RuntimeException with an IOExceptino if there are any problems reading
     * @throws SyntaxException if there are any problems with the source
     */
    public abstract Doc parse(String srcInfo, Reader reader);

}