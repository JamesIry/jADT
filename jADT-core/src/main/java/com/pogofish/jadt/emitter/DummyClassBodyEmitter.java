/*
 * Copyright 2012 James Iry Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0 Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language governing permissions and limitations under the
 * License.
 */
package com.pogofish.jadt.emitter;

import java.util.List;

import com.pogofish.jadt.ast.Constructor;
import com.pogofish.jadt.sink.Sink;


/**
 * Dummy ClassBodyEmitter that just outputs comments saying what it is generating
 */
public class DummyClassBodyEmitter implements ClassBodyEmitter {

    @Override
    public void constructorFactory(Sink sink, String dataTypeName, String factoryName, List<String> typeParameters, Constructor constructor) {
        sink.write("/* constructor factory " + dataTypeName + " " + factoryName + " " + constructor.name + "*/");

    }

    @Override
    public void emitConstructorMethod(Sink sink, Constructor constructor) {
        sink.write("/* constructor method " + constructor.name + "*/");
    }

    @Override
    public void emitToString(Sink sink, Constructor constructor) {
        sink.write("/* toString method " + constructor.name + "*/");
    }

    @Override
    public void emitEquals(Sink sink, Constructor constructor, List<String> typeArguments) {
        sink.write("/* equals method " + constructor.name + "*/");
    }

    @Override
    public void emitHashCode(Sink sink, Constructor constructor) {
        sink.write("/* hashCode method " + constructor.name + "*/");
    }
    
    @Override
	public void emitParameterizedTypeName(Sink sink,
			List<String> typeArguments) {
		sink.write("/* type arguments */");
	}   

}
