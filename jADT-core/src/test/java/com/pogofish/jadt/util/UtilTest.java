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
import static org.junit.Assert.assertFalse;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Test;


/**
 * Test the Util utilities
 *
 * @author jiry
 */
public class UtilTest {
    /**
     * Cobertura isn't happy unless the (implicit) constructor is called so this
     * stupid little test does that
     */
    @Test
    public void constructorTest() {
        final Util util = new Util();
        assertFalse(util.toString().isEmpty());
    }

    /**
     * Does list produce a list?
     */
    @Test
    public void testList() {
        final List<String> list = new ArrayList<String>();
        list.add("hello");
        list.add("world");
        assertEquals(list, Util.list("hello", "world"));
    }
    
    /**
     * Does set produce a set?
     */
    @Test
    public void testSet() {
        final Set<String> set = new HashSet<String>();
        set.add("hello");
        set.add("world");
        assertEquals(set, Util.set("hello", "world"));
    }
}
