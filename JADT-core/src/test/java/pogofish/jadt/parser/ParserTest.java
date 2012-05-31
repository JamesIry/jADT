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
package pogofish.jadt.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static pogofish.jadt.ast.Arg._Arg;
import static pogofish.jadt.ast.Constructor._Constructor;
import static pogofish.jadt.ast.DataType._DataType;
import static pogofish.jadt.ast.PrimitiveType.*;
import static pogofish.jadt.ast.RefType._ArrayType;
import static pogofish.jadt.ast.RefType._ClassType;
import static pogofish.jadt.ast.Type._Primitive;
import static pogofish.jadt.ast.Type._Ref;
import static pogofish.jadt.util.Util.list;

import java.util.List;

import org.junit.Test;

import pogofish.jadt.ast.*;
import pogofish.jadt.parser.StandardParser.Impl;
import pogofish.jadt.source.StringSource;
import pogofish.jadt.util.Util;

/**
 * Test the StandardParser, mostly by probing its Impl
 *
 * @author jiry
 */
public class ParserTest {

    /**
     * In order to zero in on specific sections of the parser it's easier to poke at the
     * Impl than try to parse whole documents and pull them apart
     * @param text
     * @return
     */
    private Impl parserImpl(final String text) {
        return new StandardParser.Impl(new Tokenizer(new StringSource("ParserTest", text)));
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
        assertEquals(_ClassType("Foo", Util.<RefType>list()), parserImpl("Foo").classType());
        assertEquals(_ClassType("package.Foo", Util.<RefType>list()), parserImpl("package.Foo").classType());
        assertEquals(_ClassType("Foo", list(_ClassType("Bar", Util.<RefType>list()))), parserImpl("Foo<Bar>").classType());
        assertEquals(_ClassType("Foo", list(_ArrayType(_Primitive(_IntType())))), parserImpl("Foo<int[]>").classType());
        assertEquals(_ClassType("Foo", list(_ClassType("Bar", Util.<RefType>list()), _ClassType("Baz", Util.<RefType>list()))), parserImpl("Foo<Bar, Baz>").classType());
        try {
            final RefType result = parserImpl("int").classType();
            fail("No syntax exception from primitive, got " + result);
        } catch (SyntaxException e) {            
        }
        try {
            final RefType result = parserImpl("Foo<int>").classType();
            fail("No syntax exception from primitive type parameter, got " + result);
        } catch (SyntaxException e) {            
        }        
        try {
            final RefType result = parserImpl("Foo<Bar Baz").classType();
            fail("No syntax exception from missing right angle bracket, got " + result);
        } catch (SyntaxException e) {            
        }        
    }
    
    /**
     * Make sure the array function wraps types in array wrappers correctly based on the number []
     * pairs in the tokenizer stream
     */
    @Test
    public void testArray() {
        assertEquals(_Primitive(_IntType()), parserImpl(" whatever").array(_Primitive(_IntType())));
        assertEquals(_Ref(_ArrayType(_Primitive(_IntType()))), parserImpl("[]").array(_Primitive(_IntType())));
        assertEquals(_Ref(_ArrayType(_Ref(_ArrayType(_Primitive(_IntType()))))), parserImpl("[][]").array(_Primitive(_IntType())));
        try {
            final Type result = parserImpl("[ whatever").array(_Primitive(_IntType()));
            fail("No syntax exception from missing right square bracket, got " + result);
        } catch (SyntaxException e) {            
        }                
    }
    
    /**
     * Make sure types pare properly
     */
    @Test
    public void testType() {
        assertEquals(_Primitive(_IntType()), parserImpl("int").type());
        assertEquals(_Ref(_ClassType("Foo", Util.<RefType>list())), parserImpl("Foo").type());
        assertEquals(_Ref(_ArrayType(_Primitive(_IntType()))), parserImpl("int[]").type());
        assertEquals(_Ref(_ArrayType(_Ref(_ClassType("Foo", Util.<RefType>list())))), parserImpl("Foo[]").type());
        try {
            final Type result = parserImpl("").type();
            fail("No syntax exception from missing type, got " + result);
        } catch (SyntaxException e) {            
        }                        
    }
    
