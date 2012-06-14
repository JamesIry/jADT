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
 * An exception occuring during the parsing of a jADT description file
 *
 * @author jiry
 */
public class SyntaxException extends RuntimeException {

	private static final long serialVersionUID = -8863574669612380241L;

	/**
     * Constructor based on a message with the problem
     * 
     * @param msg String explaining what went wrong
     */
    public SyntaxException(String msg) {
      super(msg);
    }

}
