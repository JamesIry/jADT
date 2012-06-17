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

import static com.pogofish.jadt.ast.SemanticError.*;
import static com.pogofish.jadt.printer.SemanticErrorPrinter.*;
import static org.junit.Assert.*;

import org.junit.Test;

/**
 * Make sure SemanticErrors print properly
 * 
 * @author jiry
 */
public class SemanticErrorPrinterTest {
    /**
     * Cobertura isn't happy unless the (implicit) constructor is called. This
     * stupid test does exactly that
     */
    @Test
    public void constructorTest() {
        final SemanticErrorPrinter printer = new SemanticErrorPrinter();
        assertFalse(printer.toString().isEmpty());
    }
    
    @Test
    public void test() {
        assertEquals("Cannot have two datatypes named foo in one jADT document.", print(_DuplicateDataType("foo")));
        assertEquals("Data type foo cannot have a constructor with the name foo.  Only single constructor data types may have constructors with the same name.", print(_ConstructorDataTypeConflict("foo")));
        assertEquals("Data type foo cannot have multiple constructors named bar.", print(_DuplicateConstructor("foo", "bar")));
        assertEquals("Duplicated arg name baz in constructor bar of data type foo.", print(_DuplicateArgName("foo", "bar", "baz")));
        assertEquals("Duplicated modifier quux on arg name baz in constructor bar of data type foo.", print(_DuplicateModifier("foo", "bar", "baz", "quux")));
    }
}
