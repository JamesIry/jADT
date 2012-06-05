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
package pogofish.jadt.target;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Test;

/**
 * Test StringTarget
 *
 * @author jiry
 */
public class StringTargetTest {
    /**
     * Test that a StringTarget produces the expected result
     */
    @Test
    public void testHappy() {
        final StringTarget target = new StringTarget("test");
        try {
            target.write("world");
        } finally {
            target.close();
        }
        assertEquals("world", target.result());
    }
    
    /**
     * Test that a StringTarget throws an exception if not closed before fetching its result
     */
    @Test
    public void testExceptionIfNotClosed() {
        final StringTarget target = new StringTarget("test");
        try {
            target.write("world");
            final String result = target.result();
            fail("Did not get exception on unclosed target, got " + result);
        } catch (RuntimeException e) {
            assertEquals("target was not closed", e.getMessage());
        } finally {
            target.close();
        }
    }
}
