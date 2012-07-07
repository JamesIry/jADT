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

import static com.pogofish.jadt.ast.ASTConstants.EMPTY_PKG;
import static com.pogofish.jadt.ast.ASTConstants.NO_COMMENTS;
import static com.pogofish.jadt.ast.ASTConstants.NO_IMPORTS;
import static com.pogofish.jadt.ast.JDTagSection._JDTagSection;
import static com.pogofish.jadt.ast.JDToken._JDAsterisk;
import static com.pogofish.jadt.ast.JDToken._JDEOL;
import static com.pogofish.jadt.ast.JDToken._JDTag;
import static com.pogofish.jadt.ast.JDToken._JDWhiteSpace;
import static com.pogofish.jadt.ast.JDToken._JDWord;
import static com.pogofish.jadt.ast.JavaComment._JavaDocComment;
import static com.pogofish.jadt.ast.JavaComment._JavaEOLComment;
import static com.pogofish.jadt.ast.JavaComment._JavaMultiLineComment;
import static com.pogofish.jadt.ast.JavaDoc._JavaDoc;
import static com.pogofish.jadt.ast.PrimitiveType._BooleanType;
import static com.pogofish.jadt.ast.PrimitiveType._ByteType;
import static com.pogofish.jadt.ast.PrimitiveType._CharType;
import static com.pogofish.jadt.ast.PrimitiveType._DoubleType;
import static com.pogofish.jadt.ast.PrimitiveType._FloatType;
import static com.pogofish.jadt.ast.PrimitiveType._IntType;
import static com.pogofish.jadt.ast.PrimitiveType._LongType;
import static com.pogofish.jadt.ast.PrimitiveType._ShortType;
import static com.pogofish.jadt.ast.RefType._ArrayType;
import static com.pogofish.jadt.ast.RefType._ClassType;
import static com.pogofish.jadt.ast.Type._Primitive;
import static com.pogofish.jadt.ast.Type._Ref;
import static com.pogofish.jadt.printer.ASTPrinter.print;
import static com.pogofish.jadt.printer.ASTPrinter.printComments;
import static com.pogofish.jadt.util.Util.list;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.util.List;

import org.junit.Test;

import com.pogofish.jadt.ast.Arg;
import com.pogofish.jadt.ast.ArgModifier;
import com.pogofish.jadt.ast.Constructor;
import com.pogofish.jadt.ast.DataType;
import com.pogofish.jadt.ast.Doc;
import com.pogofish.jadt.ast.Imprt;
import com.pogofish.jadt.ast.JDTagSection;
import com.pogofish.jadt.ast.JDToken;
import com.pogofish.jadt.ast.JavaComment;
import com.pogofish.jadt.ast.JavaDoc;
import com.pogofish.jadt.ast.Pkg;
import com.pogofish.jadt.ast.RefType;
import com.pogofish.jadt.util.Util;


/**
 * Test the pretty ASTPrinter.  Does not prove that the output is in fact pretty.
 *
 * @author jiry
 */
public class ASTPrinterTest {
    /**
     * Cobertura isn't happy unless the (implicit) constructor is called. This
     * stupid test does exactly that
     */
    @Test
    public void constructorTest() {
        final ASTPrinter printer = new ASTPrinter();
        assertFalse(printer.toString().isEmpty());
    }

    /**
     * Test that the primitive types print properly
     */
    @Test
    public void testPrimitiveTypes() {
        assertEquals("boolean", print(_Primitive(_BooleanType())));
        assertEquals("byte", print(_Primitive(_ByteType())));
        assertEquals("char", print(_Primitive(_CharType())));
        assertEquals("short", print(_Primitive(_ShortType())));
        assertEquals("int", print(_Primitive(_IntType())));
        assertEquals("long", print(_Primitive(_LongType())));
        assertEquals("float", print(_Primitive(_FloatType())));
        assertEquals("double", print(_Primitive(_DoubleType())));
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
        assertEquals("boolean[]", print(_Ref(_ArrayType(_Primitive(_BooleanType())))));
        // double level
        assertEquals("String[][]",
                print(_Ref(_ArrayType(_Ref(_ArrayType(_Ref(_ClassType("String", Util.<RefType> list()))))))));
    }

