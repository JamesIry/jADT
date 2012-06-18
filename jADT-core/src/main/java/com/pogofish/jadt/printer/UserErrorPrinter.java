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
package com.pogofish.jadt.printer;

import com.pogofish.jadt.ast.SemanticError;
import com.pogofish.jadt.ast.SemanticError.ConstructorDataTypeConflict;
import com.pogofish.jadt.ast.SemanticError.DuplicateArgName;
import com.pogofish.jadt.ast.SemanticError.DuplicateConstructor;
import com.pogofish.jadt.ast.SemanticError.DuplicateDataType;
import com.pogofish.jadt.ast.SemanticError.DuplicateModifier;
import com.pogofish.jadt.ast.SyntaxError;
import com.pogofish.jadt.ast.UserError;
import com.pogofish.jadt.ast.UserError.Semantic;
import com.pogofish.jadt.ast.UserError.Syntactic;

/**
 * Pretty printer for the jADT user errors.  Note that in the context of pretty printers "print" just means "make a nice looking string"
 *
 * @author jiry
 */
public class UserErrorPrinter {
    public static String print(UserError error) {
        return error.match(new UserError.MatchBlock<String>() {

            @Override
            public String _case(Semantic x) {
                return print(x.error);
            }

            @Override
            public String _case(Syntactic x) {
                return print(x.error);
            }
        });
    }
    
    public static String print(SyntaxError error) {
        return  "Found token '" + error.found + "' but expected '" + error.expected + "' at line " + error.line + ".";
    }
    
    public static String print(SemanticError error) {
        return error.match(new SemanticError.MatchBlock<String>() {
            @Override
            public String _case(DuplicateDataType x) {
                return "Cannot have two datatypes named " + x.dataTypeName + " in one jADT document.";
            }

            @Override
            public String _case(ConstructorDataTypeConflict x) {
                return "Data type " + x.dataTypeName + " cannot have a constructor with the name " + x.dataTypeName +".  Only single constructor data types may have constructors with the same name.";
           }

            @Override
            public String _case(DuplicateConstructor x) {
                return "Data type " + x.dataTypeName + " cannot have multiple constructors named " + x.constructorName + ".";
           }

            @Override
            public String _case(DuplicateArgName x) {
                return "Duplicated arg name " + x.argName + " in constructor " + x.constructorName + " of data type " + x.dataTypeName + ".";
            }

            @Override
            public String _case(DuplicateModifier x) {
                return "Duplicated modifier " + x.modifier +" on arg name " + x.argName + " in constructor " + x.constructorName + " of data type " + x.dataTypeName + ".";
            }
        });
    }
}
