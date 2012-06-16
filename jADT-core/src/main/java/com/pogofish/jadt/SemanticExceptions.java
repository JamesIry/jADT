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
package com.pogofish.jadt;

import java.util.Set;

import com.pogofish.jadt.ast.SemanticError;
import com.pogofish.jadt.printer.SemanticErrorPrinter;


/**
 * Exception thrown when there are one or more semantic exceptions
 * 
 * @author jiry
 */
public class SemanticExceptions extends RuntimeException {

	private static final long serialVersionUID = 799507299528324679L;

	public SemanticExceptions(Set<SemanticError> errors) {
        super(makeString(errors));
    }
	
	private static final String makeString(Set<SemanticError> errors) {
	    final StringBuilder builder = new StringBuilder();
	    for (SemanticError error : errors) {
	        builder.append(SemanticErrorPrinter.print(error));
	        builder.append("\n\n");
	    }
	    return builder.toString();
	}

}
