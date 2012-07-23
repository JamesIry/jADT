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

import static com.pogofish.jadt.ast.JDToken._JDWhiteSpace;
import static com.pogofish.jadt.ast.JDToken._JDWord;
import static com.pogofish.jadt.ast.JavaComment._JavaDocComment;
import static com.pogofish.jadt.ast.PrimitiveType._IntType;
import static com.pogofish.jadt.ast.RefType._ClassType;
import static com.pogofish.jadt.ast.Type._Primitive;
import static com.pogofish.jadt.ast.Type._Ref;
import static com.pogofish.jadt.util.Util.list;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.pogofish.jadt.ast.Arg;
import com.pogofish.jadt.ast.ArgModifier;
import com.pogofish.jadt.ast.Constructor;
import com.pogofish.jadt.ast.JDTagSection;
import com.pogofish.jadt.ast.RefType;
import com.pogofish.jadt.sink.StringSink;
import com.pogofish.jadt.util.Util;


/**
 * Test the StandardConstructorEmitter
 *
 * @author jiry
 */
public class ConstructorEmitterTest {
    private static final String CONSTRUCTOR_CLASS = 
    "   /** hello */\n" +
    "   public static final class Foo/* type arguments */ extends NonPrimitive/* type arguments */ {\n" +
    "      /* constructor method Foo*/\n" +
    "\n" +
    "      @Override\n" +
    "      public <ResultType> ResultType match(MatchBlock/* type arguments */ matchBlock) { return matchBlock._case(this); }\n" +
    "\n" +
    "      @Override\n" +
    "      public void _switch(SwitchBlock/* type arguments */ switchBlock) { switchBlock._case(this); }\n" +
    "\n" +
    "      /* hashCode method Foo*/\n" +
    "\n" +
    "      /* equals method Foo*/\n" +
    "\n" +
    "      /* toString method Foo*/\n" +
    "\n" +
    "   }";

    
    private static final String FACTORY = 
    "/* constructor factory SomeDataType Foo Foo*/";    
    
    /**
     * Create a factory
     */
    @Test
    public void testFactory() {
        final Constructor constructor = new Constructor(Util.list(_JavaDocComment("/**", list(_JDWhiteSpace(" "), _JDWord("hello"), _JDWhiteSpace(" ")), Util.<JDTagSection>list(), "*/")), "Foo", list(new Arg(Util.<ArgModifier>list(), _Ref(_ClassType("String", Util.<RefType>list())), "um"), new Arg(Util.<ArgModifier>list(), _Primitive(_IntType()), "yeah")));

        final StringSink sink = new StringSink("test");
        try {
            final ConstructorEmitter emitter = new StandardConstructorEmitter(new DummyClassBodyEmitter());

            emitter.constructorFactory(sink, "SomeDataType", Util.<String>list(), constructor);
        } finally {
            sink.close();
        }
        assertEquals(FACTORY, sink.result());
    }
    
    /**
     * Create a constructor class
     */
    @Test
    public void testConstrucorDeclaration() {
        final Constructor constructor = new Constructor(Util.list(_JavaDocComment("/**", list(_JDWhiteSpace(" "), _JDWord("hello"), _JDWhiteSpace(" ")), Util.<JDTagSection>list(), "*/")), "Foo", list(new Arg(Util.<ArgModifier>list(), _Ref(_ClassType("String", Util.<RefType>list())), "um"), new Arg(Util.<ArgModifier>list(), _Primitive(_IntType()), "yeah")));

        final StringSink sink = new StringSink("test");
        try {
            final ConstructorEmitter emitter = new StandardConstructorEmitter(new DummyClassBodyEmitter());

            emitter.constructorDeclaration(sink, constructor, "NonPrimitive", Util.<String>list());
        } finally {
            sink.close();
        }
        assertEquals(CONSTRUCTOR_CLASS, sink.result());
    }
}
