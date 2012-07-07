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
import static com.pogofish.jadt.ast.JDToken._JDWhiteSpace;
import static com.pogofish.jadt.ast.JDToken._JDWord;
import static com.pogofish.jadt.ast.JavaComment._JavaDocComment;
import static com.pogofish.jadt.ast.JavaDoc._JavaDoc;
import static com.pogofish.jadt.ast.RefType._ClassType;
import static com.pogofish.jadt.ast.Type._Ref;
import static com.pogofish.jadt.util.Util.list;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.pogofish.jadt.ast.Arg;
import com.pogofish.jadt.ast.ArgModifier;
import com.pogofish.jadt.ast.Constructor;
import com.pogofish.jadt.ast.DataType;
import com.pogofish.jadt.ast.JDTagSection;
import com.pogofish.jadt.ast.RefType;
import com.pogofish.jadt.sink.StringSink;
import com.pogofish.jadt.util.Util;


/**
 * Test the StandardDataTypeEmitter
 *
 * @author jiry
 */
public class DataTypeEmitterTest {
    private static final String HEADER = "/*header*/\n";
    private static final String MULTI_HEADER = "FooBar =\n" +
    "    Foo(Integer yeah, String hmmm)\n" +
    "  | Bar\n" +
    "*/\n";
    private static final String MULTI_CONSTRUCTOR = 
    "/** hello */\n" +
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
    
    private static final String SINGLE_CONSTRUCTOR = 
    "/** hello */\n" +
    "public final class FooBar/* type arguments */ {\n" +
    "\n" +
    "/* constructor factory FooBar Foo FooBar*/\n" +
    "\n" +
    "/* constructor method FooBar*/\n" +
    "\n" +
    "/* hashCode method FooBar*/\n" +
    "\n" +
    "/* equals method FooBar*/\n" +
    "\n" +
    "/* toString method FooBar*/\n" +
    "\n" +
    "}";
    private static final String SINGLE_HEADER =
    "FooBar =\n" +
    "    Foo(Integer yeah, String hmmm)\n" +
    "*/\n";
    
    /**
     * Test that multiple constructors does its thing correctly
     */
    @Test
    public void testMultipleConstructors() {
        final DataType fooBar = new DataType(Util.list(_JavaDocComment(_JavaDoc("/**", list(_JDWhiteSpace(" "), _JDWord("hello"), _JDWhiteSpace(" ")), Util.<JDTagSection>list(), "*/"))), "FooBar", Util.<String>list(), list(
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
        assertEquals(HEADER + MULTI_HEADER + MULTI_CONSTRUCTOR, sink.result());
    }
   
    /**
     * Test that single constructor does its thing properly
     */
    @Test
    public void testSingleConstructor() {
        final DataType fooBar = new DataType(Util.list(_JavaDocComment(_JavaDoc("/**", list(_JDWhiteSpace(" "), _JDWord("hello"), _JDWhiteSpace(" ")), Util.<JDTagSection>list(), "*/"))), "FooBar", Util.<String>list(), list(new Constructor(NO_COMMENTS, "Foo", list(
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
        assertEquals(HEADER + SINGLE_HEADER + SINGLE_CONSTRUCTOR, sink.result());
    }
   
}
