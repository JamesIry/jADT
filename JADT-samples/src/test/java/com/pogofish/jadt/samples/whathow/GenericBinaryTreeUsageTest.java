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
package com.pogofish.jadt.samples.whathow;

import org.junit.Test;

import com.pogofish.jadt.samples.whathow.GenericBinaryTreeUsage;
import com.pogofish.jadt.samples.whathow.data.BinaryTree;


import static com.pogofish.jadt.samples.whathow.data.BinaryTree.*;
import static org.junit.Assert.*;

/**
 * Test to make sure the example generic binary tree usage does what it says it does
 * 
 * @author jiry
 */
public class GenericBinaryTreeUsageTest {
    @Test
    public void testMax() {
        GenericBinaryTreeUsage usage = new GenericBinaryTreeUsage();
        BinaryTree<String> empty = _EmptyTree();
        
        assertEquals(null, usage.max(empty));
        
        assertEquals("whatever", usage.max(_Node("hello", _Node("goodbye", empty, empty), _Node("whatever", empty, _Node("foo", empty, empty)))));
    }
    
    @Test
    public void testExampleCration() {
        GenericBinaryTreeUsage usage = new GenericBinaryTreeUsage();
        BinaryTree<String> empty = _EmptyTree();

        assertEquals(_Node("hello", _Node("goodbye", empty, empty), _Node("whatever", empty, _Node("foo", empty, empty))), usage.createExample());
    }
}
