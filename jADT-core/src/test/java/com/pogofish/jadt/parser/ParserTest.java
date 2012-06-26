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

import static com.pogofish.jadt.ast.Arg._Arg;
import static com.pogofish.jadt.ast.ArgModifier._Final;
import static com.pogofish.jadt.ast.Constructor._Constructor;
import static com.pogofish.jadt.ast.DataType._DataType;
import static com.pogofish.jadt.ast.PrimitiveType._BooleanType;
import static com.pogofish.jadt.ast.PrimitiveType._CharType;
import static com.pogofish.jadt.ast.PrimitiveType._DoubleType;
import static com.pogofish.jadt.ast.PrimitiveType._FloatType;
import static com.pogofish.jadt.ast.PrimitiveType._IntType;
import static com.pogofish.jadt.ast.PrimitiveType._LongType;
import static com.pogofish.jadt.ast.PrimitiveType._ShortType;
import static com.pogofish.jadt.ast.RefType._ArrayType;
import static com.pogofish.jadt.ast.RefType._ClassType;
import static com.pogofish.jadt.ast.SyntaxError._UnexpectedToken;
import static com.pogofish.jadt.ast.Type._Primitive;
import static com.pogofish.jadt.ast.Type._Ref;
import static com.pogofish.jadt.util.Util.list;
import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;

import com.pogofish.jadt.ast.Arg;
import com.pogofish.jadt.ast.ArgModifier;
import com.pogofish.jadt.ast.Constructor;
import com.pogofish.jadt.ast.DataType;
import com.pogofish.jadt.ast.Doc;
import com.pogofish.jadt.ast.ParseResult;
import com.pogofish.jadt.ast.RefType;
import com.pogofish.jadt.ast.SyntaxError;
import com.pogofish.jadt.parser.StandardParser.Impl;
import com.pogofish.jadt.source.StringSource;
import com.pogofish.jadt.util.Util;

/**
 * Test the StandardParser, mostly by probing its Impl
 * 
 * @author jiry
 */
public class ParserTest {

    /**
     * In order to zero in on specific sections of the parser it's easier to
     * poke at the Impl than try to parse whole documents and pull them apart
     * 
     * @param text
     * @return
     */
    private Impl parserImpl(final String text) {
        final StringSource source = new StringSource("ParserTest", text);
        return new StandardParser.Impl(new Tokenizer(source.getSrcInfo(),
                source.createReader()));
    }

    /**
     * Make sure primitives parse correctly
     */
    @Test
    public void testPrimitive() {
        assertEquals(_BooleanType(), parserImpl("boolean").primitiveType());
        assertEquals(_ShortType(), parserImpl("short").primitiveType());
        assertEquals(_CharType(), parserImpl("char").primitiveType());
        assertEquals(_IntType(), parserImpl("int").primitiveType());
        assertEquals(_LongType(), parserImpl("long").primitiveType());
        assertEquals(_DoubleType(), parserImpl("double").primitiveType());
        assertEquals(_FloatType(), parserImpl("float").primitiveType());
        // make sure noise returns a null
        assertEquals(null, parserImpl("flurbis").primitiveType());
    }

    /**
     * Make sure class types parse correctly
     */
    @Test
    public void testClassType() {
        assertEquals(_ClassType("Foo", Util.<RefType> list()),
                parserImpl("Foo").classType());
        assertEquals(_ClassType("pkg.Foo", Util.<RefType> list()),
                parserImpl("pkg.Foo").classType());
        assertEquals(
                _ClassType("Foo",
                        list(_ClassType("Bar", Util.<RefType> list()))),
                parserImpl("Foo<Bar>").classType());
        assertEquals(
                _ClassType("Foo", list(_ArrayType(_Primitive(_IntType())))),
                parserImpl("Foo<int[]>").classType());
        assertEquals(
                _ClassType(
                        "Foo",
                        list(_ClassType("Bar", Util.<RefType> list()),
                                _ClassType("Baz", Util.<RefType> list()))),
                parserImpl("Foo<Bar, Baz>").classType());
    }

