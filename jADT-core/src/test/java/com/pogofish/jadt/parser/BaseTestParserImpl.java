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
import com.pogofish.jadt.ast.Imprt;
import com.pogofish.jadt.ast.JavaComment;
import com.pogofish.jadt.ast.Literal;
import com.pogofish.jadt.ast.Pkg;
import com.pogofish.jadt.ast.PrimitiveType;
import com.pogofish.jadt.ast.RefType;
import com.pogofish.jadt.ast.Type;
import com.pogofish.jadt.errors.SyntaxError;

/**
 * Base class for producing test parser impls.  Throws exceptions on all methods
 * 
 * @author jiry
 */
public abstract class BaseTestParserImpl implements ParserImpl {

    public BaseTestParserImpl() {
        super();
    }

    @Override
    public List<String> typeArguments() throws Exception {
        throw new RuntimeException(
                "This should not have been called");
    
    }

    @Override
    public String typeArgument() throws Exception {
        throw new RuntimeException(
                "This should not have been called");
    
    }

    @Override
    public Type type() throws Exception {
        throw new RuntimeException(
                "This should not have been called");
    
    }

    @Override
    public Imprt singleImport() throws Exception {
        throw new RuntimeException(
                "This should not have been called");
    
    }

    @Override
    public PrimitiveType shortType() throws Exception {
        throw new RuntimeException(
                "This should not have been called");
    
    }

    @Override
    public void rparen() throws Exception {
        throw new RuntimeException(
                "This should not have been called");
    
    }

    @Override
    public RefType refType() throws Exception {
        throw new RuntimeException(
                "This should not have been called");
    
    }

    @Override
    public void rbracket() throws Exception {
        throw new RuntimeException(
                "This should not have been called");
    
    }

    @Override
    public void rangle() throws Exception {
        throw new RuntimeException(
                "This should not have been called");
    
    }

    @Override
    public PrimitiveType primitiveType() throws Exception {
        throw new RuntimeException(
                "This should not have been called");
    
    }

    @Override
    public Pkg pkg() throws Exception {
        throw new RuntimeException(
                "This should not have been called");
    
    }

    @Override
    public String packageName() throws Exception {
        throw new RuntimeException(
                "This should not have been called");
    
    }

    @Override
    public String packageSpec() throws Exception {
        throw new RuntimeException(
                "This should not have been called");
    
    }

    @Override
    public List<JavaComment> packageKeyword() throws Exception {
        throw new RuntimeException(
                "This should not have been called");
    
    }

    @Override
    public void lparen() throws Exception {
        throw new RuntimeException(
                "This should not have been called");
    
    }

    @Override
    public PrimitiveType longType() throws Exception {
        throw new RuntimeException(
                "This should not have been called");
    
    }

    @Override
    public void lbracket() throws Exception {
        throw new RuntimeException(
                "This should not have been called");
    
    }

    @Override
    public void langle() throws Exception {
        throw new RuntimeException(
                "This should not have been called");
    
    }

    @Override
    public PrimitiveType intType() throws Exception {
        throw new RuntimeException(
                "This should not have been called");
    
    }

    @Override
    public List<Imprt> imports() throws Exception {
        throw new RuntimeException(
                "This should not have been called");
    
    }

    @Override
    public List<JavaComment> importKeyword() throws Exception {
        throw new RuntimeException(
                "This should not have been called");
    
    }

    @Override
    public String identifier(String expected) throws Exception {
        throw new RuntimeException(
                "This should not have been called");
    
    }

    @Override
    public String getSrcInfo() {
        throw new RuntimeException(
                "This should not have been called");
    
    }

    @Override
    public PrimitiveType floatType() throws Exception {
        throw new RuntimeException(
                "This should not have been called");
    
    }

    @Override
    public ArgModifier finalKeyword() throws Exception {
        throw new RuntimeException(
                "This should not have been called");
    
    }
    
    @Override
    public ArgModifier transientKeyword() throws Exception {
        throw new RuntimeException(
                "This should not have been called");
    
    }

