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
package com.pogofish.jadt.parser.javacc;

import static com.pogofish.jadt.ast.ASTConstants.EMPTY_PKG;
import static com.pogofish.jadt.ast.ASTConstants.NO_COMMENTS;
import static com.pogofish.jadt.ast.ASTConstants.NO_IMPORTS;
import static com.pogofish.jadt.ast.Arg._Arg;
import static com.pogofish.jadt.ast.ArgModifier._Final;
import static com.pogofish.jadt.ast.CommentedIdentifier._CommentedIdentifier;
import static com.pogofish.jadt.ast.Constructor._Constructor;
import static com.pogofish.jadt.ast.DataType._DataType;
import static com.pogofish.jadt.ast.JavaComment._JavaDocComment;
import static com.pogofish.jadt.ast.JavaComment._JavaEOLComment;
import static com.pogofish.jadt.ast.JavaComment._JavaMultiLineComment;
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
import com.pogofish.jadt.ast.Imprt;
import com.pogofish.jadt.ast.JavaComment;
import com.pogofish.jadt.ast.ParseResult;
import com.pogofish.jadt.ast.Pkg;
import com.pogofish.jadt.ast.RefType;
import com.pogofish.jadt.ast.SyntaxError;
import com.pogofish.jadt.parser.Parser;
import com.pogofish.jadt.parser.ParserImpl;
import com.pogofish.jadt.parser.StandardParser;
import com.pogofish.jadt.parser.javacc.generated.BaseJavaCCParserImplConstants;
import com.pogofish.jadt.parser.javacc.generated.Token;
import com.pogofish.jadt.source.StringSource;
import com.pogofish.jadt.util.Util;
/**
 * Tests for the new JavaCC based parser.
 * 
 * @author jiry
 */
public class JavaCCParserImplTest {

    private static final JavaCCParserImplFactory PARSER_IMPL_FACTORY = new JavaCCParserImplFactory();

    private static final String COMMENT_ERROR_MESSAGE = "a java comment, which is only allowed before 'package', 'import', data type definitions and constructor defintions";

    /**
     * In order to zero in on specific sections of the parser it's easier to
     * poke at the Impl than try to parse whole documents and pull them apart
     * 
     * @param text
     * @return
     */
    private JavaCCParserImpl parserImpl(final String text) {
        final StringSource source = new StringSource("ParserTest", text);
        return PARSER_IMPL_FACTORY.create(source.getSrcInfo(), source.createReader());
    }
  
    /**
     * Test unterminated comments
     */
    @Test
    public void testUnterminatedComment() throws Exception {
        final ParserImpl p1 = parserImpl("/** hello");
        checkError(list(_UnexpectedToken("whatever", "unterminated comment", 1)), "BAD_IDENTIFIER_unterminated comment@1", p1.identifier("whatever"), p1);

    }
    
    /**
     * Make sure primitives parse correctly
     */
    @Test
    public void testPrimitive() throws Exception {
        assertEquals(_BooleanType(), parserImpl("boolean").primitiveType());
        assertEquals(_ByteType(), parserImpl("byte").primitiveType());
        assertEquals(_ShortType(), parserImpl("short").primitiveType());
        assertEquals(_CharType(), parserImpl("char").primitiveType());
        assertEquals(_IntType(), parserImpl("int").primitiveType());
        assertEquals(_LongType(), parserImpl("long").primitiveType());
        assertEquals(_DoubleType(), parserImpl("double").primitiveType());
        assertEquals(_FloatType(), parserImpl("float").primitiveType());
    }

