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
package com.pogofish.jadt.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.fail;

import java.io.IOException;

import org.junit.Test;


/**
 * Test the ExceptionAction to make sure it squashes exceptions properly
 *
 * @author jiry
 */
public class ExceptionActionTest {
    /**
     * What happens when there's no exception?
     */
    @Test
    public void testNoException() {
        final String result = Util.execute(new ExceptionAction<String>() {

            @Override
            public String doAction() throws IOException {
                return "hello";
            }
        });

        assertEquals("hello", result);
    }

    /**
     * What happens when there is a checked exception?
     */
    @Test
    public void testException() {
        final IOException thrown = new IOException("uh oh");
        try {
            final String result = Util.execute(new ExceptionAction<String>() {

                @Override
                public String doAction() throws IOException {
                    throw thrown;
                }
            });
            fail("Execution did not cause an exception got " + result);
        } catch (RuntimeException e) {
            assertSame("Contained exception was the wrong ", e.getCause(), thrown);
        }
    }

    /**
     * What happens when there is an unchecked exception?
     */
    @Test
    public void testRuntimeException() {
        final RuntimeException thrown = new RuntimeException("uh oh");
        try {
            final String result = Util.execute(new ExceptionAction<String>() {

                @Override
                public String doAction() {
                    throw thrown;
                }
            });
            fail("Execution did not cause an exception got " + result);
        } catch (RuntimeException e) {
            assertSame("Exception was the wrong ", e, thrown);
        }
    }
    
    /**
     * What happens when there is an unchecked exception?
     */
    @Test
    public void testError() {
        final Error thrown = new Error("uh oh");
        try {
            final String result = Util.execute(new ExceptionAction<String>() {

                @Override
                public String doAction() {
                    throw thrown;
                }
            });
            fail("Execution did not cause an exception got " + result);
        } catch (Error e) {
            assertSame("Exception was the wrong ", e, thrown);
        }
    }


}
