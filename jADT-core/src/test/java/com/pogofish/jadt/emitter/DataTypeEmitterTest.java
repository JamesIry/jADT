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
package com.pogofish.jadt.emitter;

import static com.pogofish.jadt.ast.ASTConstants.NO_COMMENTS;
import static com.pogofish.jadt.ast.Annotation._Annotation;
import static com.pogofish.jadt.ast.AnnotationElement._ElementValue;
import static com.pogofish.jadt.ast.AnnotationValue._AnnotationValueAnnotation;
import static com.pogofish.jadt.ast.JDToken._JDWhiteSpace;
import static com.pogofish.jadt.ast.JDToken._JDWord;
import static com.pogofish.jadt.ast.JavaComment._JavaDocComment;
import static com.pogofish.jadt.ast.Optional._Some;
import static com.pogofish.jadt.ast.RefType._ClassType;
import static com.pogofish.jadt.ast.Type._Ref;
import static com.pogofish.jadt.util.Util.list;
import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;

import com.pogofish.jadt.ast.Annotation;
import com.pogofish.jadt.ast.AnnotationElement;
import com.pogofish.jadt.ast.Arg;
import com.pogofish.jadt.ast.ArgModifier;
import com.pogofish.jadt.ast.Constructor;
import com.pogofish.jadt.ast.DataType;
import com.pogofish.jadt.ast.JDTagSection;
import com.pogofish.jadt.ast.Optional;
import com.pogofish.jadt.ast.RefType;
import com.pogofish.jadt.sink.StringSink;
import com.pogofish.jadt.util.Util;


/**
 * Test the StandardDataTypeEmitter
 *
 * @author jiry
 */
public class DataTypeEmitterTest {
    private static final Optional<RefType> NO_EXTENDS = Optional.<RefType>_None();
    private static final List<RefType> NO_IMPLEMENTS = Util.<RefType>list();
    private static final List<RefType> NO_TYPE_ARGS = Util.<RefType>list();
    private static final List<Annotation> ANNOTATIONS = list(_Annotation("foo", Optional.<AnnotationElement>_None()), _Annotation("foo", _Some(_ElementValue(_AnnotationValueAnnotation(_Annotation("bar", Optional.<AnnotationElement>_None()))))));
    private static final String HEADER = "/*header*/\n";
    private static final String MULTI_HEADER_NO_BASE = 
    "@foo\n" +
    "@foo( @bar )\n" +
    "FooBar =\n" +
    "    Foo(Integer yeah, String hmmm)\n" +
    "  | Bar\n" +
    "*/\n";
    private static final String MULTI_HEADER_WITH_BASE =  
    "@foo\n" +
    "@foo( @bar )\n" +
    "FooBar extends FooA implements FooB, FooC =\n" +
    "    Foo(Integer yeah, String hmmm)\n" +
    "  | Bar\n" +
    "*/\n";
    private static final String MULTI_CONSTRUCTOR_NO_BASE = 
    "/** hello */\n" +
    "@foo\n" +
    "@foo( @bar )\n" +
    "public abstract class FooBar/* type arguments */ {\n" +
    "\n" +
    "   private FooBar() {\n" +
    "   }\n" +
    "\n" +
    "/* factory FooBar Foo */\n" +
    "/* factory FooBar Bar */\n" +
    "\n" +
    "   public static interface MatchBlock/* type arguments */ {\n" +
    "      ResultType _case(Foo/* type arguments */ x);\n" +
    "      ResultType _case(Bar/* type arguments */ x);\n" +
    "   }\n" +
    "\n" +
    "   public static abstract class MatchBlockWithDefault/* type arguments */ implements MatchBlock/* type arguments */ {\n" +
    "      @Override\n" +
    "      public ResultType _case(Foo/* type arguments */ x) { return _default(x); }\n" +
    "\n" +
    "      @Override\n" +
    "      public ResultType _case(Bar/* type arguments */ x) { return _default(x); }\n" +    
    "\n" +
    "      protected abstract ResultType _default(FooBar/* type arguments */ x);\n" +
    "   }\n" +
    "\n" +
    "   public static interface SwitchBlock/* type arguments */ {\n" +
    "      void _case(Foo/* type arguments */ x);\n" +
    "      void _case(Bar/* type arguments */ x);\n" +
    "   }\n" +
    "\n" +
    "   public static abstract class SwitchBlockWithDefault/* type arguments */ implements SwitchBlock/* type arguments */ {\n" +
    "      @Override\n" +
    "      public void _case(Foo/* type arguments */ x) { _default(x); }\n" +
    "\n" +
    "      @Override\n" +
    "      public void _case(Bar/* type arguments */ x) { _default(x); }\n" +    
    "\n" +
    "      protected abstract void _default(FooBar/* type arguments */ x);\n" +
    "   }\n" +
    "\n" +    
    "/* declaration FooBar Foo */\n" +
    "\n" +
    "/* declaration FooBar Bar */\n" +
    "\n" +
    "   public abstract <ResultType> ResultType match(MatchBlock/* type arguments */ matchBlock);\n" +
    "\n" +
    "   public abstract void _switch(SwitchBlock/* type arguments */ switchBlock);\n" +
    "\n" +
    "}";
    