    /**
     * Make sure ref types parse properly
     */
    @Test
    public void testRefType() {
        assertEquals(_ClassType("Foo", Util.<RefType>list()), parserImpl("Foo").refType());
        assertEquals(_ArrayType(_Primitive(_IntType())), parserImpl("int[]").refType());
        assertEquals(_ArrayType(_Ref(_ClassType("Foo", Util.<RefType>list()))), parserImpl("Foo[]").refType());
        try {
            final RefType result = parserImpl("int").refType();
            fail("No syntax exception from primitive type, got " + result);
        } catch (SyntaxException e) {            
        }                                
    }
    
    /**
     * Make sure args parse properly
     */
    @Test
    public void testArg() {
        assertEquals(_Arg(_Primitive(_IntType()), "Foo"), parserImpl("int Foo").arg());        
        try {
            final Arg result = parserImpl("foo").arg();
            fail("No syntax exception from missing type, got " + result);
        } catch (SyntaxException e) {            
        }                                
        try {
            final Arg result = parserImpl("int").arg();
            fail("No syntax exception from missing name, got " + result);
        } catch (SyntaxException e) {            
        }                                
    }
    
    /**
     * Make sure an arg list parses properly
     */
    @Test
    public void testArgs() {
        assertEquals(list(_Arg(_Primitive(_IntType()), "Foo")), parserImpl("int Foo").args());        
        assertEquals(list(_Arg(_Primitive(_IntType()), "Foo"), _Arg(_Primitive(_BooleanType()), "Bar")), parserImpl("int Foo, boolean Bar").args());        
        try {
            final List<Arg> result = parserImpl("").args();
            fail("No syntax exception from empty arg list, got " + result);
        } catch (SyntaxException e) {            
        }                                
        try {
            final List<Arg> result = parserImpl("int Foo,").args();
            fail("No syntax exception from missing arg after comma, got " + result);
        } catch (SyntaxException e) {            
        }                                
        
    }
    
    /**
     * Make sure a constructor parses properly
     */
    @Test
    public void testConstructor() {
        // no arg
        assertEquals(_Constructor("Foo", Util.<Arg>list()), parserImpl("Foo").constructor());
        // args
        assertEquals(_Constructor("Foo", list(_Arg(_Primitive(_IntType()), "Bar"))), parserImpl("Foo(int Bar)").constructor());
        try {
            final Constructor result = parserImpl("").constructor();
            fail("No syntax exception from empty constructor, got " + result);
        } catch (SyntaxException e) {            
        }                                
        try {
            final Constructor result = parserImpl("Foo()").constructor();
            fail("No syntax exception from missing args, got " + result);
        } catch (SyntaxException e) {            
        }                                
        try {
            final Constructor result = parserImpl("Foo(int Bar").constructor();
            fail("No syntax exception from missing paren, got " + result);
        } catch (SyntaxException e) {            
        }                                
        
    }
    
    /**
     * Make sure a contructor list parses properly
     */
    @Test
    public void testConstructors() {
        assertEquals(list(_Constructor("Foo", Util.<Arg>list())), parserImpl("Foo").constructors());
        assertEquals(list(_Constructor("Foo", Util.<Arg>list()), _Constructor("Bar", Util.<Arg>list())), parserImpl("Foo|Bar").constructors());
        try {
            final List<Constructor> result = parserImpl("").constructors();
            fail("No syntax exception from empty constructor list, got " + result);
        } catch (SyntaxException e) {            
        }                                
        try {
            final List<Constructor> result = parserImpl("Foo|").constructors();
            fail("No syntax exception from missing constructor after bar, got " + result);
        } catch (SyntaxException e) {            
        }                                
    }
    
    /**
     * Make sure datatypes parse properly
     */
    @Test
    public void testDataType() {
        assertEquals(_DataType("Foo", Util.<String>list(), list(_Constructor("Foo", Util.<Arg>list()))), parserImpl("Foo=Foo").dataType());
        assertEquals(_DataType("Foo", list("A"), list(_Constructor("Foo", Util.<Arg>list()))), parserImpl("Foo A=Foo").dataType());
        assertEquals(_DataType("Foo", list("A", "B"), list(_Constructor("Foo", Util.<Arg>list()))), parserImpl("Foo A B=Foo").dataType());
        try {
            final DataType result = parserImpl("").dataType();
            fail("No syntax exception from empty dataType, got " + result);
        } catch (SyntaxException e) {            
        }                                
        try {
            final DataType result = parserImpl("Foo").dataType();
            fail("No syntax exception from missing = , got " + result);
        } catch (SyntaxException e) {            
        }                                
    }
    
