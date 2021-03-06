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
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;

import java.util.List;
import java.util.Map;

import org.junit.Test;


/**
 * Test that the factory factory actually factories
 *
 * @author jiry
 */
public class StringSinkFactoryFactoryTest {
    /**
     * Make sure the results map is correct
     */
    @Test
    public void test() {
        final StringSinkFactoryFactory ugh = new StringSinkFactoryFactory();
        
        final StringSinkFactory sf1 = ugh.createSinkFactory("baseDir1");
        final StringSinkFactory sf2 = ugh.createSinkFactory("baseDir1");
        final StringSinkFactory sf3 = ugh.createSinkFactory("baseDir2");
        
        assertNotSame(sf1, sf2);
        assertNotSame(sf2, sf3);
        assertNotSame(sf1, sf3);
        
        final Map<String, List<StringSinkFactory>> results = ugh.results();
        assertEquals(2, results.size());
        
        final List<StringSinkFactory> result1 = results.get("baseDir1");
        assertEquals(2, result1.size());
        assertSame(sf1, result1.get(0));
        assertSame(sf2, result1.get(1));
        
        final List<StringSinkFactory> result2 = results.get("baseDir2");
        assertEquals(1, result2.size());
        assertSame(sf3, result2.get(0));
    }
}
