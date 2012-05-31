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
package pogofish.jadt.checker;

/**
 * SemanticException indicating that a data type and a constructor had the same name
 *
 * @author jiry
 */
public abstract class SemanticException extends RuntimeException {

	private static final long serialVersionUID = 6663550218226785742L;

	public SemanticException(String msg) {
        super(msg);
    }

}
