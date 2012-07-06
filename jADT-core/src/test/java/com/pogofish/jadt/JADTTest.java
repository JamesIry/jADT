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

import static com.pogofish.jadt.errors.SemanticError._ConstructorDataTypeConflict;
import static com.pogofish.jadt.errors.SemanticError._DuplicateConstructor;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

import com.pogofish.jadt.checker.StandardChecker;
import com.pogofish.jadt.emitter.StandardDocEmitter;
import com.pogofish.jadt.errors.SemanticError;
import com.pogofish.jadt.errors.SyntaxError;
import com.pogofish.jadt.errors.UserError;
import com.pogofish.jadt.parser.StandardParser;
import com.pogofish.jadt.sink.FileSinkFactoryFactory;
import com.pogofish.jadt.sink.StringSinkFactoryFactory;
import com.pogofish.jadt.source.FileSourceFactory;
import com.pogofish.jadt.util.TestUtil;
import com.pogofish.jadt.util.Util;

/**
 * Test for the main jADT driver
 *
 * @author jiry
 */
public class JADTTest {
    
    private static final List<SyntaxError> NO_SYNTAX_ERRORS = Collections.<SyntaxError>emptyList();
    private static final List<SemanticError> NO_SEMANTIC_ERRORS = Collections.<SemanticError>emptyList();
    private static final String[] GOOD_ARGS = new String[]{JADT.TEST_SRC_INFO, JADT.TEST_DIR};

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
        assertTrue("Standard driver had wrong sink factory factory", driver.factoryFactory instanceof FileSinkFactoryFactory);
    }
    
    /**
     * Create a dummy configged jADT based on the provided checker, send it the provided args and return the
     * resulting string (or throw the resulting exception 
     */
    private String testWithDummyJADT(String[] args, List<SyntaxError> syntaxErrors, List<SemanticError> semanticErrors) {
        final StringSinkFactoryFactory factory = new StringSinkFactoryFactory();
        JADT.createDummyJADT(syntaxErrors, semanticErrors, JADT.TEST_SRC_INFO, factory).parseAndEmit(args);
        return factory.results().get(JADT.TEST_DIR).get(0).getResults().get(JADT.TEST_CLASS_NAME);
    }

    
    /**
     * Ensure that sending bad args to parseAndEmit gets an IllegalArgumentException
     */
    @Test
    public void testDriverBadArgs() {
        
        try {
            final String result = testWithDummyJADT(new String[]{JADT.TEST_SRC_INFO}, NO_SYNTAX_ERRORS, NO_SEMANTIC_ERRORS);
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
        final String result = testWithDummyJADT(GOOD_ARGS, NO_SYNTAX_ERRORS, NO_SEMANTIC_ERRORS);
        
        assertEquals(JADT.TEST_SRC_INFO, result);
    }
    
    /**
     * Test that syntax errors from the parser get bundled up and thrown properly
     */
    @Test
    public void testDriverSyntacticIssue() {
        final List<SyntaxError> errors = Util.list(SyntaxError._UnexpectedToken("flurb", "blurb", 42));
        
        try {
            final String result = testWithDummyJADT(GOOD_ARGS, errors, NO_SEMANTIC_ERRORS);
            fail("Did not get an exception from syntax errors, got " + result);
        } catch (JADTUserErrorsException e) {
            final List<UserError> userErrors = e.getErrors();
            assertEquals(errors.size(), userErrors.size());
            for (SyntaxError error : errors) {
                final UserError userError = UserError._Syntactic(error);
                assertTrue("User errrors did not contain " + userError, userErrors.contains(userError));
            }
        }
    }
    
    /**
     * Test that semantic errors from the checker get bundled up and thrown properly
     */
    @Test
    public void testDriverSemanticIssue() {
        final List<SemanticError> errors = Util.<SemanticError>list(_DuplicateConstructor("Foo", "Bar"), _ConstructorDataTypeConflict("Foo"));
        try {
            final String result = testWithDummyJADT(GOOD_ARGS, NO_SYNTAX_ERRORS, errors);
            fail("Did not get an exception, got " + result);
        } catch (JADTUserErrorsException e) {
            final List<UserError> userErrors = e.getErrors();
            assertEquals(errors.size(), userErrors.size());
            for (SemanticError error : errors) {
                final UserError userError = UserError._Semantic(error);
                assertTrue("User errrors did not contain " + userError, userErrors.contains(userError));
            }
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
                final File tmpDir = TestUtil.createTmpDir();
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
