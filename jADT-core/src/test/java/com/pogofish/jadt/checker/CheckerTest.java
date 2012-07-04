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

import static com.pogofish.jadt.ast.ASTConstants.NO_COMMENTS;
import static com.pogofish.jadt.ast.ASTConstants.NO_IMPORTS;
import static com.pogofish.jadt.ast.JavaComment._JavaDocComment;
import static com.pogofish.jadt.ast.SemanticError._ConstructorDataTypeConflict;
import static com.pogofish.jadt.ast.SemanticError._DuplicateArgName;
import static com.pogofish.jadt.ast.SemanticError._DuplicateConstructor;
import static com.pogofish.jadt.ast.SemanticError._DuplicateDataType;
import static com.pogofish.jadt.ast.SemanticError._DuplicateModifier;
import static com.pogofish.jadt.ast.SemanticError._TooManyConstructorJavaDocComments;
import static com.pogofish.jadt.ast.SemanticError._TooManyDataTypeJavaDocComments;
import static com.pogofish.jadt.util.Util.list;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;

import com.pogofish.jadt.ast.Arg;
import com.pogofish.jadt.ast.ArgModifier;
import com.pogofish.jadt.ast.Constructor;
import com.pogofish.jadt.ast.DataType;
import com.pogofish.jadt.ast.Doc;
import com.pogofish.jadt.ast.Pkg;
import com.pogofish.jadt.ast.PrimitiveType;
import com.pogofish.jadt.ast.SemanticError;
import com.pogofish.jadt.ast.Type;
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
        final DataType dataType = new DataType(NO_COMMENTS, "Foo", Util.<String>list(), list(new Constructor(NO_COMMENTS, "Foo", Util.<Arg>list())));
        final Doc doc = new Doc("CheckerTest", Pkg._Pkg(NO_COMMENTS, ""), NO_IMPORTS, list(dataType, dataType));
        final List<SemanticError> errors = checker.check(doc);
        assertEquals(1, errors.size());
        assertTrue(errors.contains(_DuplicateDataType(dataType.name)));
    }
    
    /**
     * Check with duplicate constructors for one data type
     */
    @Test
    public void testDuplicateConstructor() {
        final Checker checker = new StandardChecker();
        final Constructor constructor = new Constructor(NO_COMMENTS, "Bar", Util.<Arg>list());
        final DataType dataType = new DataType(NO_COMMENTS, "Foo", Util.<String>list(), list(constructor, constructor));
        final Doc doc = new Doc("CheckerTest", Pkg._Pkg(NO_COMMENTS, ""), NO_IMPORTS, list(dataType));
        final List<SemanticError> errors = checker.check(doc);
        assertEquals(1, errors.size());
        assertTrue(errors.contains(_DuplicateConstructor(dataType.name, constructor.name)));
    }
    
    /**
     * Check when there's a conflict in the name of a data type and one of its constructors
     */
    @Test
    public void testConstructorDataTypeConflict() {
        final Checker checker = new StandardChecker();
        final Constructor constructor1 = new Constructor(NO_COMMENTS, "Bar", Util.<Arg>list());
        final Constructor constructor2 = new Constructor(NO_COMMENTS, "Foo", Util.<Arg>list());
        final DataType dataType = new DataType(NO_COMMENTS, "Foo", Util.<String>list(), list(constructor1, constructor2));
        final Doc doc = new Doc("CheckerTest", Pkg._Pkg(NO_COMMENTS, ""), NO_IMPORTS, list(dataType));
        final List<SemanticError> errors = checker.check(doc);
        assertEquals(1, errors.size());
        assertTrue(errors.contains(_ConstructorDataTypeConflict(dataType.name)));
    }
    
    @Test
    public void testDuplicateArgName() {
        final Checker checker = new StandardChecker();
        final Constructor constructor = new Constructor(NO_COMMENTS, "Bar", list(Arg._Arg(Util.<ArgModifier>list(), Type._Primitive(PrimitiveType._IntType()), "foo"), Arg._Arg(Util.<ArgModifier>list(), Type._Primitive(PrimitiveType._BooleanType()), "foo")));
        final DataType dataType = new DataType(NO_COMMENTS, "Foo", Util.<String>list(), list(constructor));
        final Doc doc = new Doc("CheckerTest", Pkg._Pkg(NO_COMMENTS, ""), NO_IMPORTS, list(dataType));
        final List<SemanticError> errors = checker.check(doc);
        assertEquals(1, errors.size());
        assertTrue(errors.contains(_DuplicateArgName(dataType.name, constructor.name, "foo")));       
    }
    
    @Test
    public void testDuplicateArgModifier() {
        final Checker checker = new StandardChecker();
        final Constructor constructor = new Constructor(NO_COMMENTS, "Bar", list(Arg._Arg(list(ArgModifier._Final(), ArgModifier._Final()), Type._Primitive(PrimitiveType._IntType()), "foo")));
        final DataType dataType = new DataType(NO_COMMENTS, "Foo", Util.<String>list(), list(constructor));
        final Doc doc = new Doc("CheckerTest", Pkg._Pkg(NO_COMMENTS, ""), NO_IMPORTS, list(dataType));
        final List<SemanticError> errors = checker.check(doc);
        assertEquals(1, errors.size());
        assertTrue(errors.contains(_DuplicateModifier(dataType.name, constructor.name, "foo", "final")));       
    }    
    
    @Test
    public void testTooManyJavaDocCommentsDataType() {
        final Checker checker = new StandardChecker();
        final Constructor constructor = new Constructor(NO_COMMENTS, "Bar", list(Arg._Arg(list(ArgModifier._Final()), Type._Primitive(PrimitiveType._IntType()), "foo")));
        final DataType dataType = new DataType(list(_JavaDocComment("/** hello */"), _JavaDocComment("/** world */")), "Foo", Util.<String>list(), list(constructor));
        final Doc doc = new Doc("CheckerTest", Pkg._Pkg(NO_COMMENTS, ""), NO_IMPORTS, list(dataType));
        final List<SemanticError> errors = checker.check(doc);
        assertEquals(1, errors.size());
        assertTrue(errors.contains(_TooManyDataTypeJavaDocComments("Foo")));           
    }
    
    @Test
    public void testTooManyJavaDocCommentsConstructor() {
        final Checker checker = new StandardChecker();
        final Constructor constructor = new Constructor(list(_JavaDocComment("/** hello */"), _JavaDocComment("/** world */")), "Bar", list(Arg._Arg(list(ArgModifier._Final()), Type._Primitive(PrimitiveType._IntType()), "foo")));
        final DataType dataType = new DataType(NO_COMMENTS, "Foo", Util.<String>list(), list(constructor));
        final Doc doc = new Doc("CheckerTest", Pkg._Pkg(NO_COMMENTS, ""), NO_IMPORTS, list(dataType));
        final List<SemanticError> errors = checker.check(doc);
        assertEquals(1, errors.size());
        assertTrue(errors.contains(_TooManyConstructorJavaDocComments("Foo", "Bar")));           
    }
       
}
