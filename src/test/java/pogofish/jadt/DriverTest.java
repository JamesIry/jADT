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
package pogofish.jadt;

import static org.junit.Assert.*;

import java.io.*;
import java.util.Set;

import org.junit.Test;

import pogofish.jadt.ast.DataType;
import pogofish.jadt.ast.Doc;
import pogofish.jadt.checker.*;
import pogofish.jadt.emitter.DocEmitter;
import pogofish.jadt.emitter.StandardDocEmitter;
import pogofish.jadt.parser.Parser;
import pogofish.jadt.parser.StandardParser;
import pogofish.jadt.source.*;
import pogofish.jadt.target.*;
import pogofish.jadt.util.Util;


public class DriverTest {
    
    private final class DummyParser implements Parser {
        private final Doc doc;
        private final String testString;

        private DummyParser(Doc doc, String testString) {
            this.doc = doc;
            this.testString = testString;
        }

        @Override
        public Doc parse(Source source) {
            assertEquals("srcInfo", source.getSrcInfo());                        
            try {
                assertEquals(testString, new BufferedReader(source.getReader()).readLine());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return doc;
        }
    }

    @Test
    public void testStandardConfig() {
        final JADT driver = JADT.standardConfigDriver();
        assertTrue("Standard driver had wrong source factory", driver.sourceFactory instanceof FileSourceFactory);
        assertTrue("Standard driver had wrong parser", driver.parser instanceof StandardParser);
        assertTrue("Standard driver had wrong checker", driver.checker instanceof StandardChecker);
        assertTrue("Standard driver had wrong emitter", driver.emitter instanceof StandardDocEmitter);
        assertTrue("Standard driver had wrong target factory factory", driver.factoryFactory instanceof FileTargetFactoryFactory);
    }

    @Test
    public void testDriverBadArgs() throws IOException {
        final String testString = "hello";
        final StringWriter writer = new StringWriter();
        try {
            final SourceFactory sourceFactory = new StringSourceFactory(testString);
            final Doc doc = new Doc("srcInfo", "pkg", Util.<String>list(), Util.<DataType>list());
            final DummyChecker checker = new DummyChecker();
            final JADT adt = new JADT(sourceFactory, new DummyParser(doc, testString), checker, new DocEmitter(){    
                @Override
                public void emit(TargetFactory factory, Doc arg) {
                }}, new StringTargetFactoryFactory());
            adt.parseAndEmit(new String[]{"srcInfo"});
            fail("Did not get an exception from bad arguments");
        } catch(IllegalArgumentException e) {
            // yay
        } finally {
            writer.close();
        }        
    }

    @Test
    public void testDriverGood() throws IOException {
        final String testString = "hello";
        final StringWriter writer = new StringWriter();
        try {
            final SourceFactory sourceFactory = new StringSourceFactory(testString);
            final Doc doc = new Doc("srcInfo", "pkg", Util.<String>list(), Util.<DataType>list());
            final DummyChecker checker = new DummyChecker();
            final JADT adt = new JADT(sourceFactory, new DummyParser(doc, testString), checker, new DocEmitter(){
    
                @Override
                public void emit(TargetFactory factory, Doc arg) {
                    assertSame(doc, arg);
                    writer.write("all good!");
                }}, new StringTargetFactoryFactory());
            adt.parseAndEmit(new String[]{"srcInfo", "whatever"});
            assertSame("Checker was not called", doc, checker.lastDoc());
        } finally {
            writer.close();
        }
        assertEquals("all good!", writer.toString());
    }
    
    @Test
    public void testDriverSemanticIssue() throws IOException {
        final String testString = "hello";
        final StringWriter writer = new StringWriter();
        try {
            final Doc doc = new Doc("srcInfo", "pkg", Util.<String>list(), Util.<DataType>list());
            final SourceFactory sourceFactory = new StringSourceFactory(testString);
            final Checker checker = new Checker() {
                @Override
                public Set<SemanticException> check(Doc doc) {
                    return Util.<SemanticException>set(new DuplicateConstructorException("Foo", "Bar"), new ConstructorDataTypeConflictException("Foo", "Foo"));
                }
                
            };
            final JADT adt = new JADT(sourceFactory, new DummyParser(doc, testString), checker, new DocEmitter(){
    
                @Override
                public void emit(TargetFactory factory, Doc arg) {
                    assertSame(doc, arg);
                    writer.write("all good!");
                }}, new StringTargetFactoryFactory());
            adt.parseAndEmit("srcInfo", "something");
            fail("Did not get a SemanticExceptions");
        } catch (SemanticExceptions e) {
            // yay
        } finally {
            writer.close();
        }
        assertEquals("", writer.toString());
    }
    
}