    @Override
    public ArgModifier volatileKeyword() throws Exception {
        throw new RuntimeException(
                "This should not have been called");
    
    }

    @Override
    public List<SyntaxError> errors() {
        throw new RuntimeException(
                "This should not have been called");
    
    }

    @Override
    public List<JavaComment> equals() throws Exception {
        throw new RuntimeException(
                "This should not have been called");
    
    }

    @Override
    public void eof() throws Exception {
        throw new RuntimeException(
                "This should not have been called");
    
    }

    @Override
    public PrimitiveType doubleType() throws Exception {
        throw new RuntimeException(
                "This should not have been called");
    
    }

    @Override
    public String dottedIdentifier(String expected) throws Exception {
        throw new RuntimeException(
                "This should not have been called");
    
    }

    @Override
    public void dot() throws Exception {
        throw new RuntimeException(
                "This should not have been called");
    
    }

    @Override
    public List<DataType> dataTypes() throws Exception {
        throw new RuntimeException(
                "This should not have been called");
    
    }

    @Override
    public CommentedIdentifier dataTypeName() throws Exception {
        throw new RuntimeException(
                "This should not have been called");
    
    }

    @Override
    public DataType dataType() throws Exception {
        throw new RuntimeException(
                "This should not have been called");
    
    }

    @Override
    public List<Constructor> constructors(List<JavaComment> comments) throws Exception {
        throw new RuntimeException(
                "This should not have been called");
    
    }

    @Override
    public CommentedIdentifier constructorName() throws Exception {
        throw new RuntimeException(
                "This should not have been called");
    
    }

    @Override
    public Constructor constructor(List<JavaComment> comments) throws Exception {
        throw new RuntimeException(
                "This should not have been called");
    
    }

    @Override
    public CommentedIdentifier commentedIdentifier(String expected) throws Exception {
        throw new RuntimeException(
                "This should not have been called");
    
    }

    @Override
    public void comma() throws Exception {
        throw new RuntimeException(
                "This should not have been called");
    
    }

    @Override
    public RefType classType() throws Exception {
        throw new RuntimeException(
                "This should not have been called");
    
    }

    @Override
    public String className() throws Exception {
        throw new RuntimeException(
                "This should not have been called");
    
    }

    @Override
    public PrimitiveType charType() throws Exception {
        throw new RuntimeException(
                "This should not have been called");
    
    }

    @Override
    public PrimitiveType byteType() throws Exception {
        throw new RuntimeException(
                "This should not have been called");
    
    }

    @Override
    public PrimitiveType booleanType() throws Exception {
        throw new RuntimeException(
                "This should not have been called");
    
    }

    @Override
    public List<JavaComment> bar() throws Exception {
        throw new RuntimeException(
                "This should not have been called");
    
    }

    @Override
    public void arrayTypeBrackets() throws Exception {
        throw new RuntimeException(
                "This should not have been called");
    
    }

    @Override
    public List<Arg> args() throws Exception {
        throw new RuntimeException(
                "This should not have been called");
    
    }

    @Override
    public String argName() throws Exception {
        throw new RuntimeException(
                "This should not have been called");
    
    }

    @Override
    public List<ArgModifier> argModifiers() throws Exception {
        throw new RuntimeException(
                "This should not have been called");
    
    }

    @Override
    public ArgModifier argModifier() throws Exception {
        throw new RuntimeException(
                "This should not have been called");
    
    }

    @Override
    public Arg arg() throws Exception {
        throw new RuntimeException(
                "This should not have been called");
    
    }

    @Override
    public List<RefType> actualTypeArguments() throws Exception {
        throw new RuntimeException(
                "This should not have been called");
    
    }

    @Override
    public void extendsKeyword() throws Exception {
        throw new RuntimeException(
                "This should not have been called");
    }

    @Override
    public void implementsKeyword() throws Exception {
        throw new RuntimeException(
                "This should not have been called");
    }

    @Override
    public Literal literal() throws Exception {
        throw new RuntimeException(
                "This should not have been called");
    }
    

}