    /**
     * Make sure the array function wraps types in array wrappers correctly
     * based on the number [] pairs in the tokenizer stream
     */
    @Test
    public void testArray() {
        assertEquals(_Primitive(_IntType()),
                parserImpl(" whatever").array(_Primitive(_IntType())));
        assertEquals(_Ref(_ArrayType(_Primitive(_IntType()))), parserImpl("[]")
                .array(_Primitive(_IntType())));
        assertEquals(
                _Ref(_ArrayType(_Ref(_ArrayType(_Primitive(_IntType()))))),
                parserImpl("[][]").array(_Primitive(_IntType())));
    }

    /**
     * Make sure types parse properly
     */
    @Test
    public void testType() {
        assertEquals(_Primitive(_IntType()), parserImpl("int").type());
        assertEquals(_Ref(_ClassType("Foo", Util.<RefType> list())),
                parserImpl("Foo").type());
        assertEquals(_Ref(_ArrayType(_Primitive(_IntType()))),
                parserImpl("int[]").type());
        assertEquals(
                _Ref(_ArrayType(_Ref(_ClassType("Foo", Util.<RefType> list())))),
                parserImpl("Foo[]").type());
        
    }

    /**
     * Make sure ref types parse properly
     */
    @Test
    public void testRefType() {
        assertEquals(_ClassType("Foo", Util.<RefType> list()),
                parserImpl("Foo").refType());
        assertEquals(_ArrayType(_Primitive(_IntType())), parserImpl("int[]")
                .refType());
        assertEquals(
                _ArrayType(_Ref(_ClassType("Foo", Util.<RefType> list()))),
                parserImpl("Foo[]").refType());
        
        final Impl p1 = parserImpl("Foo[");
        checkError(list(_UnexpectedToken("']'", "<EOF>", 1)), _ArrayType(_Ref(_ClassType("Foo", Util.<RefType> list()))), p1.refType(), p1);

        final Impl p2 = parserImpl("Foo<int>");
        checkError(list(_UnexpectedToken("an array or class type", "'int'", 1)), _Ref(_ClassType("Foo", list(_ClassType("BAD_CLASS_int@1", Util.<RefType>list())))), p2.type(), p2);

        final Impl p3 = parserImpl("Foo<A");
        checkError(list(_UnexpectedToken("'>'", "<EOF>", 1)), _Ref(_ClassType("Foo", list(_ClassType("A", Util.<RefType>list())))), p3.type(), p3);

        final Impl p4 = parserImpl("Foo<A B>");
        checkError(list(_UnexpectedToken("'>'", "'B'", 1)), _Ref(_ClassType("Foo", list(_ClassType("A", Util.<RefType>list()), _ClassType("B", Util.<RefType>list())))), p4.type(), p4);

        final Impl p5 = parserImpl("");
        checkError(list(_UnexpectedToken("a class name", "<EOF>", 1)), _Ref(_ClassType("NO_IDENTIFIER@1", Util.<RefType>list())), p5.type(), p5);
        
        final Impl p6 = parserImpl("import");
        checkError(list(_UnexpectedToken("a class name", "'import'", 1)), _Ref(_ClassType("BAD_IDENTIFIER_import@1", Util.<RefType>list())), p6.type(), p6);
        
    }

    /**
     * Make sure arg modifiers parse properly
     */
    @Test
    public void testArgModifier() {
        assertEquals(_Final(), parserImpl("final").argModifier());
        assertEquals(null, parserImpl("").argModifier());
        assertEquals(null, parserImpl("int").argModifier());
    }

    /**
     * Make sure list of arg modifiers parse properly
     */
    @Test
    public void testArgModifiers() {
        assertEquals(list(_Final()), parserImpl("final int").argModifiers());
        assertEquals(list(_Final(), _Final()), parserImpl("final final int")
                .argModifiers());
        assertEquals(Util.<ArgModifier> list(), parserImpl("").argModifiers());
        assertEquals(Util.<ArgModifier> list(), parserImpl("int")
                .argModifiers());
    }

