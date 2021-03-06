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

import java.util.List;

import com.pogofish.jadt.ast.Constructor;
import com.pogofish.jadt.sink.Sink;


/**
 * Emitter for a Constructor class
 *
 * @author jiry
 */
public interface ConstructorEmitter {    
    /**
     * The constructor factory 
     * 
     * @param sink Sink for output
     * @param dataTypeName name of the DataType to return from the factory
     * @param constructor the Constructor with information about arguments
     */
    public void constructorFactory(Sink sink, String dataTypeName, List<String> typeParameters, Constructor constructor);

    /**
     * The complete declaration of the constructor class
     * 
     * @param sink Sink for output
     * @param constructor the Constructor with information about arguments
     * @param dataTypeName name of the DataType that the Constructor class will extend
     * @param typeParameters names of type arguments to apply to the constructor's class and the class it extends
     */
    public void constructorDeclaration(Sink sink, Constructor constructor, String dataTypeName, List<String> typeParameters);
}