    private static final String MULTI_CONSTRUCTOR_WITH_BASE = 
    "/** hello */\n" +
    "@foo\n" +
    "@foo( @bar )\n" +
    "public abstract class FooBar/* type arguments */ extends FooA implements FooB, FooC {\n" +
    "\n" +
    "   private FooBar() {\n" +
    "   }\n" +
    "\n" +
    "/* factory FooBar Foo */\n" +
    "/* factory FooBar Bar */\n" +
    "\n" +
    "   public static interface MatchBlock/* type arguments */ {\n" +
    "      ResultType _case(Foo/* type arguments */ x);\n" +
    "      ResultType _case(Bar/* type arguments */ x);\n" +
    "   }\n" +
    "\n" +
    "   public static abstract class MatchBlockWithDefault/* type arguments */ implements MatchBlock/* type arguments */ {\n" +
    "      @Override\n" +
    "      public ResultType _case(Foo/* type arguments */ x) { return _default(x); }\n" +
    "\n" +
    "      @Override\n" +
    "      public ResultType _case(Bar/* type arguments */ x) { return _default(x); }\n" +    
    "\n" +
    "      protected abstract ResultType _default(FooBar/* type arguments */ x);\n" +
    "   }\n" +
    "\n" +
    "   public static interface SwitchBlock/* type arguments */ {\n" +
    "      void _case(Foo/* type arguments */ x);\n" +
    "      void _case(Bar/* type arguments */ x);\n" +
    "   }\n" +
    "\n" +
    "   public static abstract class SwitchBlockWithDefault/* type arguments */ implements SwitchBlock/* type arguments */ {\n" +
    "      @Override\n" +
    "      public void _case(Foo/* type arguments */ x) { _default(x); }\n" +
    "\n" +
    "      @Override\n" +
    "      public void _case(Bar/* type arguments */ x) { _default(x); }\n" +    
    "\n" +
    "      protected abstract void _default(FooBar/* type arguments */ x);\n" +
    "   }\n" +
    "\n" +    
    "/* declaration FooBar Foo */\n" +
    "\n" +
    "/* declaration FooBar Bar */\n" +
    "\n" +
    "   public abstract <ResultType> ResultType match(MatchBlock/* type arguments */ matchBlock);\n" +
    "\n" +
    "   public abstract void _switch(SwitchBlock/* type arguments */ switchBlock);\n" +
    "\n" +
    "}";
    
    private static final String SINGLE_CONSTRUCTOR_NO_BASE = 
    "/** hello */\n" +
    "@foo\n" +
    "@foo( @bar )\n" +
    "public final class FooBar/* type arguments */ {\n" +
    "\n" +
    "/* constructor factory FooBar Foo FooBar*/\n" +
    "\n" +
    "   /* constructor method FooBar*/\n" +
    "\n" +
    "   /* hashCode method FooBar*/\n" +
    "\n" +
    "   /* equals method FooBar*/\n" +
    "\n" +
    "   /* toString method FooBar*/\n" +
    "\n" +
    "}";
    private static final String SINGLE_CONSTRUCTOR_WITH_BASE = 
    "/** hello */\n" +
    "@foo\n" +
    "@foo( @bar )\n" +
    "public final class FooBar/* type arguments */ extends FooA implements FooB, FooC {\n" +
    "\n" +
    "/* constructor factory FooBar Foo FooBar*/\n" +
    "\n" +
    "   /* constructor method FooBar*/\n" +
    "\n" +
    "   /* hashCode method FooBar*/\n" +
    "\n" +
    "   /* equals method FooBar*/\n" +
    "\n" +
    "   /* toString method FooBar*/\n" +
    "\n" +
    "}";
    private static final String SINGLE_HEADER_NO_BASE =            
   "@foo\n" +
   "@foo( @bar )\n" +
    "FooBar =\n" +
    "    Foo(Integer yeah, String hmmm)\n" +
    "*/\n";
    private static final String SINGLE_HEADER_WITH_BASE =
    "@foo\n" +
    "@foo( @bar )\n" +
    "FooBar extends FooA implements FooB, FooC =\n" +
    "    Foo(Integer yeah, String hmmm)\n" +
    "*/\n";
    
