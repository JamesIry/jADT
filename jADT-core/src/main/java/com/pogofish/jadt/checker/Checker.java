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
package com.pogofish.jadt.checker;

import java.util.List;

import com.pogofish.jadt.ast.Doc;
import com.pogofish.jadt.ast.SemanticError;


/**
 * Checks for semantic errors in a document
 *
 * @author jiry
 */
public interface Checker {

    /**
     * Checks for semantic errors in a document
     * 
     * @param doc
     * @return A set of SemanticErrors - empty set if none
     */
    public List<SemanticError> check(Doc doc);

}