    /**
     * Make sure args parse properly
     */
    @Test
    public void testArg() {
        assertEquals(
                _Arg(Util.<ArgModifier> list(), _Primitive(_IntType()), "Foo"),
                parserImpl("int Foo").arg());
        assertEquals(_Arg(list(_Final()), _Primitive(_IntType()), "Foo"),
                parserImpl("final int Foo").arg());
        
        Impl p1 = parserImpl("int");
        checkError(list(_UnexpectedToken("an argument name", "<EOF>", 1)), _Arg(Util.<ArgModifier>list(), _Primitive(_IntType()), "NO_ARG_NAME@1"), p1.arg(), p1);

        Impl p2 = parserImpl("int boolean");
        checkError(list(_UnexpectedToken("an argument name", "'boolean'", 1)), _Arg(Util.<ArgModifier>list(), _Primitive(_IntType()), "BAD_ARG_NAME_boolean@1"), p2.arg(), p2);

    }

    /**
     * Make sure an arg list parses properly
     */
    @Test
    public void testArgs() {
        assertEquals(Util.<Arg>list(), parserImpl("").args());
        assertEquals(
                list(_Arg(Util.<ArgModifier> list(), _Primitive(_IntType()),
                        "Foo")), parserImpl("(int Foo)").args());
        assertEquals(
                list(_Arg(Util.<ArgModifier> list(), _Primitive(_IntType()),
                        "Foo"),
                        _Arg(Util.<ArgModifier> list(),
                                _Primitive(_BooleanType()), "Bar")),
                parserImpl("(int Foo, boolean Bar)").args());
        
        Impl p1 = parserImpl("(int Foo");
        checkError(list(_UnexpectedToken("')'", "<EOF>", 1)), list(_Arg(Util.<ArgModifier>list(), _Primitive(_IntType()), "Foo")), p1.args(), p1);
        
        Impl p2 = parserImpl("()");
        checkError(list(_UnexpectedToken("a class name", "')'", 1)), list(_Arg(Util.<ArgModifier>list(), _Ref(_ClassType("NO_IDENTIFIER@1", Util.<RefType>list())), "NO_ARG_NAME@2")), p2.args(), p2);

        Impl p3 = parserImpl("(int Foo,)");
        checkError(list(_UnexpectedToken("a class name", "')'", 1)), list(_Arg(Util.<ArgModifier> list(), _Primitive(_IntType()),
                "Foo"), _Arg(Util.<ArgModifier>list(), _Ref(_ClassType("NO_IDENTIFIER@1", Util.<RefType>list())), "NO_ARG_NAME@2")), p3.args(), p3);
        
        Impl p4 = parserImpl("(int Foo int Bar)");
        checkError(list(_UnexpectedToken("')'", "'int'", 1)), list(_Arg(Util.<ArgModifier> list(), _Primitive(_IntType()),
                "Foo"), _Arg(Util.<ArgModifier> list(), _Primitive(_IntType()), "Bar")), p4.args(), p4);
    }

    /**
     * Make sure a constructor parses properly
     */
    @Test
    public void testConstructor() {
        // no arg
        assertEquals(_Constructor("Foo", Util.<Arg> list()), parserImpl("Foo")
                .constructor());
        // args
        assertEquals(
                _Constructor(
                        "Foo",
                        list(_Arg(Util.<ArgModifier> list(),
                                _Primitive(_IntType()), "Bar"))),
                parserImpl("Foo(int Bar)").constructor());
        
        Impl p1 = parserImpl("");
        checkError(list(_UnexpectedToken("a constructor name", "<EOF>", 1)), _Constructor("NO_CONSTRUCTOR_NAME@1", Util.<Arg>list()), p1.constructor(), p1);
    }

