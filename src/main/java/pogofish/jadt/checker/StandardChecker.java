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
package pogofish.jadt.checker;

import java.util.HashSet;
import java.util.Set;

import pogofish.jadt.ast.*;

/**
 * Standard checker that finds problems in Doc documents
 *
 * @author jiry
 */
public class StandardChecker implements Checker {
    
    /**
     * Checks a documents for duplicate dataType names and calls check(DataType) on each one
     * 
     * @param doc Doc to check
     * @return Set<SemanticException> with the problems or empty set if there are none
     */
    @Override 
    public Set<SemanticException> check(Doc doc) {
        final Set<SemanticException> errors = new HashSet<SemanticException>();
        final Set<String> dataTypeNames = new HashSet<String>();
        for (DataType dataType : doc.dataTypes) {
            errors.addAll(check(dataType));
            if (dataTypeNames.contains(dataType.name)) {
                errors.add(new DuplicateDataTypeException(dataType.name));
            } else {
                dataTypeNames.add(dataType.name);
            }
        }
        return errors;
    }

    /**
     * Checks a data type for duplicate constructor names or constructors having the same name
     * as the data type
     * 
     * @param dataType DataType to check
     * @return Set<SemanticException> with the problems or empty set if there are none
     */
    private Set<SemanticException> check(DataType dataType) {
        final Set<SemanticException> errors = new HashSet<SemanticException>();
        final Set<String> constructorNames = new HashSet<String>();
        if (dataType.constructors.size() > 1) {
            for(Constructor constructor : dataType.constructors) {
                if (dataType.name.equals(constructor.name)) {
                    errors.add(new ConstructorDataTypeConflictException(dataType.name, constructor.name));
                }
                if (constructorNames.contains(constructor.name)) {
                    errors.add(new DuplicateConstructorException(dataType.name, constructor.name));
                } else {
                    constructorNames.add(constructor.name);
                }
            }
        }
        return errors;
    }

}
