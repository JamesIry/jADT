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
package com.pogofish.jadt.sink;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Test;

import com.pogofish.jadt.sink.StringSink;


/**
 * Test StringSink
 *
 * @author jiry
 */
public class StringSinkTest {
    /**
     * Test that a StringSink produces the expected result
     */
    @Test
    public void testHappy() {
        final StringSink sink = new StringSink("test");
        try {
            sink.write("world");
        } finally {
            sink.close();
        }
        assertEquals("world", sink.result());
    }
    
    /**
     * Test that a StringSink throws an exception if not closed before fetching its result
     */
    @Test
    public void testExceptionIfNotClosed() {
        final StringSink sink = new StringSink("test");
        try {
            sink.write("world");
            final String result = sink.result();
            fail("Did not get exception on unclosed sink, got " + result);
        } catch (RuntimeException e) {
            assertEquals("sink was not closed", e.getMessage());
        } finally {
            sink.close();
        }
    }
}