    /**
     * Make sure a datatype list parses properly
     */
    @Test
    public void testDataTypes() {
        assertEquals(list(_DataType("Foo", Util.<String>list(), list(_Constructor("Foo", Util.<Arg>list())))), parserImpl("Foo=Foo").dataTypes());
        assertEquals(list(_DataType("Foo", Util.<String>list(), list(_Constructor("Foo", Util.<Arg>list()))), _DataType("Bar", Util.<String>list(), list(_Constructor("Bar", Util.<Arg>list())))), parserImpl("Foo=Foo Bar = Bar").dataTypes());
        try {
            final List<DataType> result = parserImpl("").dataTypes();
            fail("No syntax exception from empty dataType list, got " + result);
        } catch (SyntaxException e) {            
        }
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
        try {
            final String result = parserImpl("package").pkg();
            fail("No syntax exception from missing package name, got " + result);
        } catch (SyntaxException e) {
        }
        try {
            final String result = parserImpl("package foo.bar.").pkg();
            fail("No syntax exception from malformed package name, got " + result);
        } catch (SyntaxException e) {
        }
        try {
            final String result = parserImpl("package ?g42").pkg();
            fail("No syntax exception from bad package name, got " + result);
        } catch (SyntaxException e) {
        }
        try {
            final String result = parserImpl("package boolean").pkg();
            fail("No syntax exception from keyword package name, got " + result);
        } catch (SyntaxException f) {
        }
    }

    /**
     * Make sure a package list parses properly
     */
    @SuppressWarnings("unchecked") // warning in list generation on first line because generic types blah blah
    @Test
    public void testImports() {
        assertEquals(Util.<List<String>>list(), parserImpl("").imports());
        assertEquals(list("hello"),parserImpl("import hello").imports());
        assertEquals(list("hello", "oh.yeah"),parserImpl("import hello import oh.yeah").imports());
        try {
            final List<String> result = parserImpl("import").imports();
            fail("No syntax exception from missing import name, got " + result);
        } catch (SyntaxException e) {
        }
        try {
            final List<String> result = parserImpl("import ?g42").imports();
            fail("No syntax exception from bad import name, got " + result);
        } catch (SyntaxException e) {
        }
        try {
            final List<String> result = parserImpl("import boolean").imports();
            fail("No syntax exception from keyword import name, got " + result);
        } catch (SyntaxException f) {
        }
    }
    
    /**
     * Test the whole shebang with a minimal document
     */
    @Test
    public void testMinimal() {
        final Parser parser = new StandardParser();
        final Doc doc = parser.parse(new StringSource("ParserTest","Foo = Foo"));

        assertEquals(new Doc("ParserTest", "", Util.<String> list(), list(_DataType("Foo", Util.<String>list(), list(_Constructor("Foo", Util.<Arg>list()))))), doc);
    }

    
    /**
     * Test the whole shebang with a less minimal document
     */
    @Test
    public void testFull() {
        final Parser parser = new StandardParser();
        final String source = "//a start comment\npackage hello.world /* here are some imports */import wow.man import flim.flam "
        + "FooBar = foo | bar(int hey, String[] yeah) whatever = whatever";
		final Doc doc = parser.parse(new StringSource("ParserTest", source));

        assertEquals(
                new Doc("ParserTest", "hello.world", list("wow.man", "flim.flam"), list(
                        new DataType("FooBar", Util.<String>list(), Util.list(new Constructor("foo", Util.<Arg> list()), new Constructor(
                                "bar", list(
                                        new Arg(_Primitive(_IntType()), "hey"), 
                                        new Arg(_Ref(_ArrayType(_Ref(_ClassType("String", Util.<RefType>list())))), "yeah"))))), 
                                 new DataType("whatever", Util.<String>list(), list(new Constructor("whatever", Util.<Arg> list()))))), doc);
    }    
}

