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
 * Dummy ConstructorEmitter that outputs simple comments with data type name and constructor name
 *
 * @author jiry
 */
public class DummyConstructorEmitter implements ConstructorEmitter {

    @Override
    public void constructorFactory(Sink sink, String dataTypeName, List<String> typeParameters, Constructor constructor) {
        sink.write("/* factory " + dataTypeName + " " + constructor.name + " */");
    }

    @Override
    public void constructorDeclaration(Sink sink, Constructor constructor, String dataTypeName, List<String> typeParamters) {
        sink.write("/* declaration " + dataTypeName + " " + constructor.name + " */");
    }

}
