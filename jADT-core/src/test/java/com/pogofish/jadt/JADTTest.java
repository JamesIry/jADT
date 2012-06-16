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
package com.pogofish.jadt;

import static org.junit.Assert.*;

import java.io.*;
import java.util.Collections;

import org.junit.Test;

import com.pogofish.jadt.JADT;
import com.pogofish.jadt.SemanticExceptions;
import com.pogofish.jadt.ast.SemanticError;
import com.pogofish.jadt.checker.*;
import com.pogofish.jadt.emitter.StandardDocEmitter;
import com.pogofish.jadt.parser.StandardParser;
import com.pogofish.jadt.source.FileSourceFactory;
import com.pogofish.jadt.target.FileTargetFactoryFactory;
import com.pogofish.jadt.target.StringTargetFactoryFactory;
import com.pogofish.jadt.util.Util;

import static com.pogofish.jadt.ast.SemanticError.*;

/**
 * Test for the main jADT driver
 *
 * @author jiry
 */
public class JADTTest {
    
    /**
     * Ensure that the standard config jADT has all the right parts.  The various bits are tested separately
     */
    @Test
    public void testStandardConfig() {
        final JADT driver = JADT.standardConfigDriver();
        assertTrue("Standard driver had wrong source factory", driver.sourceFactory instanceof FileSourceFactory);
        assertTrue("Standard driver had wrong parser", driver.parser instanceof StandardParser);
        assertTrue("Standard driver had wrong checker", driver.checker instanceof StandardChecker);
        assertTrue("Standard driver had wrong emitter", driver.emitter instanceof StandardDocEmitter);
        assertTrue("Standard driver had wrong target factory factory", driver.factoryFactory instanceof FileTargetFactoryFactory);
    }
    
    /**
     * Create a dummy configged jADT based on the provided checker, send it the provided args and return the
     * resulting string (or throw the resulting exception 
     */
    private String testWithDummyJADT(String[] args, Checker checker) {
        final StringTargetFactoryFactory factory = new StringTargetFactoryFactory();
        JADT.createDummyJADT(checker, JADT.TEST_SRC_INFO, factory).parseAndEmit(args);
        return factory.results().get(JADT.TEST_DIR).get(0).getResults().get(JADT.TEST_CLASS_NAME);
    }


    
    /**
     * Ensure that sending bad args to parseAndEmit gets an IllegalArgumentException
     */
    @Test
    public void testDriverBadArgs() {
        
        try {
            final String result = testWithDummyJADT(new String[]{JADT.TEST_SRC_INFO}, new DummyChecker(Collections.<SemanticError>emptySet()));
            fail("Did not get an exception from bad arguments, got " + result);
        } catch(IllegalArgumentException e) {
            // yay
        }        
    }

    /**
     * Test the happy path using dummy everything
     */
    @Test
    public void testDriverGood() {
        final String result = testWithDummyJADT(new String[]{JADT.TEST_SRC_INFO, JADT.TEST_DIR}, new DummyChecker(Collections.<SemanticError>emptySet()));
        
        assertEquals(JADT.TEST_SRC_INFO, result);
    }
    
    /**
     * Test that semantic errors from the checker get bundled up and thrown properly
     */
    @Test
    public void testDriverSemanticIssue() {
        try {
            final Checker checker = new DummyChecker(Util.<SemanticError>set(_DuplicateConstructor("Foo", "Bar"), _ConstructorDataTypeConflict("Foo")));
            final String result = testWithDummyJADT(new String[]{JADT.TEST_SRC_INFO, JADT.TEST_DIR}, checker);
            fail("Did not get a SemanticErrors, got " + result);
        } catch (SemanticExceptions e) {
            // yay
        }
    }
    
    /**
     * Test the happy path of the main method.  That means outputting real files to the real file system.
     * Only minimal testing is done around source and output - all the various components are tested
     * more thoroughly elsewhere
     * 
     * @throws IOException
     */
    @Test
    public void testMain() throws IOException {
        final File srcFile = File.createTempFile("tmp", ".jadt");
        try {
            final BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(srcFile), "UTF-8"));
            try {
                writer.write("Foo = Foo");
                writer.close();
                final File tmpDir = Util.createTmpDir();
                try {
                   JADT.main(new String[]{srcFile.getAbsolutePath(), tmpDir.getAbsolutePath()});
                   final File outputFile = new File(tmpDir, "Foo.java");
                   try {
                       assertTrue("Could not find output file at " + outputFile.getAbsolutePath(), outputFile.exists());
                       final BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(outputFile), "UTF-8"));
                       try {
                           assertStartsWith("/*", reader.readLine());
                           assertStartsWith("This file was generated based on ", reader.readLine());
                       } finally {
                           reader.close();
                       }
                   } finally {
                       outputFile.delete();
                   }
                } finally {
                    tmpDir.delete();
                }
            } finally {
                writer.close();
            }
        } finally {
            srcFile.delete();
        }
    }
    
    /**
     * Assert that the actual string provided starts with the expected string
     * @param expected
     * @param actual
     */
    private void assertStartsWith(String expected, String actual) {
        assertTrue("Line was expected to start with '" + expected + "' but was '" + actual + "'", actual.startsWith(expected));
    }
    
}
