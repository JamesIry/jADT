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
package pogofish.jadt.emitter;

import static org.junit.Assert.assertEquals;
import static pogofish.jadt.ast.PrimitiveType._IntType;
import static pogofish.jadt.ast.RefType._ClassType;
import static pogofish.jadt.ast.Type._Primitive;
import static pogofish.jadt.ast.Type._Ref;
import static pogofish.jadt.util.Util.list;

import java.util.Map;

import org.junit.Test;

import pogofish.jadt.ast.*;
import pogofish.jadt.target.StringTargetFactory;
import pogofish.jadt.util.Util;


public class DocEmitterTest {
    private static final String FULL_HEADER =
    "package some.package;\n" +
    "\n" +
    "import wow.man;\n" +
    "import flim.flam;\n" +
    "\n" +
    "/*\n" +
    "This file was generated based on EmitterTest. Please do not modify directly.\n" +
    "\n" +
    "The source was parsed as: \n" +
    "\n" +
    "package some.package\n" +
    "\n" +
    "import wow.man\n" +
    "import flim.flam\n" +
    "\n" +
    "FooBar =\n" +
    "    Foo(int yeah, String hmmm)\n" +
    "  | Bar\n" +
    "Whatever =\n" +
    "    Whatever\n" +
    "\n" +
    "*/\n";

    private static final String NO_PACKAGE_HEADER =
    "import wow.man;\n" +
    "import flim.flam;\n" +
    "\n" +
    "/*\n" +
    "This file was generated based on EmitterTest. Please do not modify directly.\n" +
    "\n" +
    "The source was parsed as: \n" +
    "\n" +
    "import wow.man\n" +
    "import flim.flam\n" +
    "\n" +
    "FooBar =\n" +
    "    Foo(int yeah, String hmmm)\n" +
    "  | Bar\n" +
    "Whatever =\n" +
    "    Whatever\n" +
    "\n" +
    "*/\n";

    
    private static final String NO_IMPORTS_HEADER =
    "package some.package;\n" +
    "\n" +
    "/*\n" +
    "This file was generated based on EmitterTest. Please do not modify directly.\n" +
    "\n" +
    "The source was parsed as: \n" +
    "\n" +
    "package some.package\n" +
    "\n" +
    "FooBar =\n" +
    "    Foo(int yeah, String hmmm)\n" +
    "  | Bar\n" +
    "Whatever =\n" +
    "    Whatever\n" +
    "\n" +
    "*/\n";
    
    private static final String FOOBAR = 
    "FooBar";
    
    private static final String WHATEVER =
    "Whatever";
    
    @Test
    public void testFull() {
        final Doc doc = new Doc("EmitterTest", "some.package", list("wow.man", "flim.flam"), list(
                new DataType("FooBar", list(
                        new Constructor("Foo", list(
                                new Arg(_Primitive(_IntType), "yeah"),
                                new Arg(_Ref(_ClassType("String", Util.<RefType>list())), "hmmm")
                        )),
                        new Constructor("Bar", Util.<Arg>list())
                )),
                new DataType("Whatever", list(
                        new Constructor("Whatever", Util.<Arg>list())
                ))
                
        ));
        final StringTargetFactory factory = new StringTargetFactory("whatever");
        final DocEmitter emitter = new StandardDocEmitter(new DummyDataTypeEmitter());
        emitter.emit(factory, doc);
        final Map<String, String> results = factory.getResults();
        assertEquals("Got the wrong number of results", 2, results.size());
        final String foobar = results.get("some.package.FooBar");
        assertEquals(FULL_HEADER+FOOBAR, foobar);
        assertEquals(FULL_HEADER+WHATEVER, results.get("some.package.Whatever"));
    }

    @Test
    public void testNoImports() {
        final Doc doc = new Doc("EmitterTest", "some.package", Util.<String>list(), list(
                new DataType("FooBar", list(
                        new Constructor("Foo", list(
                                new Arg(_Primitive(_IntType), "yeah"),
                                new Arg(_Ref(_ClassType("String", Util.<RefType>list())), "hmmm")
                        )),
                        new Constructor("Bar", Util.<Arg>list())
                )),
                new DataType("Whatever", list(
                        new Constructor("Whatever", Util.<Arg>list())
                ))
                
        ));
        final StringTargetFactory factory = new StringTargetFactory("whatever");
        final DocEmitter emitter = new StandardDocEmitter(new DummyDataTypeEmitter());
        emitter.emit(factory, doc);
        final Map<String, String> results = factory.getResults();
        assertEquals("Got the wrong number of results", 2, results.size());
        final String foobar = results.get("some.package.FooBar");
        assertEquals(NO_IMPORTS_HEADER+FOOBAR, foobar);
        assertEquals(NO_IMPORTS_HEADER+WHATEVER, results.get("some.package.Whatever"));
    }
    
    @Test
    public void testNoPackage() {
        final Doc doc = new Doc("EmitterTest", "", list("wow.man", "flim.flam"), list(
                new DataType("FooBar", list(
                        new Constructor("Foo", list(
                                new Arg(_Primitive(_IntType), "yeah"),
                                new Arg(_Ref(_ClassType("String", Util.<RefType>list())), "hmmm")
                        )),
                        new Constructor("Bar", Util.<Arg>list())
                )),
                new DataType("Whatever", list(
                        new Constructor("Whatever", Util.<Arg>list())
                ))
                
        ));
        final StringTargetFactory factory = new StringTargetFactory("whatever");
        final DocEmitter emitter = new StandardDocEmitter(new DummyDataTypeEmitter());
        emitter.emit(factory, doc);
        final Map<String, String> results = factory.getResults();
        assertEquals("Got the wrong number of results", 2, results.size());
        final String foobar = results.get("FooBar");
        assertEquals(NO_PACKAGE_HEADER+FOOBAR, foobar);
        assertEquals(NO_PACKAGE_HEADER+WHATEVER, results.get("Whatever"));
    }
}
