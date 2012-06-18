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

import com.pogofish.jadt.ast.UserError;
import com.pogofish.jadt.printer.UserErrorPrinter;


/**
 * Exception thrown when there are one or more user errors discovered during processing
 * of a jADT document
 * 
 * @author jiry
 */
public class JADTUserErrorsException extends RuntimeException {

	private static final long serialVersionUID = 799507299528324679L;
	
	private final Set<UserError> errors;

	public JADTUserErrorsException(Set<UserError> errors) {
        super(makeString(errors));
        this.errors = errors;
    }
	
	private static final String makeString(Set<UserError> errors) {
	    final StringBuilder builder = new StringBuilder();
	    for (UserError error : errors) {
	        builder.append(UserErrorPrinter.print(error));
	        builder.append("\n\n");
	    }
	    return builder.toString();
	}
	
	/**
	 * Return the list of errors in this exception
	 */
	public Set<UserError> getErrors() {
	    return errors;
	}

}