    /**
     * Test that args print properly
     */
    @Test
    public void testArg() {      
        assertEquals("boolean[] Foo", print(new Arg(Util.<ArgModifier>list(), _Ref(_ArrayType(_Primitive(_BooleanType()))), "Foo")));
        assertEquals("final boolean[] Foo", print(new Arg(list(ArgModifier._Final()), _Ref(_ArrayType(_Primitive(_BooleanType()))), "Foo")));
        assertEquals("final boolean[] Foo", print(new Arg(list(ArgModifier._Final(), ArgModifier._Final()), _Ref(_ArrayType(_Primitive(_BooleanType()))), "Foo")));
    }

    /**
     * Test that constructors print properly
     */
    @Test
    public void testConstructors() {
        // no arg constructor
        assertEquals("Foo", print(new Constructor(NO_COMMENTS, "Foo", Util.<Arg> list())));
        // constructor with args
        assertEquals("Foo(boolean hello, int World)", print(new Constructor(NO_COMMENTS, "Foo", list(new Arg(
                Util.<ArgModifier>list(), _Primitive(_BooleanType()), "hello"), new Arg(Util.<ArgModifier>list(), _Primitive(_IntType()), "World")))));
    }

    /**
     * Test that datatypes print properly
     */
    @Test
    public void testDataTypes() {
        assertEquals("Foo =\n" + "    Bar\n" + "  | Baz", print(new DataType(NO_COMMENTS, "Foo", Util.<String>list(), list(new Constructor(NO_COMMENTS, "Bar",
                Util.<Arg> list()), new Constructor(NO_COMMENTS, "Baz", Util.<Arg> list())))));
    }

    /**
     * Test that docs print properly
     */
    @Test
    public void testDoc() {
        // empty doc
        assertEquals("", print(new Doc("PrinterTest", EMPTY_PKG, NO_IMPORTS, Util.<DataType> list())));
        // package
        assertEquals("package some.package\n\n",
                print(new Doc("PrinterTest", Pkg._Pkg(NO_COMMENTS, "some.package"), NO_IMPORTS, Util.<DataType> list())));
        // imports
        assertEquals("import number.one\nimport number.two\n\n",
                print(new Doc("PrinterTest", EMPTY_PKG, list(Imprt._Imprt(NO_COMMENTS, "number.one"), Imprt._Imprt(NO_COMMENTS,"number.two")), Util.<DataType> list())));
        // package and imports
        assertEquals("package some.package\n\nimport number.one\nimport number.two\n\n", print(new Doc(
                "PrinterTest", Pkg._Pkg(NO_COMMENTS, "some.package"), list(Imprt._Imprt(NO_COMMENTS, "number.one"), Imprt._Imprt(NO_COMMENTS,"number.two")), Util.<DataType> list())));
        // package, imports and datatypes
        assertEquals("package some.package\n\nimport number.one\nimport number.two\n\nFoo =\n    Bar\n", print(new Doc(
                "PrinterTest", Pkg._Pkg(NO_COMMENTS, "some.package"), list(Imprt._Imprt(NO_COMMENTS, "number.one"), Imprt._Imprt(NO_COMMENTS,"number.two")), list(new DataType(NO_COMMENTS, "Foo", Util.<String>list(), 
                        list(new Constructor(NO_COMMENTS, "Bar", Util.<Arg> list())))))));
    }

    private static final JDToken ONEEOL = _JDEOL("\n");
    private static final JDToken ONEWS = _JDWhiteSpace(" ");
    private static final List<JDToken> NO_TOKENS = Util.<JDToken>list();
    private static final List<JDTagSection> NO_TAG_SECTIONS = Util.<JDTagSection>list();
    
