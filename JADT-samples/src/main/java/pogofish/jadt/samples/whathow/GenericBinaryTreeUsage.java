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

import pogofish.jadt.samples.whathow.BinaryTree.*;
import static pogofish.jadt.samples.whathow.BinaryTree.*;

/**
 * Example usage of an GenericBinaryTree used in the how page
 * 
 * @author jiry
 */
public class GenericBinaryTreeUsage {
    /**
     * Creates an example IntBinaryTree with some fixed values
     */
    public BinaryTree<String> createExample() {
        
        BinaryTree<String> empty = BinaryTree.<String>_EmptyTree();

        return _Node("hello", _Node("goodbye", empty, empty), _Node("whatever", empty, _Node("foo", empty, empty)));        
    } 
    
    /**
     * Find the string that sorts highest in a tree where null 
     * is considered smallest and otherwise strings are compared with compareTo
     */
    public String max(BinaryTree<String> tree)  {
        return tree.accept(new BinaryTree.Visitor<String, String>() {
           @Override
           public String visit(Node<String> x) {
               final String maxLeft = max(x.left);
               final String maxRight = max(x.right);

               return maxString(maxString(maxLeft, maxRight), x.value);
           }

           @Override
           public String visit(EmptyTree<String> x) {
              return null;
           }
        });
     }
    
    /**
     * Find the max of two Strings where null is smaller than all strings and otherwise strings are compared with compareTo
     */
    private String maxString(String l, String r) {
        return l == null ? r : (r == null ? l : (l.compareTo(r) >= 0 ? l : r));
    }
}
