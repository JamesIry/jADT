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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.Collections;

import org.junit.Test;

import com.pogofish.jadt.ast.DataType;
import com.pogofish.jadt.ast.Doc;
import com.pogofish.jadt.emitter.DocEmitter;
import com.pogofish.jadt.emitter.DummyDocEmitter;
import com.pogofish.jadt.sink.StringSinkFactory;


/**
 * Tests that the DummyDocEmitter will misbehave properly
 *
 * @author jiry
 */
public class DummyDocEmitterTest {
    /**
     * Happy path should give back the srcInfo
     */
    @Test
    public void testHappy() {
        final Doc testDoc = new Doc("some source", "", Collections.<String>emptyList(),Collections.<DataType>emptyList());
        final DocEmitter dummyDocEmitter = new DummyDocEmitter(testDoc, "SomeClass");
        final StringSinkFactory factory = new StringSinkFactory("baseDir");
        dummyDocEmitter.emit(factory, testDoc);
        assertEquals("some source", factory.getResults().get("SomeClass"));
    }
    
    /**
     * Sending in a different doc should cause an exception
     */
    @Test
    public void testWrongDoc() {
        final Doc testDoc = new Doc("some source", "", Collections.<String>emptyList(),Collections.<DataType>emptyList());
        final Doc doc = new Doc("some source", "", Collections.<String>emptyList(),Collections.<DataType>emptyList());
        final DocEmitter dummyDocEmitter = new DummyDocEmitter(testDoc, "SomeClass");
        final StringSinkFactory factory = new StringSinkFactory("baseDir");
        try {
            dummyDocEmitter.emit(factory, doc);
            fail("Did not get an exception with the wrong doc, got " + factory.getResults().get("SomeClass"));
        } catch (RuntimeException e) {
            // yay
        }
    }
    
}
