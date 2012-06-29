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

import java.util.List;

import com.pogofish.jadt.ast.Arg;
import com.pogofish.jadt.ast.ArgModifier;
import com.pogofish.jadt.ast.Constructor;
import com.pogofish.jadt.ast.DataType;
import com.pogofish.jadt.ast.Doc;
import com.pogofish.jadt.ast.PrimitiveType;
import com.pogofish.jadt.ast.RefType;
import com.pogofish.jadt.ast.SyntaxError;
import com.pogofish.jadt.ast.Type;

/**
 * Interface for internal parser implementations used by the StandardParser
 * 
 * @author jiry
 */
public interface StandardParserImpl {

    /**
     * Parses a complete document which is pkg imports dataTypes
     * 
     * @return Doc
     */
    public abstract Doc doc() throws Exception;

    /** 
     * Gets an optional package declaration "pacakage" packageName
     * 
     * @return String the package name, or, because package is optional returns an empty string ""
     */
    public abstract String pkg() throws Exception;

    /**
     * Parses an optional list of imports which is ("import" packageName)*
     * 
     * @return List<String> possibly empty list of imports
     */
    public abstract List<String> imports() throws Exception;

    /**
     * Parses a required list of datatypes which is dataType (dataType)*
     * 
     * @return List<DataType> non empty list of DataTypes
     */
    public abstract List<DataType> dataTypes() throws Exception;

    /**
     * Parses a dataType which is (name typeArguments? "=" constructors)
     * 
     * @return DataType
     */
    public abstract DataType dataType() throws Exception;

    /**
     * Parses an optional type arguments which is ('<' typeArgument (',' typeArgument)* '>')?
     * 
     * @return list of string names of type arguments. Empty if none
     */
    public abstract List<String> typeArguments() throws Exception;

    /**
     * A required type argument which is just any identifier
     */
    public abstract String typeArgument() throws Exception;

    /**
     * Parses a required list of constructors which is constructor ("|" constructor)*
     * 
     * @return List<Constructor> non empty List of constructors
     */
    public abstract List<Constructor> constructors() throws Exception;

    /**
     * Parses a required constructor which is constructorName args
     * 
     * @return Constructor
     */
    public abstract Constructor constructor() throws Exception;

    /** 
     * Parses an optional list of constructor args which is ("(" arg ("," arg)* ")")?
     * 
     * @return List<Arg> non-empty list of args
     */
    public abstract List<Arg> args() throws Exception;

    /**
     * parses a single required constructor argument which is a list of modifiers followed by a type followed by a name
     * 
     * @return Arg
     */
    public abstract Arg arg() throws Exception;

    /**
     * Parses a possibly empty list of arg modifiers
     */
    public abstract List<ArgModifier> argModifiers() throws Exception;

    /**
     * Parses one optional arg modifier or null if there isn't one
     */
    public abstract ArgModifier argModifier() throws Exception;

    /** 
     * Returns a required class or array type, giving a syntax error on a primitive
     * 
     * @return a RefType
     */
    public abstract RefType refType() throws Exception;

    /**
     * Returns a required type where a type is a primitive or classType wrapped in an array
     * 
     * @return Type
     */
    public abstract Type type() throws Exception;

    /**
     * Returns a required classType, which is className ("<" refType ("," refType)* ">")?
     * 
     * @return RefType
     */
    public abstract RefType classType() throws Exception;

    /**
     * Returns a class name which is either an identifier or a dotted identifier
     */
    public abstract String className() throws Exception;

    /**
     * Returns an identifier that may have dots in it, e.g a fully qualified class name or a package name
     */
    public abstract String dottedIdentifier(String expected) throws Exception;

    /** 
     * Optionally recognizes and returns any of the primitive types
     * 
     * @return PrimitiveType or null if the next token isn't a primitive type
     */
    public abstract PrimitiveType primitiveType() throws Exception;

    /**
     * Errors that occured during parsing
     */
    public abstract List<SyntaxError> errors();

}