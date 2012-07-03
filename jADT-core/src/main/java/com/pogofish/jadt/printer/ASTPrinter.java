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

import java.util.List;

import com.pogofish.jadt.ast.Arg;
import com.pogofish.jadt.ast.ArgModifier;
import com.pogofish.jadt.ast.Constructor;
import com.pogofish.jadt.ast.DataType;
import com.pogofish.jadt.ast.Doc;
import com.pogofish.jadt.ast.Imprt;
import com.pogofish.jadt.ast.JavaComment;
import com.pogofish.jadt.ast.JavaComment.JavaDocComment;
import com.pogofish.jadt.ast.JavaComment.JavaEOLComment;
import com.pogofish.jadt.ast.JavaComment.JavaMultiLineComment;
import com.pogofish.jadt.ast.PrimitiveType;
import com.pogofish.jadt.ast.PrimitiveType.BooleanType;
import com.pogofish.jadt.ast.PrimitiveType.ByteType;
import com.pogofish.jadt.ast.PrimitiveType.CharType;
import com.pogofish.jadt.ast.PrimitiveType.DoubleType;
import com.pogofish.jadt.ast.PrimitiveType.FloatType;
import com.pogofish.jadt.ast.PrimitiveType.IntType;
import com.pogofish.jadt.ast.PrimitiveType.LongType;
import com.pogofish.jadt.ast.PrimitiveType.ShortType;
import com.pogofish.jadt.ast.RefType;
import com.pogofish.jadt.ast.RefType.ArrayType;
import com.pogofish.jadt.ast.RefType.ClassType;
import com.pogofish.jadt.ast.Type;
import com.pogofish.jadt.ast.Type.Primitive;
import com.pogofish.jadt.ast.Type.Ref;

/**
 * Pretty printer for the jADT AST.  Note that in the context of pretty printers "print" just means "make a nice looking string"
 *
 * @author jiry
 */
public class ASTPrinter  {
    /**
     * ( "package" packageName )?
     * (
     * ( "import" import \n)*
     * )?
     * dataType
     * (dataType\n)*
     * 
     * @param doc Document to be printed
     * @return pretty string
     */
    public static String print(Doc doc) {
        final StringBuilder builder = new StringBuilder(doc.pkg.name.isEmpty() ? "" : ("package " + doc.pkg.name + "\n\n"));
        if (!doc.imports.isEmpty()) {
            for (Imprt imp : doc.imports) {
                builder.append("import " + imp.name + "\n");
            }
            builder.append("\n");
        }
        for (DataType dataType : doc.dataTypes) {
            builder.append(print(dataType));
            builder.append("\n");
        }
        return builder.toString();  
        
    }
    
    /**
     * Prints a list of comments pretty much unmolested except to add \n s
     */
    public static String printComments(List<JavaComment> comments) {
        final StringBuilder builder = new StringBuilder();
        for (JavaComment comment : comments) {
            builder.append(print(comment));
            builder.append("\n");
        }
        return builder.toString();
    }

    /**
     * Prints a single comment pretty much unmolested
     */
    public static String print(JavaComment comment) {
        return comment.match(new JavaComment.MatchBlock<String>() {

            @Override
            public String _case(JavaDocComment x) {
                return x.comment;
            }

            @Override
            public String _case(JavaMultiLineComment x) {
                return x.comment;
            }

            @Override
            public String _case(JavaEOLComment x) {
                return x.comment;
            }
        });
    }

    /**
     * Prints a dataType as
     * dataType = constructor
     * (\n  | constructor)*
     * 
     * @param dataType DataType to be printed
     * @return pretty string
     */
    public static String print(DataType dataType) {
        final StringBuilder builder = new StringBuilder(dataType.name + " =\n    ");
        boolean first = true;
        for (Constructor constructor : dataType.constructors) {
            if (first) {
                first = false;
            } else {
                builder.append("\n  | ");
            }
            builder.append(print(constructor));
        }
        return builder.toString();
    }

    /**
     * Prints a constructor as
     * name("("arg(", "arg)*")")?
     * 
     * @param constructor
     * @return pretty string
     */
    public static String print(Constructor constructor) {
        final StringBuilder builder = new StringBuilder(constructor.name);
        if (! constructor.args.isEmpty()) {
            builder.append("(");
            boolean first = true;
            for (Arg arg : constructor.args) {
                if (first) {
                    first = false;
                } else {
                    builder.append(", ");
                }
                builder.append(print(arg));
            }
            builder.append(")");
        }
        return builder.toString();
    }

    /**
     * prints an arg as
     * type name
     * 
     * @param arg Arg to be printed
     * @return pretty string
     */
    public static String print(Arg arg) {
        return printArgModifiers(arg.modifiers) + print(arg.type) + " " + arg.name;
    }
    
    /**
     * Prints a list of arg modifiers
     * @param modifiers list of arg modifiers
     * @return string consisting of those modifiers, with a trailing space
     */
    public static String printArgModifiers(List<ArgModifier> modifiers) {
        final StringBuilder builder = new StringBuilder();
        if (modifiers.contains(ArgModifier._Final())) {
            builder.append(print(ArgModifier._Final()));
            builder.append(" ");
        }
        return builder.toString();
    }
    
    /**
     * Prints a type as either a ref type or a primitive
     * 
     * @param type Type to be printed
     * @return pretty string
     */
    public static String print(Type type) {
        return type.match(new Type.MatchBlock<String>(){
            @Override
            public String _case(Ref x) {
                return print(x.type);
            }

            @Override
            public String _case(Primitive x) {
                return print(x.type);
            }
            
        });
    }
    
    /**
     * Prints a type as either an array or class type. 
     * 
     * For a an array, it prints as
     * type[]
     * 
     * For a class it prints as
     * className("<" refType(", " refType)*">")?
     * 
     * @param type RefType to be printed
     * @return pretty string
     */
    public static String print(RefType type) {
        return type.match(new RefType.MatchBlock<String>() {

            @Override
            public String _case(ClassType x) {
                final StringBuilder builder = new StringBuilder(x.baseName);
                if (!x.typeArguments.isEmpty()) {
                    builder.append("<");
                    boolean first = true;
                    for (RefType typeArgument : x.typeArguments) {
                        if (first) {
                            first = false;
                        } else {
                            builder.append(", ");
                        }
                        builder.append(print(typeArgument));
                    }
                    builder.append(">");
                }
                return builder.toString();
            }

            @Override
            public String _case(ArrayType x) {
                return print(x.heldType) + "[]";
            }});
    }
    
    /**
     * Prints a primitive type as "boolean" | "char" | "short" | "int" | "long" | "float" | "double"
     * @param type PrimitiveType to be printed
     * @return pretty string
     */
    public static String print(PrimitiveType type) {
        return type.match(new PrimitiveType.MatchBlock<String>() {

            @Override
            public String _case(BooleanType x) {
                return "boolean";
            }

            @Override
            public String _case(ByteType x) {
                return "byte";
            }

            @Override
            public String _case(CharType x) {
                return "char";
            }

            @Override
            public String _case(DoubleType x) {
                return "double";
            }

            @Override
            public String _case(FloatType x) {
                return "float";
            }

            @Override
            public String _case(IntType x) {
                return "int";
            }

            @Override
            public String _case(LongType x) {
                return "long";
            }

            @Override
            public String _case(ShortType x) {
                return "short";
            }});
    }

    /**
     * Print arg modifier
     */
    public static String print(ArgModifier modifier) {
        // so far there's only one modifer, but that will change;
        return "final";
    }

}
