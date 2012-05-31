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
package pogofish.jadt.emitter;

import java.util.List;

import pogofish.jadt.ast.Constructor;
import pogofish.jadt.target.Target;

/**
 * Emitter for the methods that go in a constructor body
 *
 * @author jiry
 */
public interface ClassBodyEmitter {

    /**
     * The constructor factory 
     * 
     * @param target Target for output
     * @param dataTypeName name of the DataType to return from the factory
     * @param factoryName name of the factory method/constant to create
     * @param typeParameters List of String names for type parameters
     * @param constructor the Constructor with information about arguments
     */
    public abstract void constructorFactory(Target target, String dataTypeName, String factoryName, List<String> typeParameters, Constructor constructor);

    /**
     * Emit the Java constructor for the constructor class
     * 
     * @param target Target for output
     * @param constructor Constructor with information about arguments
     */
    public abstract void emitConstructorMethod(Target target, Constructor constructor);

    /**
     * Emit a toString method
     * 
     * @param target Target for output
     * @param constructor Constructor with information about arguments
     */
    public abstract void emitToString(Target target, Constructor constructor);

    /**
     * Emit an equals method
     * 
     * @param target Target for output
     * @param constructor Constructor with information about arguments
     * @param typeArguments List of names of types that parameterize the datatype
     */
    public abstract void emitEquals(Target target, Constructor constructor, List<String> typeArguments);

    /**
     * Emit a hashCode method
     * 
     * @param target Target for output
     * @param constructor Constructor with information about arguments
     */
    public abstract void emitHashCode(Target target, Constructor constructor);
       
    /**
     * Emit a list of type arg names if any
     * @param target Target for output
     * @param typeArguments list of type argument names
     */
    public void emitParameterizedTypeName(Target target, List<String> typeArguments);
}