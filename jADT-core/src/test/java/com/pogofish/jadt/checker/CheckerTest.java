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

import static com.pogofish.jadt.util.Util.list;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Set;

import org.junit.Test;

import com.pogofish.jadt.ast.*;
import com.pogofish.jadt.checker.Checker;
import com.pogofish.jadt.checker.ConstructorDataTypeConflictException;
import com.pogofish.jadt.checker.DuplicateConstructorException;
import com.pogofish.jadt.checker.DuplicateDataTypeException;
import com.pogofish.jadt.checker.SemanticException;
import com.pogofish.jadt.checker.StandardChecker;
import com.pogofish.jadt.util.Util;


/**
 * Test the StandardChecker
 *
 * @author jiry
 */
public class CheckerTest {
    /**
     * Check with duplicate data types
     */
    @Test
    public void testDuplicateDataType() {
        final Checker checker = new StandardChecker();
        final DataType dataType = new DataType("Foo", Util.<String>list(), list(new Constructor("Foo", Util.<Arg>list())));
        final Doc doc = new Doc("CheckerTest", "", Util.<String>list(), list(dataType, dataType));
        final Set<SemanticException> errors = checker.check(doc);
        assertEquals(1, errors.size());
        assertTrue(errors.contains(new DuplicateDataTypeException(dataType.name)));
    }
    
    /**
     * Check with duplicate constructors for one data type
     */
    @Test
    public void testDuplicateConstructor() {
        final Checker checker = new StandardChecker();
        final Constructor constructor = new Constructor("Bar", Util.<Arg>list());
        final DataType dataType = new DataType("Foo", Util.<String>list(), list(constructor, constructor));
        final Doc doc = new Doc("CheckerTest", "", Util.<String>list(), list(dataType));
        final Set<SemanticException> errors = checker.check(doc);
        assertEquals(1, errors.size());
        assertTrue(errors.contains(new DuplicateConstructorException(dataType.name, constructor.name)));
    }
    
    /**
     * Check when there's a conflict in the name of a data type and one of its constructors
     */
    @Test
    public void testConstructorDataTypeConflict() {
        final Checker checker = new StandardChecker();
        final Constructor constructor1 = new Constructor("Bar", Util.<Arg>list());
        final Constructor constructor2 = new Constructor("Foo", Util.<Arg>list());
        final DataType dataType = new DataType("Foo", Util.<String>list(), list(constructor1, constructor2));
        final Doc doc = new Doc("CheckerTest", "", Util.<String>list(), list(dataType));
        final Set<SemanticException> errors = checker.check(doc);
        assertEquals(1, errors.size());
        assertTrue(errors.contains(new ConstructorDataTypeConflictException(dataType.name)));
    }    
}
