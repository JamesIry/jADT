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
package pogofish.jadt.samples.whathow;

import static pogofish.jadt.samples.whathow.data.IntBinaryTree._EmptyTree;
import static pogofish.jadt.samples.whathow.data.IntBinaryTree._Node;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * Test the int binary tree usage examplex
 * 
 * @author jiry
 */
public class IntBinaryTreeUsageTest {
    @Test
    public void testExampleCreate() {
        final IntBinaryTreeUsage usage = new IntBinaryTreeUsage();
        assertEquals(_Node(42, _Node(12, _EmptyTree(), _EmptyTree()), _Node(103, _EmptyTree(), _Node(110, _EmptyTree(), _EmptyTree()))), usage.createExample());
    }
    
    @Test
    public void testMax() {
        final IntBinaryTreeUsage usage = new IntBinaryTreeUsage();
        assertEquals(null, usage.max(_EmptyTree()));
        assertEquals(Integer.valueOf(110), usage.max(_Node(42, _Node(12, _EmptyTree(), _EmptyTree()), _Node(103, _EmptyTree(), _Node(110, _EmptyTree(), _EmptyTree())))));
    }

}
