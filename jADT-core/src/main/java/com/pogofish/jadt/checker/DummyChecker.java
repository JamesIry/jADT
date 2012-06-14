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

import java.util.Set;

import com.pogofish.jadt.ast.Doc;


/**
 * Dummy checked usesd for testing. It returns a specified set of exceptions when
 * asked to check a Doc
 *
 * @author jiry
 */
public class DummyChecker implements Checker {
    private final Set<SemanticException> exceptions;

    /**
     * 
     * @param exceptions the SemanticExceptions to be returned by the check method
     */
    public DummyChecker(Set<SemanticException> exceptions) {
        super();
        this.exceptions = exceptions;
    }

    /**
     * returns the set of exceptions specified in the constructor
     */
    @Override
    public Set<SemanticException> check(Doc doc) {
        return exceptions;
    }
}
