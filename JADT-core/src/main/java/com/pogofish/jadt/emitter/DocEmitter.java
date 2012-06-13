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
package com.pogofish.jadt.emitter;

import com.pogofish.jadt.ast.Doc;
import com.pogofish.jadt.target.TargetFactory;


/**
 * Emitter for a whole document
 *
 * @author jiry
 */
public interface DocEmitter {

    /**
     * Emits a document to targets created by the specified factory
     * 
     * @param factory TargetFactory that will produce targets for output
     * @param doc Document to output
     */
    public abstract void emit(TargetFactory factory, Doc doc);

}