    /**
     * Test that multiple constructors with no base does its thing correctly
     */
    @Test
    public void testMultipleConstructorsNoBase() {
        final DataType fooBar = new DataType(Util.list(_JavaDocComment("/**", list(_JDWhiteSpace(" "), _JDWord("hello"), _JDWhiteSpace(" ")), Util.<JDTagSection>list(), "*/")), ANNOTATIONS, "FooBar", Util.<String>list(), NO_EXTENDS, NO_IMPLEMENTS, list(
                new Constructor(NO_COMMENTS, "Foo", list(new Arg(Util.<ArgModifier>list(), _Ref(_ClassType("Integer", Util.<RefType> list())), "yeah"),
                        new Arg(Util.<ArgModifier>list(), _Ref(_ClassType("String", Util.<RefType> list())), "hmmm"))), new Constructor(NO_COMMENTS, "Bar",
                        Util.<Arg> list())));

        final StringSink sink = new StringSink("test");
        try {
            final DataTypeEmitter emitter = new StandardDataTypeEmitter(new DummyClassBodyEmitter(),
                    new DummyConstructorEmitter());

            emitter.emit(sink, fooBar, HEADER);
        } finally {
            sink.close();
        }
        assertEquals(HEADER + MULTI_HEADER_NO_BASE + MULTI_CONSTRUCTOR_NO_BASE, sink.result());
    }
   
    /**
     * Test that multiple constructors with base types does its thing correctly
     */
    @Test
    public void testMultipleConstructorsWithBase() {
        final DataType fooBar = new DataType(Util.list(_JavaDocComment("/**", list(_JDWhiteSpace(" "), _JDWord("hello"), _JDWhiteSpace(" ")), Util.<JDTagSection>list(), "*/")), ANNOTATIONS, "FooBar", Util.<String>list(), _Some(_ClassType("FooA", NO_TYPE_ARGS)), list(_ClassType("FooB", NO_TYPE_ARGS), _ClassType("FooC", NO_TYPE_ARGS)), list(
                new Constructor(NO_COMMENTS, "Foo", list(new Arg(Util.<ArgModifier>list(), _Ref(_ClassType("Integer", Util.<RefType> list())), "yeah"),
                        new Arg(Util.<ArgModifier>list(), _Ref(_ClassType("String", Util.<RefType> list())), "hmmm"))), new Constructor(NO_COMMENTS, "Bar",
                        Util.<Arg> list())));

        final StringSink sink = new StringSink("test");
        try {
            final DataTypeEmitter emitter = new StandardDataTypeEmitter(new DummyClassBodyEmitter(),
                    new DummyConstructorEmitter());

            emitter.emit(sink, fooBar, HEADER);
        } finally {
            sink.close();
        }
        assertEquals(HEADER + MULTI_HEADER_WITH_BASE + MULTI_CONSTRUCTOR_WITH_BASE, sink.result());
    }
   
    /**
     * Test that single constructor with no base does its thing properly
     */
    @Test
    public void testSingleConstructorNoBase() {
        final DataType fooBar = new DataType(Util.list(_JavaDocComment("/**", list(_JDWhiteSpace(" "), _JDWord("hello"), _JDWhiteSpace(" ")), Util.<JDTagSection>list(), "*/")), ANNOTATIONS, "FooBar", Util.<String>list(), NO_EXTENDS, NO_IMPLEMENTS, list(new Constructor(NO_COMMENTS, "Foo", list(
                new Arg(Util.<ArgModifier>list(), _Ref(_ClassType("Integer", Util.<RefType> list())), "yeah"),
                new Arg(Util.<ArgModifier>list(), _Ref(_ClassType("String", Util.<RefType> list())), "hmmm")))));

        final StringSink sink = new StringSink("test");
        try {
            final DataTypeEmitter emitter = new StandardDataTypeEmitter(new DummyClassBodyEmitter(),
                    new DummyConstructorEmitter());

            emitter.emit(sink, fooBar, HEADER);
        } finally {
            sink.close();
        }
        assertEquals(HEADER + SINGLE_HEADER_NO_BASE + SINGLE_CONSTRUCTOR_NO_BASE, sink.result());
    }
   
    /**
     * Test that single constructor with base does its thing properly
     */
    @Test
    public void testSingleConstructorWithBase() {
        final DataType fooBar = new DataType(Util.list(_JavaDocComment("/**", list(_JDWhiteSpace(" "), _JDWord("hello"), _JDWhiteSpace(" ")), Util.<JDTagSection>list(), "*/")), ANNOTATIONS, "FooBar", Util.<String>list(), _Some(_ClassType("FooA", NO_TYPE_ARGS)), list(_ClassType("FooB", NO_TYPE_ARGS), _ClassType("FooC", NO_TYPE_ARGS)), list(new Constructor(NO_COMMENTS, "Foo", list(
                new Arg(Util.<ArgModifier>list(), _Ref(_ClassType("Integer", Util.<RefType> list())), "yeah"),
                new Arg(Util.<ArgModifier>list(), _Ref(_ClassType("String", Util.<RefType> list())), "hmmm")))));

        final StringSink sink = new StringSink("test");
        try {
            final DataTypeEmitter emitter = new StandardDataTypeEmitter(new DummyClassBodyEmitter(),
                    new DummyConstructorEmitter());

            emitter.emit(sink, fooBar, HEADER);
        } finally {
            sink.close();
        }
        assertEquals(HEADER + SINGLE_HEADER_WITH_BASE + SINGLE_CONSTRUCTOR_WITH_BASE, sink.result());
    }
   
}
