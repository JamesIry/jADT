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
package pogofish.jadt.printer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static pogofish.jadt.ast.PrimitiveType.*;
import static pogofish.jadt.ast.RefType._ArrayType;
import static pogofish.jadt.ast.RefType._ClassType;
import static pogofish.jadt.ast.Type._Primitive;
import static pogofish.jadt.ast.Type._Ref;
import static pogofish.jadt.printer.Printer.print;
import static pogofish.jadt.util.Util.list;

import org.junit.Test;

import pogofish.jadt.ast.*;
import pogofish.jadt.util.Util;

/**
 * Test the pretty Printer.  Does not prove that the output is in fact pretty.
 *
 * @author jiry
 */
public class PrinterTest {
    /**
     * Cobertura isn't happy unless the (implicit) constructor is called. This
     * stupid test does exactly that
     */
    @Test
    public void constructorTest() {
        final Printer printer = new Printer();
        assertFalse(printer.toString().isEmpty());
    }

    /**
     * Test that the primitive types print properly
     */
    @Test
    public void testPrimitiveTypes() {
        assertEquals("boolean", print(_Primitive(_BooleanType)));
        assertEquals("char", print(_Primitive(_CharType)));
        assertEquals("short", print(_Primitive(_ShortType)));
        assertEquals("int", print(_Primitive(_IntType)));
        assertEquals("long", print(_Primitive(_LongType)));
        assertEquals("float", print(_Primitive(_FloatType)));
        assertEquals("double", print(_Primitive(_DoubleType)));
    }

    /**
     * Test that class types print properly
     */
    @Test
    public void testClassTypes() {
        // simple class type
        assertEquals("String", print(_Ref(_ClassType("String", Util.<RefType> list()))));
        // parameterized class type
        assertEquals("List<String>", print(_Ref(_ClassType("List",
                list(_ClassType("String", Util.<RefType> list()))))));
        // very complicated class type
        assertEquals("Map<String, List<List<String>>>", print(_Ref(_ClassType("Map",
                list(_ClassType("String", Util.<RefType> list()), _ClassType("List", list((_ClassType("List",
                        list(_ClassType("String", Util.<RefType> list())))))))))));
    }

    /**
     * Test that array types print properly
     */
    @Test
    public void testArrayTypes() {
        // single level
        assertEquals("boolean[]", print(_Ref(_ArrayType(_Primitive(_BooleanType)))));
        // double level
        assertEquals("String[][]",
                print(_Ref(_ArrayType(_Ref(_ArrayType(_Ref(_ClassType("String", Util.<RefType> list()))))))));
    }

    /**
     * Test that args print properly
     */
    @Test
    public void testArg() {      
        assertEquals("boolean[] Foo", print(new Arg(_Ref(_ArrayType(_Primitive(_BooleanType))), "Foo")));
    }

    /**
     * Test that constructors print properly
     */
    @Test
    public void testConstructors() {
        // no arg constructor
        assertEquals("Foo", print(new Constructor("Foo", Util.<Arg> list())));
        // constructor with args
        assertEquals("Foo(boolean hello, int World)", print(new Constructor("Foo", list(new Arg(
                _Primitive(_BooleanType), "hello"), new Arg(_Primitive(_IntType), "World")))));
    }

    /**
     * Test that datatypes print properly
     */
    @Test
    public void testDataTypes() {
        assertEquals("Foo =\n" + "    Bar\n" + "  | Baz", print(new DataType("Foo", list(new Constructor("Bar",
                Util.<Arg> list()), new Constructor("Baz", Util.<Arg> list())))));
    }

    /**
     * Test that docs print properly
     */
    @Test
    public void testDoc() {
        // empty doc
        assertEquals("", print(new Doc("PrinterTest", "", Util.<String> list(), Util.<DataType> list())));
        // package
        assertEquals("package some.package\n\n",
                print(new Doc("PrinterTest", "some.package", Util.<String> list(), Util.<DataType> list())));
        // imports
        assertEquals("import number.one\nimport number.two\n\n",
                print(new Doc("PrinterTest", "", list("number.one", "number.two"), Util.<DataType> list())));
        // package and imports
        assertEquals("package some.package\n\nimport number.one\nimport number.two\n\n", print(new Doc(
                "PrinterTest", "some.package", list("number.one", "number.two"), Util.<DataType> list())));
        // package, imports and datatypes
        assertEquals("package some.package\n\nimport number.one\nimport number.two\n\nFoo =\n    Bar\n", print(new Doc(
                "PrinterTest", "some.package", list("number.one", "number.two"), list(new DataType("Foo",
                        list(new Constructor("Bar", Util.<Arg> list())))))));
    }
}
