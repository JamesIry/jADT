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
import static pogofish.jadt.util.Util.list;

import java.io.IOException;
import java.io.StringReader;

import org.junit.Test;

import pogofish.jadt.ast.*;
import pogofish.jadt.util.Util;


public class ParserTest {

    @Test
    public void testEmpty() throws IOException {
        final Parser parser = new StandardParser();
        final Doc doc = parser.parse("ParserTest", new StringReader(""));

        assertEquals(new Doc("ParserTest", "", Util.<String> list(), Util.<DataType> list()), doc);
    }

    @Test
    public void testSimplePackage() throws IOException {
        final Parser parser = new StandardParser();
        final Doc doc = parser.parse("ParserTest", new StringReader("package hello"));

        assertEquals(new Doc("ParserTest", "hello", Util.<String> list(), Util.<DataType> list()), doc);
    }

    @Test
    public void testComplexPackage() throws IOException {
        final Parser parser = new StandardParser();
        final Doc doc = parser.parse("ParserTest", new StringReader("package hello.world"));

        assertEquals(new Doc("ParserTest", "hello.world", Util.<String> list(), Util.<DataType> list()), doc);
    }

    @Test
    public void testImports() throws IOException {
        final Parser parser = new StandardParser();
        final Doc doc = parser.parse("ParserTest", new StringReader("import wow.man import flim.flam"));

        assertEquals(new Doc("ParserTest", "", list("wow.man", "flim.flam"), Util.<DataType> list()), doc);
    }

    @Test
    public void testNoArgs() throws IOException {
        final Parser parser = new StandardParser();
        final Doc doc = parser.parse("ParserTest", new StringReader("whatever = whatever"));

        assertEquals(new Doc("ParserTest", "", Util.<String> list(), list(new DataType("whatever",
                list(new Constructor("whatever", Util.<Arg> list()))))), doc);
    }

    @Test
    public void testArgs() throws IOException {
        final Parser parser = new StandardParser();
        final Doc doc = parser.parse("ParserTest", new StringReader("FooBar = FooBar(int hello, String world)"));

        assertEquals(new Doc("ParserTest", "", Util.<String> list(), list(new DataType("FooBar", list(new Constructor(
                "FooBar", list(new Arg("int", "hello"), new Arg("String", "world"))))))), doc);
    }

    @Test
    public void testFull() throws IOException {
        final Parser parser = new StandardParser();
        final Doc doc = parser.parse("ParserTest", new StringReader(
                "package hello.world import wow.man import flim.flam "
                        + "FooBar = foo | bar(int hey, String yeah) " + "whatever = whatever"));

        assertEquals(
                new Doc("ParserTest", "hello.world", list("wow.man", "flim.flam"), list(
                        new DataType("FooBar", Util.list(new Constructor("foo", Util.<Arg> list()), new Constructor(
                                "bar", list(new Arg("int", "hey"), new Arg("String", "yeah"))))), new DataType(
                                "whatever", list(new Constructor("whatever", Util.<Arg> list()))))), doc);
    }

    @Test
    public void testParameterizedArg() throws IOException {
        final Parser parser = new StandardParser();
        final Doc doc = parser.parse("ParserTest", new StringReader(
                "package hello.world import wow.man import flim.flam "
                        + "FooBar = foo | bar(int hey, Map<String, Cow> yeah) " + "whatever = whatever"));

        assertEquals(
                new Doc("ParserTest", "hello.world", list("wow.man", "flim.flam"), list(
                        new DataType("FooBar", Util.list(new Constructor("foo", Util.<Arg> list()), new Constructor(
                                "bar", list(new Arg("int", "hey"), new Arg("Map<String, Cow>", "yeah"))))), new DataType(
                                "whatever", list(new Constructor("whatever", Util.<Arg> list()))))), doc);
    }
    
    @Test
    public void testJavaKeyword() throws IOException {
        
        try {
            final Parser parser = new StandardParser();
            final Doc doc = parser.parse("ParserTest", new StringReader(
                    "package hello.world import wow.man import flim.flam "
                            + "ClassType = class | interface | enum"));
            fail("Expected a syntax exception but got " + doc);
        } catch (SyntaxException e) {
            // yay, that's good
        }
    }

}