    /**
     * Make sure class types parse correctly
     */
    @Test
    public void testClassType() throws Exception {
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
     * Make sure the arrays types are wrapperd properly
     * based on the number [] pairs in the tokenizer stream
     */
    @Test
    public void testArray() throws Exception {
        assertEquals(_ArrayType(_Primitive(_IntType())), parserImpl("int[]")
                .refType());
        assertEquals(
                _ArrayType(_Ref(_ArrayType(_Primitive(_IntType())))),
                parserImpl("int[][]").refType());
        assertEquals(_Ref(_ArrayType(_Primitive(_IntType()))), parserImpl("int[]")
                .type());
        assertEquals(
                _Ref(_ArrayType(_Ref(_ArrayType(_Primitive(_IntType()))))),
                parserImpl("int[][]").type());
    }

    /**
     * Make sure types parse properly
     */
    @Test
    public void testType() throws Exception {
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
    public void testRefType() throws Exception {
        assertEquals(_ClassType("Foo", Util.<RefType> list()),
                parserImpl("Foo").refType());
        assertEquals(_ArrayType(_Primitive(_IntType())), parserImpl("int[]")
                .refType());
        assertEquals(
                _ArrayType(_Ref(_ClassType("Foo", Util.<RefType> list()))),
                parserImpl("Foo[]").refType());
    }
    
    @Test
    public void testRefTypeErrors() throws Exception {
        final ParserImpl p1 = parserImpl("Foo[");
        checkError(list(_UnexpectedToken("']'", "<EOF>", 1)), _ArrayType(_Ref(_ClassType("Foo", Util.<RefType> list()))), p1.refType(), p1);

        final ParserImpl p2 = parserImpl("Foo<int>");
        checkError(list(_UnexpectedToken("'['", "'>'", 1)), _Ref(_ClassType("Foo", list(_ArrayType(_Primitive(_IntType()))))), p2.type(), p2);

        final ParserImpl p3 = parserImpl("Foo<A");
        checkError(list(_UnexpectedToken("'>'", "<EOF>", 1)), _Ref(_ClassType("Foo", list(_ClassType("A", Util.<RefType>list())))), p3.type(), p3);

        final ParserImpl p4 = parserImpl("Foo<A B>");
        checkError(list(_UnexpectedToken("'>'", "'B'", 1)), _Ref(_ClassType("Foo", list(_ClassType("A", Util.<RefType>list())))), p4.type(), p4);

        final ParserImpl p5 = parserImpl("");
        checkError(list(_UnexpectedToken("a class name", "<EOF>", 1)), _Ref(_ClassType("NO_IDENTIFIER@1", Util.<RefType>list())), p5.type(), p5);
        
        final ParserImpl p6 = parserImpl("import");
        checkError(list(_UnexpectedToken("a class name", "'import'", 1)), _Ref(_ClassType("BAD_IDENTIFIER_import@1", Util.<RefType>list())), p6.type(), p6);
        
    }

    /**
     * Make sure arg modifiers parse properly
     */
    @Test
    public void testArgModifier() throws Exception {
        assertEquals(_Final(), parserImpl("final").argModifier());
    }

    /**
     * Make sure list of arg modifiers parse properly
     */
    @Test
    public void testArgModifiers() throws Exception {
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
    public void testArg() throws Exception {
        assertEquals(
                _Arg(Util.<ArgModifier> list(), _Primitive(_IntType()), "Foo"),
                parserImpl("int Foo").arg());
        assertEquals(_Arg(list(_Final()), _Primitive(_IntType()), "Foo"),
                parserImpl("final int Foo").arg());
    }
    
    @Test
   public void testArgErrors() throws Exception {
        ParserImpl p1 = parserImpl("int");
        checkError(list(_UnexpectedToken("an argument name", "<EOF>", 1)), _Arg(Util.<ArgModifier>list(), _Primitive(_IntType()), "NO_IDENTIFIER@1"), p1.arg(), p1);

        ParserImpl p2 = parserImpl("int boolean");
        checkError(list(_UnexpectedToken("an argument name", "'boolean'", 1)), _Arg(Util.<ArgModifier>list(), _Primitive(_IntType()), "BAD_IDENTIFIER_boolean@1"), p2.arg(), p2);

    }

    /**
     * Make sure an arg list parses properly
     */
    @Test
    public void testArgs() throws Exception {
        assertEquals(
                list(_Arg(Util.<ArgModifier> list(), _Primitive(_IntType()),
                        "Foo")), parserImpl("(int Foo)").args());
        assertEquals(
                list(_Arg(Util.<ArgModifier> list(), _Primitive(_IntType()),
                        "Foo"),
                        _Arg(Util.<ArgModifier> list(),
                                _Primitive(_BooleanType()), "Bar")),
                parserImpl("(int Foo, boolean Bar)").args());
    }
    
    @Test
   public void testArgsErrors() throws Exception {
        ParserImpl p1 = parserImpl("(int Foo");
        checkError(list(_UnexpectedToken("')'", "<EOF>", 1)), list(_Arg(Util.<ArgModifier>list(), _Primitive(_IntType()), "Foo")), p1.args(), p1);
        
        ParserImpl p2 = parserImpl("()");
        checkError(list(_UnexpectedToken("a class name", "')'", 1)), list(_Arg(Util.<ArgModifier>list(), _Ref(_ClassType("NO_IDENTIFIER@1", Util.<RefType>list())), "NO_IDENTIFIER@2")), p2.args(), p2);

        ParserImpl p3 = parserImpl("(int Foo,)");
        checkError(list(_UnexpectedToken("a class name", "')'", 1)), list(_Arg(Util.<ArgModifier> list(), _Primitive(_IntType()),
                "Foo"), _Arg(Util.<ArgModifier>list(), _Ref(_ClassType("NO_IDENTIFIER@1", Util.<RefType>list())), "NO_IDENTIFIER@2")), p3.args(), p3);
        
        ParserImpl p4 = parserImpl("(int Foo int Bar)");
        checkError(list(_UnexpectedToken("')'", "'int'", 1)), list(_Arg(Util.<ArgModifier> list(), _Primitive(_IntType()),
                "Foo")), p4.args(), p4);
    }

    /**
     * Make sure a constructor parses properly
     */
    @Test
    public void testConstructor() throws Exception {
        // no arg
        assertEquals(_Constructor(NO_COMMENTS, "Foo", Util.<Arg> list()), parserImpl("Foo")
                .constructor(NO_COMMENTS));
        // args
        assertEquals(
                _Constructor(NO_COMMENTS, 
                        "Foo",
                        list(_Arg(Util.<ArgModifier> list(),
                                _Primitive(_IntType()), "Bar"))),
                parserImpl("Foo(int Bar)").constructor(NO_COMMENTS));
    }
    
  @Test
  public void testConstructorErrors() throws Exception {
        ParserImpl p1 = parserImpl("");
        checkError(list(_UnexpectedToken("a constructor name", "<EOF>", 1)), _Constructor(NO_COMMENTS, "NO_IDENTIFIER@1", Util.<Arg>list()), p1.constructor(NO_COMMENTS), p1);
    }

    /**
     * Make sure a contructor list parses properly
     */
    @Test
    public void testConstructors() throws Exception {
        assertEquals(list(_Constructor(NO_COMMENTS, "Foo", Util.<Arg> list())),
                parserImpl("Foo").constructors(NO_COMMENTS));
        assertEquals(
                list(_Constructor(NO_COMMENTS, "Foo", Util.<Arg> list()),
                        _Constructor(NO_COMMENTS, "Bar", Util.<Arg> list())),
                parserImpl("Foo|Bar").constructors(NO_COMMENTS));
    }
    
    @Test
   public void testConstructorsErrors() throws Exception {
        final ParserImpl p1 = parserImpl("Foo|");
        checkError(list(_UnexpectedToken("a constructor name", "<EOF>", 1)), 
                list(_Constructor(NO_COMMENTS, "Foo", Util.<Arg>list()), _Constructor(NO_COMMENTS, "NO_IDENTIFIER@1", Util.<Arg>list())), p1.constructors(NO_COMMENTS), p1);

        final ParserImpl p2 = parserImpl("Foo||Bar");
        checkError(list(_UnexpectedToken("a constructor name", "'|'", 1)), 
                list(_Constructor(NO_COMMENTS, "Foo", Util.<Arg>list()), _Constructor(NO_COMMENTS, "NO_IDENTIFIER@1", Util.<Arg>list()), _Constructor(NO_COMMENTS, "Bar", Util.<Arg>list())), p2.constructors(NO_COMMENTS), p2);
    }

    /**
     * Make sure datatypes parse properly
     */
    @Test
    public void testDataType() throws Exception {
        assertEquals(
                _DataType(NO_COMMENTS, "Foo", Util.<String> list(),
                        list(_Constructor(NO_COMMENTS, "Foo", Util.<Arg> list()))),
                parserImpl("Foo=Foo").dataType());
        assertEquals(
                _DataType(NO_COMMENTS, "Foo", list("A"),
                        list(_Constructor(NO_COMMENTS, "Foo", Util.<Arg> list()))),
                parserImpl("Foo<A>=Foo").dataType());
        assertEquals(
                _DataType(NO_COMMENTS, "Foo", list("A", "B"),
                        list(_Constructor(NO_COMMENTS, "Foo", Util.<Arg> list()))),
                parserImpl("Foo<A, B>=Foo").dataType());
    }
    
    @Test
   public void testDataTypeErrors() throws Exception {
        
        final ParserImpl p1 = parserImpl("boolean = Foo");
        checkError(list(_UnexpectedToken("a data type name", "'boolean'", 1)), _DataType(NO_COMMENTS, "BAD_IDENTIFIER_boolean@1", Util.<String>list(), list(_Constructor(NO_COMMENTS, "Foo", Util.<Arg>list()))), p1.dataType(), p1);
 
        final ParserImpl p2 = parserImpl("= Foo");
        checkError(list(_UnexpectedToken("a data type name", "'='", 1)), _DataType(NO_COMMENTS, "NO_IDENTIFIER@1", Util.<String>list(), list(_Constructor(NO_COMMENTS, "Foo", Util.<Arg>list()))), p2.dataType(), p2);
 
        final ParserImpl p3 = parserImpl("Bar Foo");
        checkError(list(_UnexpectedToken("'='", "'Foo'", 1)), _DataType(NO_COMMENTS, "Bar", Util.<String>list(), list(_Constructor(NO_COMMENTS, "Foo", Util.<Arg>list()))), p3.dataType(), p3);

        final ParserImpl p4 = parserImpl("");
        checkError(list(_UnexpectedToken("a data type name", "<EOF>", 1)), _DataType(NO_COMMENTS, "NO_IDENTIFIER@1", Util.<String>list(), list(_Constructor(NO_COMMENTS, "NO_IDENTIFIER@2", Util.<Arg>list()))), p4.dataType(), p4);

        final ParserImpl p5 = parserImpl("Bar<A, = Foo");
        checkError(list(_UnexpectedToken("a type parameter", "'='", 1)), _DataType(NO_COMMENTS, "Bar", list("A", "NO_IDENTIFIER@1"), list(_Constructor(NO_COMMENTS, "Foo", Util.<Arg>list()))), p5.dataType(), p5);
 
    }

    /**
     * Make sure a datatype list parses properly
     */
    @Test
    public void testDataTypes() throws Exception {
        assertEquals(
                list(_DataType(NO_COMMENTS, "Foo", Util.<String> list(),
                        list(_Constructor(NO_COMMENTS, "Foo", Util.<Arg> list())))),
                parserImpl("Foo=Foo").dataTypes());
        assertEquals(
                list(_DataType(NO_COMMENTS, "Foo", Util.<String> list(),
                        list(_Constructor(NO_COMMENTS, "Foo", Util.<Arg> list()))),
                        _DataType(NO_COMMENTS, "Bar", Util.<String> list(),
                                list(_Constructor(NO_COMMENTS, "Bar", Util.<Arg> list())))),
                parserImpl("Foo=Foo Bar = Bar").dataTypes());
    }

    /**
     * Make sure type arguments on data types parse properly
     */
    @Test
    public void testTypeArguments() throws Exception {
        assertEquals("Invalid parse of a single type argument", list("A"),
                parserImpl("<A>").typeArguments());
        assertEquals("Invalid parse of mulitple type arguments",
                list("A", "B", "C"), parserImpl("<A,B, C>").typeArguments());
    }
    
    @Test
   public void testTypeArgumentsErrors() throws Exception {
        
        ParserImpl p1 = parserImpl("<>");
        checkError(list(_UnexpectedToken("a type parameter", "'>'", 1)), list("NO_IDENTIFIER@1"), p1.typeArguments(), p1);

        ParserImpl p2 = parserImpl("<A");
        checkError(list(_UnexpectedToken("'>'", "<EOF>", 1)), list("A"), p2.typeArguments(), p2);

        ParserImpl p3 = parserImpl("<");
        checkError(list(_UnexpectedToken("a type parameter", "<EOF>", 1)), list("NO_IDENTIFIER@1"), p3.typeArguments(), p3);

        ParserImpl p4 = parserImpl("<boolean, A>");
        checkError(list(_UnexpectedToken("a type parameter", "'boolean'", 1)), list("BAD_IDENTIFIER_boolean@1", "A"), p4.typeArguments(), p4);

        ParserImpl p5 = parserImpl("<A, ,B>");
        checkError(list(_UnexpectedToken("a type parameter", "','", 1)), list("A", "NO_IDENTIFIER@1", "B"), p5.typeArguments(), p5);

        ParserImpl p6 = parserImpl("<A B>");
        checkError(list(_UnexpectedToken("'>'", "'B'", 1)), list("A"), p6.typeArguments(), p6);

    }

    /**
     * Make sure a package declaration parse properly
     */
    @Test
    public void testPackage() throws Exception {
        assertEquals(Pkg._Pkg(NO_COMMENTS, "hello"), parserImpl("package hello").pkg());
        assertEquals(Pkg._Pkg(NO_COMMENTS, "hello.world"), parserImpl("package hello.world").pkg());
    }
    
    @Test
    public void testPackageErrors() throws Exception {
        
        final ParserImpl p1 = parserImpl("package");
        checkError(list(_UnexpectedToken("a package name", "<EOF>", 1)), Pkg._Pkg(NO_COMMENTS, "NO_IDENTIFIER@1"), p1.pkg(), p1);

        final ParserImpl p2 = parserImpl("package foo.bar.");
        checkError(list(_UnexpectedToken("a package name", "<EOF>", 1)), Pkg._Pkg(NO_COMMENTS, "foo.bar.NO_IDENTIFIER@1"), p2.pkg(), p2);

        final ParserImpl p3 = parserImpl("package ?g42");
        checkError(list(_UnexpectedToken("a package name", "'?g42'", 1)), Pkg._Pkg(NO_COMMENTS, "BAD_IDENTIFIER_?g42@1"), p3.pkg(), p3);

        final ParserImpl p4 = parserImpl("package boolean");
        checkError(list(_UnexpectedToken("a package name", "'boolean'", 1)), Pkg._Pkg(NO_COMMENTS, "BAD_IDENTIFIER_boolean@1"), p4.pkg(), p4);
    }

    /**
     * Make sure a package list parses properly
     */
    @Test
    public void testImports() throws Exception {
        assertEquals(NO_IMPORTS, parserImpl("").imports());
        assertEquals(list(Imprt._Imprt(NO_COMMENTS, "hello")), parserImpl("import hello").imports());
        assertEquals(list(Imprt._Imprt(NO_COMMENTS, "hello"), Imprt._Imprt(NO_COMMENTS, "oh.yeah")),
                parserImpl("import hello import oh.yeah").imports());
    }
    
    @Test
    public void testImportsErrors() throws Exception {
        final ParserImpl p1 = parserImpl("import");
        checkError(list(_UnexpectedToken("a package name", "<EOF>", 1)), list(Imprt._Imprt(NO_COMMENTS, "NO_IDENTIFIER@1")), p1.imports(), p1);
        
        final ParserImpl p2 = parserImpl("import ?g42");
        checkError(list(_UnexpectedToken("a package name", "'?g42'", 1)), list(Imprt._Imprt(NO_COMMENTS, "BAD_IDENTIFIER_?g42@1")), p2.imports(), p2);
        
        final ParserImpl p3 = parserImpl("import boolean");
        checkError(list(_UnexpectedToken("a package name", "'boolean'", 1)), list(Imprt._Imprt(NO_COMMENTS, "BAD_IDENTIFIER_boolean@1")), p3.imports(), p3);       

        final ParserImpl p4 = parserImpl("import import boolean");
        checkError(list(_UnexpectedToken("a package name", "'import'", 1)), list(Imprt._Imprt(NO_COMMENTS, "BAD_IDENTIFIER_import@1")), p4.imports(), p4);       

        final ParserImpl p5 = parserImpl("import package boolean");
        checkError(list(_UnexpectedToken("a package name", "'package'", 1)), list(Imprt._Imprt(NO_COMMENTS, "BAD_IDENTIFIER_package@1")), p5.imports(), p5);       
    }


    private static <A> void checkError(List<SyntaxError> expectedErrors, A expectedResult, A actualResult, ParserImpl p) {
        assertEquals(expectedErrors, p.errors());
        assertEquals(expectedResult, actualResult);
    }

    /**
     * Test the whole shebang with a minimal document
     */
    @Test
    public void testMinimal() {
        final Parser parser = new StandardParser(PARSER_IMPL_FACTORY);
        final ParseResult result = parser.parse(new StringSource("ParserTest",
                "Foo = Foo"));

        assertEquals(new ParseResult(new Doc("ParserTest", EMPTY_PKG, NO_IMPORTS, list(_DataType(NO_COMMENTS, "Foo", Util.<String> list(),
                list(_Constructor(NO_COMMENTS, "Foo", Util.<Arg> list()))))), Util.<SyntaxError>list()), result);
    }

    /**
     * Test the whole shebang with a less minimal document
     */
    @Test
    public void testFull() {
        final Parser parser = new StandardParser(PARSER_IMPL_FACTORY);
        final String source = "/*a pre-start comment*///a start comment\npackage hello.world /* here are some imports */import wow.man import flim.flam "
                + "/*datatype comment*/FooBar /*equal comment*/= /*constructor comment*/foo /*bar comment*/| /*really a bar comment*/bar(int hey, final String[] yeah) whatever = whatever";
        final ParseResult result = parser.parse(new StringSource("ParserTest",
                source));

        assertEquals(
                new ParseResult(new Doc(
                        "ParserTest",
                        Pkg._Pkg(list(_JavaMultiLineComment("/*a pre-start comment*/"), _JavaEOLComment("//a start comment")), "hello.world"), list(Imprt._Imprt(list(_JavaMultiLineComment("/* here are some imports */")), "wow.man"), Imprt._Imprt(NO_COMMENTS, "flim.flam")),
                        list(new DataType(list(_JavaMultiLineComment("/*datatype comment*/")), 
                                "FooBar",
                                Util.<String> list(),
                                list(
                                        new Constructor(list(_JavaMultiLineComment("/*equal comment*/"), _JavaMultiLineComment("/*constructor comment*/")), "foo", Util
                                                .<Arg> list()),
                                        new Constructor(list(_JavaMultiLineComment("/*bar comment*/"), _JavaMultiLineComment("/*really a bar comment*/")), 
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
                                new DataType(NO_COMMENTS, "whatever", Util.<String> list(),
                                        list(new Constructor(NO_COMMENTS, "whatever", Util
                                                .<Arg> list()))))), Util.<SyntaxError>list()).toString(), result.toString());
    }

    /**
     * Test the whole shebang with an error
     */
    @Test
   public void testError() {
        final Parser parser = new StandardParser(PARSER_IMPL_FACTORY);
        final String source = "//a start comment\npackage hello.world /* here are some imports */import wow.man import flim.flam "
                + "FooBar = foo | bar(int hey, final String[] yeah) whatever = int";
        final ParseResult result = parser.parse(new StringSource("ParserTest",
                source));

        assertEquals(
                new ParseResult(new Doc(
                        "ParserTest",
                        Pkg._Pkg(list(_JavaEOLComment("//a start comment")), "hello.world"), list(Imprt._Imprt(list(_JavaMultiLineComment("/* here are some imports */")), "wow.man"), Imprt._Imprt(NO_COMMENTS, "flim.flam")),
                        list(new DataType(NO_COMMENTS, 
                                "FooBar",
                                Util.<String> list(),
                                list(
                                        new Constructor(NO_COMMENTS, "foo", Util
                                                .<Arg> list()),
                                        new Constructor(NO_COMMENTS, 
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
                                new DataType(NO_COMMENTS, "whatever", Util.<String> list(),
                                        list(new Constructor(NO_COMMENTS, "BAD_IDENTIFIER_int@1", Util
                                                .<Arg> list()))))), list(SyntaxError._UnexpectedToken("a constructor name", "'int'", 2))).toString(), result.toString());
                
    }
    
    @Test 
    public void testInvalidCommentLocations() throws Exception {
        final ParserImpl p1 = parserImpl("/* */");
        p1.eof();
        checkVoidCommentError("<EOF>", p1);
        
        final ParserImpl p2 = parserImpl("/**/>");
        p2.rangle();
        checkVoidCommentError("'>'", p2);
        
        final ParserImpl p3 = parserImpl("/**/<");
        p3.langle();
        checkVoidCommentError("'<'", p3);
        
        final ParserImpl p4 = parserImpl("/**/[");
        p4.lbracket();
        checkVoidCommentError("'['", p4);
        
        final ParserImpl p5 = parserImpl("/**/]");
        p5.rbracket();
        checkVoidCommentError("']'", p5);
        
        final ParserImpl p6 = parserImpl("/**/(");
        p6.lparen();
        checkVoidCommentError("'('", p6);
        
        final ParserImpl p7 = parserImpl("/**/)");
        p7.rparen();
        checkVoidCommentError("')'", p7);
        
        final ParserImpl p8 = parserImpl("/**/,");
        p8.comma();
        checkVoidCommentError("','", p8);
        
        final ParserImpl p9 = parserImpl("/**/.");
        p9.dot();
        checkVoidCommentError("'.'", p9);        
        
        final ParserImpl p10 = parserImpl("/**/double");
        checkCommentError(_DoubleType(), p10.doubleType(), "'double'", p10);
        
        final ParserImpl p11 = parserImpl("/**/float");
        checkCommentError(_FloatType(), p11.floatType(), "'float'", p11);
        
        final ParserImpl p12 = parserImpl("/**/long");
        checkCommentError(_LongType(), p12.longType(), "'long'", p12);
        
        final ParserImpl p13 = parserImpl("/**/int");
        checkCommentError(_IntType(), p13.intType(), "'int'", p13);
        
        final ParserImpl p14 = parserImpl("/**/short");
        checkCommentError(_ShortType(), p14.shortType(), "'short'", p14);
        
        final ParserImpl p15 = parserImpl("/**/char");
        checkCommentError(_CharType(), p15.charType(), "'char'", p15);
        
        final ParserImpl p16 = parserImpl("/**/byte");
        checkCommentError(_ByteType(), p16.byteType(), "'byte'", p16);
        
        final ParserImpl p17 = parserImpl("/**/boolean");
        checkCommentError(_BooleanType(), p17.booleanType(), "'boolean'", p17);
        
        final ParserImpl p18 = parserImpl("/**/final");
        checkCommentError(_Final(), p18.finalKeyword(), "'final'", p18);
        
        final ParserImpl p20 = parserImpl("/**/identifier");
        checkCommentError("identifier", p20.identifier("an identifier"), "an identifier", p20);
    }

    private static void checkVoidCommentError(String expected, ParserImpl p) {
        assertEquals(list(_UnexpectedToken(expected, COMMENT_ERROR_MESSAGE, 1)), p.errors());
        
    }

    private static <A> void checkCommentError(A expected, A actual, String message, ParserImpl p) {
        checkError(list(_UnexpectedToken(message, COMMENT_ERROR_MESSAGE, 1)), expected, actual, p);
    }
    
    @Test
    public void testCommentAllowedTokens() throws Exception {
        final String commentString = "/*block*//**javadoc*///eol\n";
        final List<JavaComment> comments = list(_JavaMultiLineComment("/*block*/"), _JavaDocComment("/**javadoc*/"), _JavaEOLComment("//eol"));
        
        final ParserImpl p1 = parserImpl(commentString + "|");
        checkParseResult(comments, p1.bar(), p1);

        final ParserImpl p2 = parserImpl(commentString + "=");
        checkParseResult(comments, p2.equals(), p2);

        final ParserImpl p3 = parserImpl(commentString + "package");
        checkParseResult(comments, p3.packageKeyword(), p3);

        final ParserImpl p4 = parserImpl(commentString + "import");
        checkParseResult(comments, p4.importKeyword(), p4);

        final ParserImpl p5 = parserImpl(commentString + "hello");
        checkParseResult(_CommentedIdentifier(comments, "hello"), p5.commentedIdentifier("an identifier"), p5);
    }

    private <A>void checkParseResult(A expected, A actual, ParserImpl p) {
        assertEquals(expected, actual);
        assertEquals(Util.<SyntaxError>list(), p.errors());
    }
    
    /**
     * Make sure the tokenComments method can handle non-comment tokens
     * Mostly testing for coverage
     */
    @Test
    public void testNonJavaCommentSpecialToken() {
        final JavaCCParserImpl p1 = parserImpl("whatever");
        final Token token = new Token(BaseJavaCCParserImplConstants.IDENTIFIER, "hello");
        final Token specialtoken = new Token(BaseJavaCCParserImplConstants.WS, "   ");
        token.specialToken = specialtoken;
        final List<JavaComment> comments = p1.tokenComments(token);
        assertEquals(NO_COMMENTS, comments);
    }
    
    
    @Test
    public void testNonsense() throws Exception {
        // the old parser would get stuck in a loop with this extraneous >
        final Parser parser = new StandardParser(PARSER_IMPL_FACTORY);
        
        final ParseResult result = parser.parse(new StringSource("whatever", "FormalParameter = FormalParameter(final List<Modifier> modifiers>, final TypeRef type, final String name)"));
        assertEquals(ParseResult._ParseResult(Doc._Doc("whatever", EMPTY_PKG, NO_IMPORTS, 
                list(_DataType(NO_COMMENTS, "FormalParameter", Util.<String>list(), 
                        list(_Constructor(NO_COMMENTS, "FormalParameter", 
                                list(_Arg(list(_Final()), _Ref(_ClassType("List", list(_ClassType("Modifier", Util.<RefType>list())))), "modifiers") /*,
                                     _Arg(list(_Final()), _Ref(_ClassType("TypeRef", Util.<RefType>list())), "type"),
                                     _Arg(list(_Final()), _Ref(_ClassType("String", Util.<RefType>list())), "name") */)
                                                ))))), list(SyntaxError._UnexpectedToken("')'", "'>'", 1))).toString(), result.toString());
    }
}
