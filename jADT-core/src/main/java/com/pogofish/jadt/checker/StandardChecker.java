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

import static com.pogofish.jadt.ast.SemanticError.*;
import static com.pogofish.jadt.ast.SemanticError._DuplicateArgName;
import static com.pogofish.jadt.ast.SemanticError._DuplicateConstructor;
import static com.pogofish.jadt.ast.SemanticError._DuplicateDataType;
import static com.pogofish.jadt.ast.SemanticError._DuplicateModifier;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import com.pogofish.jadt.ast.Arg;
import com.pogofish.jadt.ast.ArgModifier;
import com.pogofish.jadt.ast.Constructor;
import com.pogofish.jadt.ast.DataType;
import com.pogofish.jadt.ast.Doc;
import com.pogofish.jadt.ast.JavaComment;
import com.pogofish.jadt.ast.JavaComment.JavaDocComment;
import com.pogofish.jadt.ast.SemanticError;
import com.pogofish.jadt.printer.ASTPrinter;


/**
 * Standard checker that finds problems in Doc documents
 *
 * @author jiry
 */
public class StandardChecker implements Checker {
	private static final Logger logger = Logger.getLogger(StandardChecker.class.toString());
    
    /**
     * Checks a documents for duplicate dataType names and calls check(DataType) on each one
     * 
     * @param doc Doc to check
     * @return Set<SemanticError> with the problems or empty set if there are none
     */
    @Override 
    public List<SemanticError> check(Doc doc) {
    	logger.fine("Checking semantic constraints in document from " + doc.srcInfo);
        final List<SemanticError> errors = new ArrayList<SemanticError>();
        final Set<String> dataTypeNames = new HashSet<String>();
        for (DataType dataType : doc.dataTypes) {
            if (dataTypeNames.contains(dataType.name)) {
            	logger.info("Duplicate data type name " + dataType.name + ".");
                errors.add(_DuplicateDataType(dataType.name));
            } else {
                dataTypeNames.add(dataType.name);
            }
            errors.addAll(check(dataType));
        }
        return errors;
    }

    /**
     * Checks a data type for duplicate constructor names or constructors having the same name
     * as the data type
     * 
     * @param dataType DataType to check
     * @return Set<SemanticError> with the problems or empty set if there are none
     */
    private List<SemanticError> check(DataType dataType) {
    	logger.finer("Checking semantic constraints on datatype " + dataType.name);
        final List<SemanticError> errors = new ArrayList<SemanticError>();
        
        final int javaDocComments = countJavaDocComments(dataType.comments);
        if (javaDocComments > 1) {
            errors.add(_TooManyDataTypeJavaDocComments(dataType.name));
        }
        
        final Set<String> constructorNames = new HashSet<String>();
        for(Constructor constructor : dataType.constructors) {
        	logger.finest("Checking semantic constraints on constructor " + constructor.name + " in datatype " + dataType.name);
            if (dataType.constructors.size() > 1 && dataType.name.equals(constructor.name)) {
            	logger.info("Constructor with same name as its data type " + dataType.name + ".");
                errors.add(_ConstructorDataTypeConflict(dataType.name));
            }
            if (constructorNames.contains(constructor.name)) {
            	logger.info("Two constructors with same name " + constructor.name + " in data type " + dataType.name + ".");
                errors.add(_DuplicateConstructor(dataType.name, constructor.name));
            } else {
                constructorNames.add(constructor.name);
            }
            errors.addAll(check(dataType, constructor));
        }
        return errors;
    }
    
    private int countJavaDocComments(List<JavaComment> comments) {
        int count = 0;
        for (JavaComment comment : comments) {
            count = count + comment.match(new JavaComment.MatchBlockWithDefault<Integer>() {

                @Override
                public Integer _case(JavaDocComment x) {
                    return 1;
                }

                @Override
                protected Integer _default(JavaComment x) {
                    return 0;
                }
            });
        }
        
        return count;
    }

    /**
     * Check a constructor to make sure it does not duplicate any args and that no arg duplicates any modifiers
     * 
     */
    private List<SemanticError> check(DataType dataType, Constructor constructor) {
        logger.finer("Checking semantic constraints on data type " + dataType.name + ", constructor " + constructor.name);
        final List<SemanticError> errors = new ArrayList<SemanticError>();
        
        final int javaDocComments = countJavaDocComments(constructor.comments);
        if (javaDocComments > 1) {
            errors.add(_TooManyConstructorJavaDocComments(dataType.name, constructor.name));
        }
        
        final Set<String> argNames = new HashSet<String>();
        for (Arg arg : constructor.args) {
            if (argNames.contains(arg.name)) {
                errors.add(_DuplicateArgName(dataType.name, constructor.name, arg.name));
            } else {
                argNames.add(arg.name);
            }
            errors.addAll(check(dataType, constructor, arg));
        }
        
        return errors;
    }

    /**
     * Checkt to make sure an arg doesn't have duplicate modifiers
     */
    private List<SemanticError> check(DataType dataType, Constructor constructor, Arg arg) {
        logger.finest("Checking semantic constraints on data type " + dataType.name + ", constructor " + constructor.name);
        final List<SemanticError> errors = new ArrayList<SemanticError>();
        final Set<ArgModifier> modifiers = new HashSet<ArgModifier>();
        for (ArgModifier modifier : arg.modifiers) {
            if (modifiers.contains(modifier)) {
                final String modName = ASTPrinter.print(modifier);
                errors.add(_DuplicateModifier(dataType.name, constructor.name, arg.name, modName));
            } else {
                modifiers.add(modifier);
            }
        }
        
        return errors;
    }

}