    /**
     * Make sure a contructor list parses properly
     */
    @Test
    public void testConstructors() {
        assertEquals(list(_Constructor("Foo", Util.<Arg> list())),
                parserImpl("Foo").constructors());
        assertEquals(
                list(_Constructor("Foo", Util.<Arg> list()),
                        _Constructor("Bar", Util.<Arg> list())),
                parserImpl("Foo|Bar").constructors());

        final Impl p1 = parserImpl("Foo|");
        checkError(list(_UnexpectedToken("a constructor name", "<EOF>", 1)), 
                list(_Constructor("Foo", Util.<Arg>list()), _Constructor("NO_CONSTRUCTOR_NAME@1", Util.<Arg>list())), p1.constructors(), p1);

        final Impl p2 = parserImpl("Foo||Bar");
        checkError(list(_UnexpectedToken("a constructor name", "'|'", 1)), 
                list(_Constructor("Foo", Util.<Arg>list()), _Constructor("NO_CONSTRUCTOR_NAME@1", Util.<Arg>list()), _Constructor("Bar", Util.<Arg>list())), p2.constructors(), p2);
    }

    /**
     * Make sure datatypes parse properly
     */
    @Test
    public void testDataType() {
        assertEquals(
                _DataType("Foo", Util.<String> list(),
                        list(_Constructor("Foo", Util.<Arg> list()))),
                parserImpl("Foo=Foo").dataType());
        assertEquals(
                _DataType("Foo", list("A"),
                        list(_Constructor("Foo", Util.<Arg> list()))),
                parserImpl("Foo<A>=Foo").dataType());
        assertEquals(
                _DataType("Foo", list("A", "B"),
                        list(_Constructor("Foo", Util.<Arg> list()))),
                parserImpl("Foo<A, B>=Foo").dataType());
        
        final Impl p1 = parserImpl("boolean = Foo");
        checkError(list(_UnexpectedToken("a data type name", "'boolean'", 1)), _DataType("BAD_DATA_TYPE_NAME_boolean@1", Util.<String>list(), list(_Constructor("Foo", Util.<Arg>list()))), p1.dataType(), p1);
 
        final Impl p2 = parserImpl("= Foo");
        checkError(list(_UnexpectedToken("a data type name", "'='", 1)), _DataType("NO_DATA_TYPE_NAME@1", Util.<String>list(), list(_Constructor("Foo", Util.<Arg>list()))), p2.dataType(), p2);
 
        final Impl p3 = parserImpl("Bar Foo");
        checkError(list(_UnexpectedToken("'='", "'Foo'", 1)), _DataType("Bar", Util.<String>list(), list(_Constructor("Foo", Util.<Arg>list()))), p3.dataType(), p3);

        final Impl p4 = parserImpl("");
        checkError(list(_UnexpectedToken("a data type name", "<EOF>", 1)), _DataType("NO_DATA_TYPE_NAME@1", Util.<String>list(), list(_Constructor("NO_CONSTRUCTOR_NAME@2", Util.<Arg>list()))), p4.dataType(), p4);

        final Impl p5 = parserImpl("Bar<A, = Foo");
        checkError(list(_UnexpectedToken("a type parameter", "'='", 1)), _DataType("Bar", list("A", "NO_TYPE_ARGUMENT@1"), list(_Constructor("Foo", Util.<Arg>list()))), p5.dataType(), p5);
 
    }

    /**
     * Make sure a datatype list parses properly
     */
    @Test
    public void testDataTypes() {
        assertEquals(
                list(_DataType("Foo", Util.<String> list(),
                        list(_Constructor("Foo", Util.<Arg> list())))),
                parserImpl("Foo=Foo").dataTypes());
        assertEquals(
                list(_DataType("Foo", Util.<String> list(),
                        list(_Constructor("Foo", Util.<Arg> list()))),
                        _DataType("Bar", Util.<String> list(),
                                list(_Constructor("Bar", Util.<Arg> list())))),
                parserImpl("Foo=Foo Bar = Bar").dataTypes());
    }

