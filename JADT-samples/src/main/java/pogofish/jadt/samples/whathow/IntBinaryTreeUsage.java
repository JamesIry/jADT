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

// START SNIPPET: imports
import pogofish.jadt.samples.whathow.IntBinaryTree.*;
import static pogofish.jadt.samples.whathow.IntBinaryTree.*;
// END SNIPPET: imports    

/**
 * Example usage of an IntBinaryTree used in the how page
 * It is marked up with START SNIPPET and END SNIPPET boundaries to support
 * /JADT/src/site/apt/*.apt
 * 
 * @author jiry
 */
public class IntBinaryTreeUsage {
    /**
     * Creates an example IntBinaryTree with some fixed values
     */
    public IntBinaryTree createExample() {
        // START SNIPPET: sampleTree    
        IntBinaryTree tree = _Node(42, _Node(12, _EmptyTree(), _EmptyTree()), _Node(103, _EmptyTree(), _Node(110, _EmptyTree(), _EmptyTree())));
        // END SNIPPET: sampleTree
        
        return tree;
    }
    
    /**
     * Find the max Integer of a tree where null means empty tree
     */
    // START SNIPPET: max  
    public Integer max(IntBinaryTree tree)  {
        return tree.accept(new IntBinaryTree.Visitor<Integer>() {
           @Override
           public Integer visit(Node x) {
              final Integer maxLeft = max(x.left);
              final int l = maxLeft == null ? Integer.MIN_VALUE : maxLeft;

              final Integer maxRight = max(x.right);
              final int r = maxRight == null ? Integer.MIN_VALUE : maxRight;

              return Math.max(Math.max(l, r), x.value);
           }

           @Override
           public Integer visit(EmptyTree x) {
              return null;
           }
        });
     }
    // END SNIPPET: max  
 }
