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

import com.pogofish.jadt.ast.DataType;
import com.pogofish.jadt.sink.Sink;


/**
 * Emits a DataType
 *
 * @author jiry
 */
public interface DataTypeEmitter {

    /**
     * Emit a datatype
     * 
     * @param sink Sink for output
     * @param dataType DataType to output
     * @param header String preamble with things like package and imports
     */
    public abstract void emit(Sink sink, DataType dataType, String header);

}