    /**
     * Make sure type arguments on data types parse properly
     */
    @Test
    public void testTypeArguments() {
        assertEquals("Invalid parse of empty type arguments",
                Util.<String> list(), parserImpl("").typeArguments());
        assertEquals("Invalid parse of a single type argument", list("A"),
                parserImpl("<A>").typeArguments());
        assertEquals("Invalid parse of mulitple type arguments",
                list("A", "B", "C"), parserImpl("<A,B, C>").typeArguments());
        
        Impl p1 = parserImpl("<>");
        checkError(list(_UnexpectedToken("a type parameter", "'>'", 1)), list("NO_TYPE_ARGUMENT@1"), p1.typeArguments(), p1);

        Impl p2 = parserImpl("<A");
        checkError(list(_UnexpectedToken("'>'", "<EOF>", 1)), list("A"), p2.typeArguments(), p2);

        Impl p3 = parserImpl("<");
        checkError(list(_UnexpectedToken("a type parameter", "<EOF>", 1)), list("NO_TYPE_ARGUMENT@1"), p3.typeArguments(), p3);

        Impl p4 = parserImpl("<boolean, A>");
        checkError(list(_UnexpectedToken("a type parameter", "'boolean'", 1)), list("BAD_TYPE_ARGUMENT_boolean@1", "A"), p4.typeArguments(), p4);

        Impl p5 = parserImpl("<A, ,B>");
        checkError(list(_UnexpectedToken("a type parameter", "','", 1)), list("A", "NO_TYPE_ARGUMENT@1", "B"), p5.typeArguments(), p5);

        Impl p6 = parserImpl("<A B>");
        checkError(list(_UnexpectedToken("'>'", "'B'", 1)), list("A", "B"), p6.typeArguments(), p6);

    }

    /**
     * Make sure a package declaration parse properly
     */
    @Test
    public void testPackage() {
        assertEquals("", parserImpl("").pkg());
        assertEquals("", parserImpl("frog").pkg());
        assertEquals("hello", parserImpl("package hello").pkg());
        assertEquals("hello.world", parserImpl("package hello.world").pkg());
        
        final Impl p1 = parserImpl("package");
        checkError(list(_UnexpectedToken("a package name", "<EOF>", 1)), "NO_IDENTIFIER@1", p1.pkg(), p1);

        final Impl p2 = parserImpl("package foo.bar.");
        checkError(list(_UnexpectedToken("a package name", "<EOF>", 1)), "foo.bar.NO_IDENTIFIER@1", p2.pkg(), p2);

        final Impl p3 = parserImpl("package ?g42");
        checkError(list(_UnexpectedToken("a package name", "'?g42'", 1)), "BAD_IDENTIFIER_?g42@1", p3.pkg(), p3);

        final Impl p4 = parserImpl("package boolean");
        checkError(list(_UnexpectedToken("a package name", "'boolean'", 1)), "BAD_IDENTIFIER_boolean@1", p4.pkg(), p4);
    }

    /**
     * Make sure a package list parses properly
     */
    @SuppressWarnings("unchecked")
    // warning in list generation on first line because generic types blah blah
    @Test
    public void testImports() {
        assertEquals(Util.<List<String>> list(), parserImpl("").imports());
        assertEquals(list("hello"), parserImpl("import hello").imports());
        assertEquals(list("hello", "oh.yeah"),
                parserImpl("import hello import oh.yeah").imports());
        
        final Impl p1 = parserImpl("import");
        checkError(list(_UnexpectedToken("a package name", "<EOF>", 1)), list("NO_IDENTIFIER@1"), p1.imports(), p1);
        
        final Impl p2 = parserImpl("import ?g42");
        checkError(list(_UnexpectedToken("a package name", "'?g42'", 1)), list("BAD_IDENTIFIER_?g42@1"), p2.imports(), p2);
        
        final Impl p3 = parserImpl("import boolean");
        checkError(list(_UnexpectedToken("a package name", "'boolean'", 1)), list("BAD_IDENTIFIER_boolean@1"), p3.imports(), p3);       

        final Impl p4 = parserImpl("import import boolean");
        checkError(list(_UnexpectedToken("a package name", "'import'", 1)), list("BAD_IDENTIFIER_import@1"), p4.imports(), p4);       

        final Impl p5 = parserImpl("import package boolean");
        checkError(list(_UnexpectedToken("a package name", "'package'", 1)), list("BAD_IDENTIFIER_package@1"), p5.imports(), p5);       
    }


