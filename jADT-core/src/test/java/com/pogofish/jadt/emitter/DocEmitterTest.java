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

import static com.pogofish.jadt.ast.ASTConstants.EMPTY_PKG;
import static com.pogofish.jadt.ast.ASTConstants.NO_COMMENTS;
import static com.pogofish.jadt.ast.ASTConstants.NO_IMPORTS;
import static com.pogofish.jadt.ast.JavaComment._JavaDocComment;
import static com.pogofish.jadt.ast.JavaComment._JavaEOLComment;
import static com.pogofish.jadt.ast.JavaComment._JavaMultiLineComment;
import static com.pogofish.jadt.ast.PrimitiveType._IntType;
import static com.pogofish.jadt.ast.RefType._ClassType;
import static com.pogofish.jadt.ast.Type._Primitive;
import static com.pogofish.jadt.ast.Type._Ref;
import static com.pogofish.jadt.util.Util.list;
import static org.junit.Assert.assertEquals;

import java.util.Map;

import org.junit.Test;

import com.pogofish.jadt.Version;
import com.pogofish.jadt.ast.Arg;
import com.pogofish.jadt.ast.ArgModifier;
import com.pogofish.jadt.ast.Constructor;
import com.pogofish.jadt.ast.DataType;
import com.pogofish.jadt.ast.Doc;
import com.pogofish.jadt.ast.Imprt;
import com.pogofish.jadt.ast.Pkg;
import com.pogofish.jadt.ast.RefType;
import com.pogofish.jadt.sink.StringSinkFactory;
import com.pogofish.jadt.util.Util;

/**
 * Test the StandardDocEmitter.  Only shallow testing is performed here, the pieces of the doc emitter
 * are tested more thoroughly elsewhere
 *
 * @author jiry
 */
public class DocEmitterTest {
	private static final String VERSION = new Version().getVersion();
	private static final String BOILERPLATE = 
    "This file was generated based on EmitterTest using jADT version " + VERSION + " http://jamesiry.github.com/jADT/ . Please do not modify directly.\n" +
    "\n" +
    "The source was parsed as: \n" +
    "\n";

	
    private static final String FULL_HEADER =
    "/** hello */\n" +
    "package some.package;\n" +
    "\n" +
    "/* hello */\n" +
    "import wow.man;\n" +
    "// hello\n" +
    "import flim.flam;\n" +
    "\n" +
    "/*\n" +
    BOILERPLATE;

    private static final String NO_PACKAGE_HEADER =
    "import wow.man;\n" +
    "import flim.flam;\n" +
    "\n" +
    "/*\n" +
    BOILERPLATE;

    
    private static final String NO_IMPORTS_HEADER =
    "package some.package;\n" +
    "\n" +
    "/*\n" +
    BOILERPLATE;
    
    private static final String FOOBAR = 
    "FooBar";
    
    private static final String WHATEVER =
    "Whatever";
    
    /**
     * Test a reasonably fully document with pacakge, imports, and datatypes
     */
    @Test
    public void testFull() {
        final Doc doc = new Doc("EmitterTest", Pkg._Pkg(Util.list(_JavaDocComment("/** hello */")), "some.package"), list(Imprt._Imprt(Util.list(_JavaMultiLineComment("/* hello */")), "wow.man"), Imprt._Imprt(Util.list(_JavaEOLComment("// hello")), "flim.flam")), list(
                new DataType(NO_COMMENTS, "FooBar", Util.<String>list(), list(
                        new Constructor(NO_COMMENTS, "Foo", list(
                                new Arg(Util.<ArgModifier>list(), _Primitive(_IntType()), "yeah"),
                                new Arg(Util.<ArgModifier>list(), _Ref(_ClassType("String", Util.<RefType>list())), "hmmm")
                        )),
                        new Constructor(NO_COMMENTS, "Bar", Util.<Arg>list())
                )),
                new DataType(NO_COMMENTS, "Whatever", Util.<String>list(), list(
                        new Constructor(NO_COMMENTS, "Whatever", Util.<Arg>list())
                ))
                
        ));
        final StringSinkFactory factory = new StringSinkFactory("whatever");
        final DocEmitter emitter = new StandardDocEmitter(new DummyDataTypeEmitter());
        emitter.emit(factory, doc);
        final Map<String, String> results = factory.getResults();
        assertEquals("Got the wrong number of results", 2, results.size());
        final String foobar = results.get("some.package.FooBar");
        assertEquals(FULL_HEADER+FOOBAR, foobar);
        assertEquals(FULL_HEADER+WHATEVER, results.get("some.package.Whatever"));
    }

    /**
     * Test a doc with no imports
     */
    @Test
    public void testNoImports() {
        final Doc doc = new Doc("EmitterTest", Pkg._Pkg(NO_COMMENTS, "some.package"), NO_IMPORTS, list(
                new DataType(NO_COMMENTS, "FooBar", Util.<String>list(), list(
                        new Constructor(NO_COMMENTS, "Foo", list(
                                new Arg(Util.<ArgModifier>list(), _Primitive(_IntType()), "yeah"),
                                new Arg(Util.<ArgModifier>list(), _Ref(_ClassType("String", Util.<RefType>list())), "hmmm")
                        )),
                        new Constructor(NO_COMMENTS, "Bar", Util.<Arg>list())
                )),
                new DataType(NO_COMMENTS, "Whatever", Util.<String>list(), list(
                        new Constructor(NO_COMMENTS, "Whatever", Util.<Arg>list())
                ))
                
        ));
        final StringSinkFactory factory = new StringSinkFactory("whatever");
        final DocEmitter emitter = new StandardDocEmitter(new DummyDataTypeEmitter());
        emitter.emit(factory, doc);
        final Map<String, String> results = factory.getResults();
        assertEquals("Got the wrong number of results", 2, results.size());
        final String foobar = results.get("some.package.FooBar");
        assertEquals(NO_IMPORTS_HEADER+FOOBAR, foobar);
        assertEquals(NO_IMPORTS_HEADER+WHATEVER, results.get("some.package.Whatever"));
    }
    
    /**
     * Test a doc with no package declaration
     */
    @Test
    public void testNoPackage() {
        final Doc doc = new Doc("EmitterTest", EMPTY_PKG, list(Imprt._Imprt(NO_COMMENTS, "wow.man"), Imprt._Imprt(NO_COMMENTS, "flim.flam")), list(
                new DataType(NO_COMMENTS, "FooBar", Util.<String>list(), list(
                        new Constructor(NO_COMMENTS, "Foo", list(
                                new Arg(Util.<ArgModifier>list(), _Primitive(_IntType()), "yeah"),
                                new Arg(Util.<ArgModifier>list(), _Ref(_ClassType("String", Util.<RefType>list())), "hmmm")
                        )),
                        new Constructor(NO_COMMENTS, "Bar", Util.<Arg>list())
                )),
                new DataType(NO_COMMENTS, "Whatever", Util.<String>list(), list(
                        new Constructor(NO_COMMENTS, "Whatever", Util.<Arg>list())
                ))
                
        ));
        final StringSinkFactory factory = new StringSinkFactory("whatever");
        final DocEmitter emitter = new StandardDocEmitter(new DummyDataTypeEmitter());
        emitter.emit(factory, doc);
        final Map<String, String> results = factory.getResults();
        assertEquals("Got the wrong number of results", 2, results.size());
        final String foobar = results.get("FooBar");
        assertEquals(NO_PACKAGE_HEADER+FOOBAR, foobar);
        assertEquals(NO_PACKAGE_HEADER+WHATEVER, results.get("Whatever"));
    }
}
