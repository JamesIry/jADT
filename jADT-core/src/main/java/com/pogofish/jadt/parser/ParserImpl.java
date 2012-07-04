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
import com.pogofish.jadt.ast.CommentedIdentifier;
import com.pogofish.jadt.ast.Constructor;
import com.pogofish.jadt.ast.DataType;
import com.pogofish.jadt.ast.Doc;
import com.pogofish.jadt.ast.Imprt;
import com.pogofish.jadt.ast.JavaComment;
import com.pogofish.jadt.ast.Pkg;
import com.pogofish.jadt.ast.PrimitiveType;
import com.pogofish.jadt.ast.RefType;
import com.pogofish.jadt.ast.SyntaxError;
import com.pogofish.jadt.ast.Type;

/**
 * Interface for internal parser implementations used by the StandardParser
 * The idea is that StandardParser provides a stateless facaade to the world
 * while ParseImpl may be stateful
 * 
 * @author jiry
 */
public interface ParserImpl {

    /**
     * Get the srcInfo for the source that created this parser
     */
    public abstract String getSrcInfo();
    
    /**
     * A jADT document has an optional package followed by imports and datatypes and ends with an end of file.
     */
    public abstract Doc doc() throws Exception;

    /**
     * The package declaration (if it exists) is the keyword "package" followed by a package name.  No semicolon required.
     */
    public abstract Pkg pkg() throws Exception;

    /**
     *     A jADT document may specify any number (including 0) of package names to import.
     */
    public abstract List<Imprt> imports() throws Exception;

    /**
     * Each import is the keyword "import" followed by a package name.
     */
    public abstract Imprt singleImport() throws Exception;

    /**
     * A package name used in a package or import declaration must be a valid Java package name.
     */
    public abstract String packageName() throws Exception;

    /**
     *     A jADT document must have at least one datatype, but may have as many as you'd like.
     */
    public abstract List<DataType> dataTypes() throws Exception;

    /**
     * Each datatype consists of a name, optional type arguments, "=" and a list of constructors.
     */
    public abstract DataType dataType() throws Exception;

    /**
     * A data type name is any valid Java identifier not qualified with a package.
     */
    public abstract CommentedIdentifier dataTypeName() throws Exception;

    /**
     * A data type's type arguments, if it has them, start with '<', have 1 or more arguments, and end with '>'.
     */
    public abstract List<String> typeArguments() throws Exception;

    /**
     * A single type argument is any valid Java identifier, not qualified with a package.
     */
    public abstract String typeArgument() throws Exception;

    /**
     * A data type must have 1 or more case constructors separated by '|'.
     */
    public abstract List<Constructor> constructors(List<JavaComment> comments) throws Exception;

    /**
     * A case constructor is a name optionally followed by arguments.  If it has no arguments then it must not be followed by '()'.
     */
    public abstract Constructor constructor(List<JavaComment> comments) throws Exception;

    /**
     * A case constructor name is any valid Java identifier not qualified by a package.
     */
    public abstract CommentedIdentifier constructorName() throws Exception;

    /**
     * If a case constructor arguments then they must start with '(', have 1 ore more arguments separated by ',' and end with ')'.
     */
    public abstract List<Arg> args() throws Exception;

    /**
     * A case constructor argument is a list of modifiers, a type, and then a name.
     */
    public abstract Arg arg() throws Exception;

    /**
     * A case constructor argument may have 0 or more modifiers separated by spaces.
     */
    public abstract List<ArgModifier> argModifiers() throws Exception;

    /**
     * Currently the only argument modifier allowed is 'final' but others will follow.
     */
    public abstract ArgModifier argModifier() throws Exception;

    /**
     * An argument name is any valid Java identifier not qualified by a package
     */
    public abstract String argName() throws Exception;

    /**
     * A type is either a class type or a primitive type wrapped in 0 or more levels of array
     */
    public abstract Type type() throws Exception;

    /**
     * A refType is either a primitive type wrapped in one level or array or a class type and in either case
     * may be wrapped in 0 or more additional levels of array
     */
    public abstract RefType refType() throws Exception;

    /**
     * The pair '[]' modifies a previously mentioned type X to make it type X[]
     */
    public abstract void arrayTypeBrackets() throws Exception;

    /**
     * A class type is a class name followed by actual type arguments
     */
    public abstract RefType classType() throws Exception;

    /**
     * A class name is a valid Java class name that may be qualified by a package.
     */
    public abstract String className() throws Exception;

    /**
     * If a type has type arguments then it will be '<' followed by 1 or more refTypes followed by '>'.
     */
    public abstract List<RefType> actualTypeArguments() throws Exception;

    /**
     * A primitive type is any of the standard Java primitive types
     */
    public abstract PrimitiveType primitiveType() throws Exception;

    /**
     * A dotted identifier is a series of 1 or more identifiers separated by '.'.
     */
    public abstract String dottedIdentifier(String expected)
            throws Exception;

    /**
     * A commented identifier may be any valid Java identifier
     * and may be precedeed by java comments
     */
    public abstract CommentedIdentifier commentedIdentifier(String expected)
            throws Exception;

    /**
     * An identifier may be any valid Java identifier
     */
    public abstract String identifier(String expected) throws Exception;

    /**
     * 'import'
     * may be precedeed by java comments
     */
    public abstract List<JavaComment> importKeyword() throws Exception;

    /**
     * 'package'
     * may be precedeed by java comments
     */
    public abstract List<JavaComment> packageKeyword() throws Exception;

    /**
     * 'final'
     */
    public abstract ArgModifier finalKeyword() throws Exception;

    /**
     * 'boolean'
     */
    public abstract PrimitiveType booleanType() throws Exception;

    /**
     * 'byte'
     */
    public abstract PrimitiveType byteType() throws Exception;

    /**
     * 'char'
     */
    public abstract PrimitiveType charType() throws Exception;

    /**
     * 'short'
     */
    public abstract PrimitiveType shortType() throws Exception;

    /**
     * 'int'
     */
    public abstract PrimitiveType intType() throws Exception;

    /**
     * 'long'
     */
    public abstract PrimitiveType longType() throws Exception;

    /**
     * 'float'
     */
    public abstract PrimitiveType floatType() throws Exception;

    /**
     * 'double'
     */
    public abstract PrimitiveType doubleType() throws Exception;

    /**
     * '.'
     */
    public abstract void dot() throws Exception;

    /**
     * ','
     */
    public abstract void comma() throws Exception;

    /**
     * '('
     */
    public abstract void lparen() throws Exception;

    /**
     * ')'
     */
    public abstract void rparen() throws Exception;

    /**
     * '['
     */
    public abstract void lbracket() throws Exception;

    /**
     * ']'
     */
    public abstract void rbracket() throws Exception;

    /**
     * '<'
     */
    public abstract void langle() throws Exception;

    /**
     * '>'
     */
    public abstract void rangle() throws Exception;

    /**
     * '='
     * may be precedeed by Java comments
     */
    public abstract List<JavaComment> equals() throws Exception;

    /**
     * '|'
     * may be precedeed by Java comments
     */
    public abstract List<JavaComment> bar() throws Exception;

    /**
     * end of file
     */
    public abstract void eof() throws Exception;

    /**
     * Errors that occured during parsing
     */
    public abstract List<SyntaxError> errors();
}