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
package com.pogofish.jadt.ant;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.Collections;

import org.apache.tools.ant.BuildException;
import org.junit.Test;

import com.pogofish.jadt.JADT;
import com.pogofish.jadt.ant.JADTAntTask;
import com.pogofish.jadt.ast.SemanticError;
import com.pogofish.jadt.checker.*;
import com.pogofish.jadt.target.StringTargetFactoryFactory;


/**
 * Unit test the ant task to make sure it does what it says on the tin
 *
 * @author jiry
 */
public class JADTAntTaskTest {
    /**
     * Make sure everything works in the happy path
     */
    @Test
    public void testHappy() {
        final JADTAntTask antTask = new JADTAntTask();
        final StringTargetFactoryFactory factory = new StringTargetFactoryFactory();        
        antTask.jadt = JADT.createDummyJADT(new DummyChecker(Collections.<SemanticError>emptySet()), JADT.TEST_SRC_INFO, factory);
        
        antTask.setSrcPath(JADT.TEST_SRC_INFO);
        antTask.setDestDir(JADT.TEST_DIR);
        antTask.execute();
        
        final String result = factory.results().get(JADT.TEST_DIR).get(0).getResults().get(JADT.TEST_CLASS_NAME);
        assertEquals(JADT.TEST_SRC_INFO, result);
    }
    
    /**
     * Make sure exceptions are handled properly
     */
    @Test
    public void testException() {
        final JADTAntTask antTask = new JADTAntTask();
        final StringTargetFactoryFactory factory = new StringTargetFactoryFactory();        
        antTask.jadt = JADT.createDummyJADT(new DummyChecker(Collections.<SemanticError>singleton(SemanticError._DuplicateConstructor("whatever", "something"))), JADT.TEST_SRC_INFO, factory);
        
        antTask.setSrcPath(JADT.TEST_SRC_INFO);
        antTask.setDestDir(JADT.TEST_DIR);
        try {
            antTask.execute();
            
            final String result = factory.results().get(JADT.TEST_DIR).get(0).getResults().get(JADT.TEST_CLASS_NAME);
            fail("Did not get exception, got " + result);
        } catch (BuildException e) {
            // yay
        }
    }
    
}
