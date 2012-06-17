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
package com.pogofish.jadt.target;

import static org.junit.Assert.*;

import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.pogofish.jadt.target.StringTargetFactory;
import com.pogofish.jadt.target.StringTargetFactoryFactory;


/**
 * Test that the factory factory actually factories
 *
 * @author jiry
 */
public class StringTargetFactoryFactoryTest {
    /**
     * Make sure the results map is correct
     */
    @Test
    public void test() {
        final StringTargetFactoryFactory ugh = new StringTargetFactoryFactory();
        
        final StringTargetFactory sf1 = ugh.createTargetFactory("baseDir1");
        final StringTargetFactory sf2 = ugh.createTargetFactory("baseDir1");
        final StringTargetFactory sf3 = ugh.createTargetFactory("baseDir2");
        
        assertNotSame(sf1, sf2);
        assertNotSame(sf2, sf3);
        assertNotSame(sf1, sf3);
        
        final Map<String, List<StringTargetFactory>> results = ugh.results();
        assertEquals(2, results.size());
        
        final List<StringTargetFactory> result1 = results.get("baseDir1");
        assertEquals(2, result1.size());
        assertSame(sf1, result1.get(0));
        assertSame(sf2, result1.get(1));
        
        final List<StringTargetFactory> result2 = results.get("baseDir2");
        assertEquals(1, result2.size());
        assertSame(sf3, result2.get(0));
    }
}