    @Test
    public void testJDGeneralSection() {
        testJD("/** */", _JavaDoc("/**", list(ONEWS), NO_TAG_SECTIONS, "*/"));
        testJD("/*** **/", _JavaDoc("/***", list(ONEWS), NO_TAG_SECTIONS, "**/"));
        testJD("/** * */", _JavaDoc("/**", list(ONEWS, _JDAsterisk(), ONEWS), NO_TAG_SECTIONS, "*/"));
        testJD("/** *\n */", _JavaDoc("/**", list(ONEWS, _JDAsterisk(), ONEEOL, ONEWS), NO_TAG_SECTIONS, "*/"));
        testJD("/**\n * hello\n * world\n */", _JavaDoc("/**", list(ONEEOL, ONEWS, _JDAsterisk(), ONEWS, _JDWord("hello"), ONEEOL, ONEWS, _JDAsterisk(), ONEWS, _JDWord("world"), ONEEOL, ONEWS), NO_TAG_SECTIONS, "*/"));
        testJD("/**\n * hello @foo */", _JavaDoc("/**", list(ONEEOL, ONEWS, _JDAsterisk(), ONEWS, _JDWord("hello"), ONEWS, _JDTag("@foo"), ONEWS), NO_TAG_SECTIONS, "*/"));
        testJD("/**\n * hello\n * * @world\n */", _JavaDoc("/**", list(ONEEOL, ONEWS, _JDAsterisk(), ONEWS, _JDWord("hello"), ONEEOL, ONEWS, _JDAsterisk(), ONEWS, _JDAsterisk(), ONEWS, _JDTag("@world"), ONEEOL, ONEWS), NO_TAG_SECTIONS, "*/"));
    }
    
    @Test
    public void testJDTagSections() {
        testJD("/**@Foo*/", _JavaDoc("/**", NO_TOKENS, list(_JDTagSection("@Foo", list(_JDTag("@Foo")))), "*/"));        
        testJD("/**@Foo hello\n * world*/", _JavaDoc("/**", NO_TOKENS, list(_JDTagSection("@Foo", list(_JDTag("@Foo"), ONEWS, _JDWord("hello"), ONEEOL, ONEWS, _JDAsterisk(), ONEWS, _JDWord("world")))), "*/"));        
        testJD("/**@Foo hello\n * world\n@Bar whatever*/", _JavaDoc("/**", NO_TOKENS, list(_JDTagSection("@Foo", list(_JDTag("@Foo"), ONEWS, _JDWord("hello"), ONEEOL, ONEWS, _JDAsterisk(), ONEWS, _JDWord("world"), ONEEOL)), _JDTagSection("@Bar", list(_JDTag("@Bar"), ONEWS, _JDWord("whatever")))), "*/"));        
    }
    
    @Test
    public void testJDFull() {
        testJD("/**\n * hello\n * * @world\n @Foo hello\n * world\n@Bar whatever*/", _JavaDoc("/**", list(ONEEOL, ONEWS, _JDAsterisk(), ONEWS, _JDWord("hello"), ONEEOL, ONEWS, _JDAsterisk(), ONEWS, _JDAsterisk(), ONEWS, _JDTag("@world"), ONEEOL, ONEWS), list(_JDTagSection("@Foo", list(_JDTag("@Foo"), ONEWS, _JDWord("hello"), ONEEOL, ONEWS, _JDAsterisk(), ONEWS, _JDWord("world"), ONEEOL)), _JDTagSection("@Bar", list(_JDTag("@Bar"), ONEWS, _JDWord("whatever")))), "*/"));
    }
    
    private void testJD(String expected, JavaDoc javaDoc) {
        assertEquals(expected, ASTPrinter.print(javaDoc));
    }

    /**
     * Test that comments print properly
     */
    @Test
    public void testComments() {
        final List<JavaComment> comments = Util.<JavaComment>list(_JavaDocComment("/** hello */"), _JavaMultiLineComment("/* hello */"), _JavaEOLComment("// hello"));
        assertEquals("/** hello */\n/* hello */\n// hello\n", printComments(comments));
    }
}
