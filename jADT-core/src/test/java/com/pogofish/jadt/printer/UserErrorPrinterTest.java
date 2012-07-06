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
package com.pogofish.jadt.printer;

import static com.pogofish.jadt.errors.SemanticError._ConstructorDataTypeConflict;
import static com.pogofish.jadt.errors.SemanticError._DuplicateArgName;
import static com.pogofish.jadt.errors.SemanticError._DuplicateConstructor;
import static com.pogofish.jadt.errors.SemanticError._DuplicateDataType;
import static com.pogofish.jadt.errors.SemanticError._DuplicateModifier;
import static com.pogofish.jadt.errors.SemanticError._TooManyConstructorJavaDocComments;
import static com.pogofish.jadt.errors.SemanticError._TooManyDataTypeJavaDocComments;
import static com.pogofish.jadt.errors.SyntaxError._UnexpectedToken;
import static com.pogofish.jadt.errors.UserError._Semantic;
import static com.pogofish.jadt.errors.UserError._Syntactic;
import static com.pogofish.jadt.printer.UserErrorPrinter.print;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

/**
 * Make sure SemanticErrors print properly
 * 
 * @author jiry
 */
public class UserErrorPrinterTest {
    /**
     * Cobertura isn't happy unless the (implicit) constructor is called. This
     * stupid test does exactly that
     */
    @Test
    public void constructorTest() {
        final UserErrorPrinter printer = new UserErrorPrinter();
        assertFalse(printer.toString().isEmpty());
    }
    
    @Test
    public void test() {
        assertEquals("Found token 'flurb' but expected 'blurb' at line 13.", print(_Syntactic(_UnexpectedToken("blurb", "flurb", 13))));

        assertEquals("Cannot have two datatypes named foo in one jADT document.", print(_Semantic(_DuplicateDataType("foo"))));
        assertEquals("Data type foo cannot have a constructor with the name foo.  Only single constructor data types may have constructors with the same name.", print(_Semantic(_ConstructorDataTypeConflict("foo"))));
        assertEquals("Data type foo cannot have multiple constructors named bar.", print(_Semantic(_DuplicateConstructor("foo", "bar"))));
        assertEquals("Duplicated arg name baz in constructor bar of data type foo.", print(_Semantic(_DuplicateArgName("foo", "bar", "baz"))));
        assertEquals("Duplicated modifier quux on arg name baz in constructor bar of data type foo.", print(_Semantic(_DuplicateModifier("foo", "bar", "baz", "quux"))));
        assertEquals("More than one JavaDoc comment was found on data type foo. jADT supports at most one JavaDoc comment per data type.", print(_Semantic(_TooManyDataTypeJavaDocComments("foo"))));
        assertEquals("More than one JavaDoc comment was found on constructor bar on data type foo. jADT supports at most one JavaDoc comment per data type.", print(_Semantic(_TooManyConstructorJavaDocComments("foo", "bar"))));
    }
}
