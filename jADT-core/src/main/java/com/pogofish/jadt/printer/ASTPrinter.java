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

import com.pogofish.jadt.ast.Annotation;
import com.pogofish.jadt.ast.AnnotationElement;
import com.pogofish.jadt.ast.AnnotationElement.ElementValue;
import com.pogofish.jadt.ast.AnnotationElement.ElementValuePairs;
import com.pogofish.jadt.ast.AnnotationKeyValue;
import com.pogofish.jadt.ast.AnnotationValue;
import com.pogofish.jadt.ast.AnnotationValue.AnnotationValueAnnotation;
import com.pogofish.jadt.ast.AnnotationValue.AnnotationValueArray;
import com.pogofish.jadt.ast.AnnotationValue.AnnotationValueExpression;
import com.pogofish.jadt.ast.Arg;
import com.pogofish.jadt.ast.ArgModifier;
import com.pogofish.jadt.ast.ArgModifier.Final;
import com.pogofish.jadt.ast.ArgModifier.Transient;
import com.pogofish.jadt.ast.ArgModifier.Volatile;
import com.pogofish.jadt.ast.BinaryOperator;
import com.pogofish.jadt.ast.BinaryOperator.Add;
import com.pogofish.jadt.ast.BinaryOperator.BitwiseAnd;
import com.pogofish.jadt.ast.BinaryOperator.BitwiseOr;
import com.pogofish.jadt.ast.BinaryOperator.BitwiseXor;
import com.pogofish.jadt.ast.BinaryOperator.Divide;
import com.pogofish.jadt.ast.BinaryOperator.DoubleEqual;
import com.pogofish.jadt.ast.BinaryOperator.GreaterThan;
import com.pogofish.jadt.ast.BinaryOperator.GreaterThanEqual;
import com.pogofish.jadt.ast.BinaryOperator.LeftShift;
import com.pogofish.jadt.ast.BinaryOperator.LessThan;
import com.pogofish.jadt.ast.BinaryOperator.LessThanEqual;
import com.pogofish.jadt.ast.BinaryOperator.LogicalAnd;
import com.pogofish.jadt.ast.BinaryOperator.LogicalOr;
import com.pogofish.jadt.ast.BinaryOperator.Mod;
import com.pogofish.jadt.ast.BinaryOperator.Multiply;
import com.pogofish.jadt.ast.BinaryOperator.NotEqual;
import com.pogofish.jadt.ast.BinaryOperator.RightShift;
import com.pogofish.jadt.ast.BinaryOperator.Subtract;
import com.pogofish.jadt.ast.BinaryOperator.ZeroExtendedRightShift;
import com.pogofish.jadt.ast.BlockToken;
import com.pogofish.jadt.ast.BlockToken.BlockEOL;
import com.pogofish.jadt.ast.BlockToken.BlockWhiteSpace;
import com.pogofish.jadt.ast.BlockToken.BlockWord;
import com.pogofish.jadt.ast.Constructor;
import com.pogofish.jadt.ast.DataType;
import com.pogofish.jadt.ast.Doc;
import com.pogofish.jadt.ast.Expression;
import com.pogofish.jadt.ast.Expression.BinaryExpression;
import com.pogofish.jadt.ast.Expression.ClassReference;
import com.pogofish.jadt.ast.Expression.LiteralExpression;
import com.pogofish.jadt.ast.Expression.NestedExpression;
import com.pogofish.jadt.ast.Expression.TernaryExpression;
import com.pogofish.jadt.ast.Expression.VariableExpression;
import com.pogofish.jadt.ast.Imprt;
import com.pogofish.jadt.ast.JDTagSection;
import com.pogofish.jadt.ast.JDToken;
import com.pogofish.jadt.ast.Literal;
import com.pogofish.jadt.ast.JDToken.JDAsterisk;
import com.pogofish.jadt.ast.JDToken.JDEOL;
import com.pogofish.jadt.ast.JDToken.JDTag;
import com.pogofish.jadt.ast.JDToken.JDWhiteSpace;
import com.pogofish.jadt.ast.JDToken.JDWord;
import com.pogofish.jadt.ast.JavaComment;
import com.pogofish.jadt.ast.JavaComment.JavaBlockComment;
import com.pogofish.jadt.ast.JavaComment.JavaDocComment;
import com.pogofish.jadt.ast.JavaComment.JavaEOLComment;
import com.pogofish.jadt.ast.Literal.BooleanLiteral;
import com.pogofish.jadt.ast.Literal.CharLiteral;
import com.pogofish.jadt.ast.Literal.FloatingPointLiteral;
import com.pogofish.jadt.ast.Literal.IntegerLiteral;
import com.pogofish.jadt.ast.Literal.NullLiteral;
import com.pogofish.jadt.ast.Literal.StringLiteral;
import com.pogofish.jadt.ast.Optional;
import com.pogofish.jadt.ast.Optional.None;
import com.pogofish.jadt.ast.Optional.Some;
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
    public static String printComments(String indent, List<JavaComment> comments) {
        final StringBuilder builder = new StringBuilder();
        for (JavaComment comment : comments) {
            builder.append(print(indent, comment));
            builder.append("\n");
        }
        return builder.toString();
    }

    /**
     * Prints a single comment pretty much unmolested
     */
    public static String print(final String indent, JavaComment comment) {
        return comment.match(new JavaComment.MatchBlock<String>() {

            @Override
            public String _case(JavaDocComment x) {
                final StringBuilder builder = new StringBuilder(indent);
                builder.append(x.start);
                for (JDToken token : x.generalSection) {
                    builder.append(print(indent, token));
                }
                for (JDTagSection tagSection : x.tagSections) {
                    for (JDToken token : tagSection.tokens) {
                        builder.append(print(indent, token));
                    }
                }
                
                builder.append(x.end);
                return builder.toString();
            }

            @Override
            public String _case(JavaBlockComment comment) {
                final StringBuilder builder = new StringBuilder(indent);
                for (List<BlockToken> line : comment.lines) {
                    for (BlockToken token : line) {
                        builder.append(token.match(new BlockToken.MatchBlock<String>() {

                            @Override
                            public String _case(BlockWord x) {
                                return x.word;
                            }

                            @Override
                            public String _case(BlockWhiteSpace x) {
                                return x.ws;
                            }

                            @Override
                            public String _case(BlockEOL x) {
                                return x.content + indent;
                            }
                        }));
                    }
                }
                return builder.toString();
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
        final StringBuilder builder = new StringBuilder();
        for(Annotation annotation : dataType.annotations) {
            builder.append(print(annotation));
            builder.append("\n");
        }
        builder.append(dataType.name);
        dataType.extendedType._switch(new Optional.SwitchBlock<RefType>() {
            @Override
            public void _case(Some<RefType> x) {
                builder.append(" extends ");
                builder.append(print(x.value));
            }

            @Override
            public void _case(None<RefType> x) {
            }
        });
        if (!dataType.implementedTypes.isEmpty()) {
            builder.append(" implements ");
            boolean first = true;
            for (RefType type : dataType.implementedTypes) {
                if (first) {
                    first = false;
                } else {
                    builder.append(", ");
                }
                builder.append(print(type));
            }
        }
        builder.append(" =\n    ");
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
        if (modifiers.contains(ArgModifier._Transient())) {
            builder.append(print(ArgModifier._Transient()));
            builder.append(" ");
        }
        if (modifiers.contains(ArgModifier._Volatile())) {
            builder.append(print(ArgModifier._Volatile()));
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
        return modifier.match(new ArgModifier.MatchBlock<String>() {

            @Override
            public String _case(Final x) {
                return "final";
            }

            @Override
            public String _case(Volatile x) {
                return "volatile";
            }

            @Override
            public String _case(Transient x) {
                return "transient";
            }
        });
    }

    /**
     * Print a single JavaDoc token
     */
    private static String print(final String indent, JDToken token) {
        return token.match(new JDToken.MatchBlock<String>() {

            @Override
            public String _case(JDAsterisk x) {
                return "*";
            }

            @Override
            public String _case(JDEOL x) {
                return x.content + indent;
            }

            @Override
            public String _case(JDTag x) {
                return x.name;
            }

            @Override
            public String _case(JDWord x) {
                return x.word;
            }

            @Override
            public String _case(JDWhiteSpace x) {
                return x.ws;
            }
        });
    }
    
    /**
     * Print a single literal
     */
    public static String print(final Literal literal) {
        return literal.match(new Literal.MatchBlock<String>() {
            @Override
            public String _case(StringLiteral x) {
                return x.content;
            }

            @Override
            public String _case(FloatingPointLiteral x) {
                return x.content;
            }

            @Override
            public String _case(IntegerLiteral x) {
                return x.content;
            }

            @Override
            public String _case(CharLiteral x) {
                return x.content;
            }

            @Override
            public String _case(BooleanLiteral x) {
                return x.content;
            }

            @Override
            public String _case(NullLiteral x) {
                return "null";
            }
        });
    }

    /**
     * Print an expression
     */
    public static String print(Expression expression) {
        return expression.match(new Expression.MatchBlock<String>() {

            @Override
            public String _case(LiteralExpression x) {
                return print(x.literal);
            }

            @Override
            public String _case(VariableExpression x) {
                return x.selector.match(new Optional.MatchBlock<Expression, String>() {
                    @Override
                    public String _case(Some<Expression> x) {
                        return print(x.value) + ".";
                    }

                    @Override
                    public String _case(None<Expression> x) {
                        return "";
                    }
                }) + x.identifier;
            }

            @Override
            public String _case(NestedExpression x) {
                return "( " + print(x.expression) + " )";
            }

            @Override
            public String _case(ClassReference x) {
                return print(x.type) + ".class";
            }

            @Override
            public String _case(TernaryExpression x) {
                return print(x.cond) + " ? " + print(x.trueExpression) + " : " + print (x.falseExpression);
            }

            @Override
            public String _case(BinaryExpression x) {
                return print(x.left) + " " + print(x.op) + " " +  print(x.right);
            }
        });
    }
    
    public static String print(Annotation annotation) {
        return "@" + annotation.name + annotation.element.match(new Optional.MatchBlock<AnnotationElement, String>() {

            @Override
            public String _case(Some<AnnotationElement> x) {
                return "( " + print(x.value) + " )";
            }

            @Override
            public String _case(None<AnnotationElement> x) {
                return "";
            }
        });
    }
    
    public static String print(AnnotationElement value) {
        return value.match(new AnnotationElement.MatchBlock<String>() {
            @Override
            public String _case(ElementValue x) {
                return print(x.value);
            }

            @Override
            public String _case(ElementValuePairs x) {
                final StringBuilder builder = new StringBuilder();
                boolean first = true;
                for (AnnotationKeyValue kv : x.keyValues) {
                    if (first) {
                        first = false;
                    } else {
                        builder.append(", ");
                    }
                    builder.append(kv.key);
                    builder.append(" = ");
                    builder.append(print(kv.value));
                }
                return builder.toString();
            }
        });
    }

    public static String print(AnnotationValue value) {
        return value.match(new AnnotationValue.MatchBlock<String>() {

            @Override
            public String _case(AnnotationValueAnnotation x) {
                return print(x.annotation);
            }

            @Override
            public String _case(AnnotationValueExpression x) {
                return print(x.expression);
            }

            @Override
            public String _case(AnnotationValueArray x) {
                final StringBuilder builder = new StringBuilder("{ ");
                boolean first = true;
                for (AnnotationValue value : x.values) {
                    if (first) {
                        first = false;
                    } else {
                        builder.append(", ");
                    }
                    builder.append(print(value));
                }
                return builder.toString();
            }
        });
    }
    
    public static String print(BinaryOperator op) {
        return op.match(new BinaryOperator.MatchBlock<String>() {

            @Override
            public String _case(LogicalOr x) {
                return "||";
            }

            @Override
            public String _case(LogicalAnd x) {
                return "&&";
            }

            @Override
            public String _case(BitwiseOr x) {
                return "|";
            }

            @Override
            public String _case(BitwiseAnd x) {
                return "&";
            }

            @Override
            public String _case(BitwiseXor x) {
                return "^";
            }

            @Override
            public String _case(Multiply x) {
                return "*";
            }

            @Override
            public String _case(Divide x) {
                return "/";
            }

            @Override
            public String _case(Add x) {
                return "+";
            }

            @Override
            public String _case(Subtract x) {
                return "-";
            }

            @Override
            public String _case(Mod x) {
                return "%";
            }

            @Override
            public String _case(DoubleEqual x) {
                return "==";
            }

            @Override
            public String _case(NotEqual x) {
                return "!=";
            }

            @Override
            public String _case(LessThan x) {
                return "<";
            }

            @Override
            public String _case(GreaterThan x) {
                return ">";
            }

            @Override
            public String _case(LessThanEqual x) {
                return "<=";
            }

            @Override
            public String _case(GreaterThanEqual x) {
                return ">=";
            }

            @Override
            public String _case(RightShift x) {
                return ">>";
            }

            @Override
            public String _case(LeftShift x) {
                return "<<";
            }

            @Override
            public String _case(ZeroExtendedRightShift x) {
                return ">>>";
            }
        });
    }
}
