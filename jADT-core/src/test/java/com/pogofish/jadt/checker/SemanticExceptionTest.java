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
package com.pogofish.jadt.checker;

import static org.junit.Assert.*;

import org.junit.Test;

import com.pogofish.jadt.checker.ConstructorDataTypeConflictException;
import com.pogofish.jadt.checker.DuplicateConstructorException;
import com.pogofish.jadt.checker.DuplicateDataTypeException;



/**
 * For code coverage make sure the SemanticExceptions behave properly with respect to equals and hashCode
 *
 * @author jiry
 */
public class SemanticExceptionTest {

    /**
     * Test ConstructorDataTypeConflictException
     */
    @Test
    public void testConstructorDataTypeConflictException() {        
        final ConstructorDataTypeConflictException ex1 = new ConstructorDataTypeConflictException("Foo");
        final ConstructorDataTypeConflictException ex2 = new ConstructorDataTypeConflictException("Foo");
        final ConstructorDataTypeConflictException ex3 = new ConstructorDataTypeConflictException("Bar");
        checkEquals(ex1, ex1);
        checkEquals(ex1, ex2);
        checkNotEquals(ex1, ex3);
        checkNotEquals(ex1, null);
        checkNotEquals(ex1, "hello");
    }
    
    /**
     * Test DuplicateConstructorException
     */
    @Test 
    public void testDuplicateConstructorException() {        
        final DuplicateConstructorException ex1 = new DuplicateConstructorException("Foo", "Bar");
        final DuplicateConstructorException ex2 = new DuplicateConstructorException("Foo", "Bar");
        final DuplicateConstructorException ex3 = new DuplicateConstructorException("Baz", "Bar");
        final DuplicateConstructorException ex4 = new DuplicateConstructorException("Foo", "Baz");
        checkEquals(ex1, ex1);
        checkEquals(ex1, ex2);
        checkNotEquals(ex1, ex3);
        checkNotEquals(ex1, ex4);
        checkNotEquals(ex1, null);
        checkNotEquals(ex1, "hello");
    }
    
    /**
     * Test DuplicateDataTypeException
     */
    @Test 
    public void testDuplicateDataTypeException() {        
        final DuplicateDataTypeException ex1 = new DuplicateDataTypeException("Foo");
        final DuplicateDataTypeException ex2 = new DuplicateDataTypeException("Foo");
        final DuplicateDataTypeException ex3 = new DuplicateDataTypeException("Baz");
        checkEquals(ex1, ex1);
        checkEquals(ex1, ex2);
        checkNotEquals(ex1, ex3);
        checkNotEquals(ex1, null);
        checkNotEquals(ex1, "hello");
    }
    
    /**
     * Test DuplicateArgNameException
     */
    @Test
    public void testDuplicateArgNameException() {
        final DuplicateArgNameException ex1 = new DuplicateArgNameException("Foo", "Bar", "Baz");
        final DuplicateArgNameException ex2 = new DuplicateArgNameException("Foo", "Bar", "Baz");
        final DuplicateArgNameException ex3 = new DuplicateArgNameException("Foo2", "Bar", "Baz");
        final DuplicateArgNameException ex4 = new DuplicateArgNameException("Foo", "Bar2", "Baz");
        final DuplicateArgNameException ex5 = new DuplicateArgNameException("Foo", "Bar", "Baz3");        
        checkEquals(ex1, ex1);
        checkEquals(ex1, ex2);
        checkNotEquals(ex1, ex3);
        checkNotEquals(ex1, ex4);
        checkNotEquals(ex1, ex5);
        checkNotEquals(ex1, null);
        checkNotEquals(ex1, "hello");
    }
    
    /**
     * Test DuplicateModifierException
     */
    @Test
    public void testDuplicateModiferException() {
        final DuplicateModifierException ex1 = new DuplicateModifierException("Foo", "Bar", "Baz", "Quux");
        final DuplicateModifierException ex2 = new DuplicateModifierException("Foo", "Bar", "Baz", "Quux");
        final DuplicateModifierException ex3 = new DuplicateModifierException("Foo2", "Bar", "Baz", "Quux");
        final DuplicateModifierException ex4 = new DuplicateModifierException("Foo", "Bar2", "Baz", "Quux");
        final DuplicateModifierException ex5 = new DuplicateModifierException("Foo", "Bar", "Baz2", "Quux");
        final DuplicateModifierException ex6 = new DuplicateModifierException("Foo", "Bar", "Baz", "Quux2");
        checkEquals(ex1, ex1);
        checkEquals(ex1, ex2);
        checkNotEquals(ex1, ex3);
        checkNotEquals(ex1, ex4);
        checkNotEquals(ex1, ex5);
        checkNotEquals(ex1, ex6);
        checkNotEquals(ex1, null);
        checkNotEquals(ex1, "hello");
    }
    
    /**
     * Ensure the two objects are equals and have the same hashCode
     */
    private void checkEquals(Object obj1, Object obj2) {
        assertEquals(obj1, obj2);
        assertTrue(obj1 + " did not have the same hashCode as " + obj2, obj1.hashCode() == obj2.hashCode());
    }

    /**
     * Ensure the two objects are not equal and have different hashCodes
     */
    private void checkNotEquals(Object obj1, Object obj2) {
        assertFalse(obj1 + " did not have the same hashCode as " + obj2, obj1.equals(obj2));
        if (obj1 != null && obj2 != null) {
            assertFalse(obj1 + " had the same hashCode as " + obj2, obj1.hashCode() == obj2.hashCode());
        }
    }
}