    private static <A> void checkError(List<SyntaxError> expectedErrors, A expectedResult, A actualResult, Impl p) {
        assertEquals(expectedErrors, p.errors);
        assertEquals(expectedResult, actualResult);
    }

    /**
     * Test the whole shebang with a minimal document
     */
    @Test
    public void testMinimal() {
        final Parser parser = new StandardParser();
        final ParseResult result = parser.parse(new StringSource("ParserTest",
                "Foo = Foo"));

        assertEquals(new ParseResult(new Doc("ParserTest", "", Util
                .<String> list(), list(_DataType("Foo", Util.<String> list(),
                list(_Constructor("Foo", Util.<Arg> list()))))), Util.<SyntaxError>list()), result);
    }

    /**
     * Test the whole shebang with a less minimal document
     */
    @Test
    public void testFull() {
        final Parser parser = new StandardParser();
        final String source = "//a start comment\npackage hello.world /* here are some imports */import wow.man import flim.flam "
                + "FooBar = foo | bar(int hey, final String[] yeah) whatever = whatever";
        final ParseResult result = parser.parse(new StringSource("ParserTest",
                source));

        assertEquals(
                new ParseResult(new Doc(
                        "ParserTest",
                        "hello.world",
                        list("wow.man", "flim.flam"),
                        list(new DataType(
                                "FooBar",
                                Util.<String> list(),
                                Util.list(
                                        new Constructor("foo", Util
                                                .<Arg> list()),
                                        new Constructor(
                                                "bar",
                                                list(new Arg(Util
                                                        .<ArgModifier> list(),
                                                        _Primitive(_IntType()),
                                                        "hey"),
                                                        new Arg(
                                                                list(_Final()),
                                                                _Ref(_ArrayType(_Ref(_ClassType(
                                                                        "String",
                                                                        Util.<RefType> list())))),
                                                                "yeah"))))),
                                new DataType("whatever", Util.<String> list(),
                                        list(new Constructor("whatever", Util
                                                .<Arg> list()))))), Util.<SyntaxError>list()), result);
    }

    /**
     * Test the whole shebang with an error
     */
    @Test
    public void testError() {
        final Parser parser = new StandardParser();
        final String source = "//a start comment\npackage hello.world /* here are some imports */import wow.man import flim.flam "
                + "FooBar = foo | bar(int hey, final String[] yeah) whatever = int";
        final ParseResult result = parser.parse(new StringSource("ParserTest",
                source));

        assertEquals(
                new ParseResult(new Doc(
                        "ParserTest",
                        "hello.world",
                        list("wow.man", "flim.flam"),
                        list(new DataType(
                                "FooBar",
                                Util.<String> list(),
                                Util.list(
                                        new Constructor("foo", Util
                                                .<Arg> list()),
                                        new Constructor(
                                                "bar",
                                                list(new Arg(Util
                                                        .<ArgModifier> list(),
                                                        _Primitive(_IntType()),
                                                        "hey"),
                                                        new Arg(
                                                                list(_Final()),
                                                                _Ref(_ArrayType(_Ref(_ClassType(
                                                                        "String",
                                                                        Util.<RefType> list())))),
                                                                "yeah"))))),
                                new DataType("whatever", Util.<String> list(),
                                        list(new Constructor("BAD_CONSTRUCTOR_NAME_int@1", Util
                                                .<Arg> list()))))), list(SyntaxError._UnexpectedToken("a constructor name", "'int'", 2))), result);
                
    }
}
