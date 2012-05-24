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
package pogofish.jadt.util;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;

/**
 * Test the IOExceptionAction to make sure it squashes exceptions properly
 *
 * @author jiry
 */
public class IOExceptionActionTest {
    /**
     * What happens when there's no exception?
     */
    @Test
    public void testNoException() {
        final String result = new IOExceptionAction<String>() {

            @Override
            public String doAction() throws IOException {
                return "hello";
            }
        }.execute();

        assertEquals("hello", result);
    }

    /**
     * What happens when there is one?
     */
    @Test
    public void testException() {
        try {
            final String result = new IOExceptionAction<String>() {

                @Override
                public String doAction() throws IOException {
                    throw new IOException("uh oh");
                }
            }.execute();
            fail("Execution did not cause an exception got " + result);
        } catch (RuntimeException e) {
            assertTrue("Contained exception was the wrong type", e.getCause() instanceof IOException);
        }
    }

}
