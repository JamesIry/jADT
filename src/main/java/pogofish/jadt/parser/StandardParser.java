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
package pogofish.jadt.parser;

import static pogofish.jadt.ast.PrimitiveType.*;
import static pogofish.jadt.ast.RefType._ArrayType;
import static pogofish.jadt.ast.RefType._ClassType;
import static pogofish.jadt.ast.Type._Primitive;
import static pogofish.jadt.ast.Type._Ref;

import java.util.*;

import pogofish.jadt.ast.*;
import pogofish.jadt.ast.Type.Primitive;
import pogofish.jadt.ast.Type.Ref;
import pogofish.jadt.source.Source;
import pogofish.jadt.util.Util;

/**
 * The standard parse for JADT description files
 *
 * @author jiry
 */
public class StandardParser implements Parser {

    
    /* (non-Javadoc)
     * @see sfdc.adt.IParser#parse(java.lang.String, java.io.Reader)
     */
    @Override
    public Doc parse(Source source)  {
        final Tokenizer tokenizer = new Tokenizer(source);
        final Impl impl = new Impl(tokenizer);
        return impl.doc();
    }
    
    /**
     * Internal implementation of the Parser that lets Parser be non-stateful
     *
     * @author jiry
     */
    static class Impl {
        /**
         * Tokenizer to be parsed
         */
        private final Tokenizer tokenizer;
        

        /**
         * Creates a Parser.Impl that will parse the given tokenizer using the srcInfo for error reporting
         * 
         * @param srcInfo String information about the source that is used when throwing a syntax exception
         * @param tokenizer Tokenizer to be parsed
         */
        public Impl(Tokenizer tokenizer) {
            this.tokenizer = tokenizer;
        }

        /**
         * Parses a complete document which is pkg imports dataTypes
         * 
         * @return Doc
         */
        public Doc doc() {
            return new Doc(tokenizer.srcInfo(), pkg(), imports(), dataTypes());
        }

        /** 
         * Gets an optional package declaration "pacakage" packageName
         * 
         * @return String the package name, or, because package is optional returns an empty string ""
         */
        public String pkg() {
            if (tokenizer.accept(TokenType.PACKAGE)) {
                return packageName();
            } else {
                return "";
            }
        }
        
        /**
         * Recognizes a required token that is a valid package name
         * 
         * @return String the package name
         */
        private String packageName() {
            if (tokenizer.accept(TokenType.IDENTIFIER)) {
                return tokenizer.lastSymbol();
            } else if (tokenizer.accept(TokenType.DOTTED_IDENTIFIER)) {
                return tokenizer.lastSymbol();
            } else {
                throw syntaxException("a package name");
            }
        }
        
        /**
         * Parses an optional list of imports which is ("import" packageName)*
         * 
         * @return List<String> possibly empty list of imports
         */
        public List<String> imports() {
            final List<String> imports = new ArrayList<String>();
            
            while (tokenizer.accept(TokenType.IMPORT)) {
                imports.add(packageName());
            }
            return imports;
            
        }

        /**
         * Parses a required list of datatypes which is dataType (dataType)*
         * 
         * @return List<DataType> non empty list of DataTypes
         */
        public List<DataType> dataTypes() {
            final List<DataType> dataTypes = new ArrayList<DataType>();

            dataTypes.add(dataType());
            
            while (!tokenizer.accept(TokenType.EOF)) {
                dataTypes.add(dataType());
            }
            return Collections.unmodifiableList(dataTypes);
        }

        /**
         * Parses a reqiored dataType which is (name "=" constructors)
         * 
         * @return DataType
         */
        public DataType dataType() {
            if (!tokenizer.accept(TokenType.IDENTIFIER)) { throw syntaxException("a data type name"); }
            final String name = tokenizer.lastSymbol();

            if (!tokenizer.accept(TokenType.EQUALS)) { throw syntaxException("'='"); }

            return new DataType(name, constructors());

        }

        /**
         * Parses a required list of constructors which is constructor ("|" constructor)*
         * 
         * @return List<Constructor> non empty List of constructors
         */
        public List<Constructor> constructors() {
            final List<Constructor> constructors = new ArrayList<Constructor>();
            constructors.add(constructor());
            while (tokenizer.accept(TokenType.BAR)) {
                constructors.add(constructor());
            }
            return Collections.unmodifiableList(constructors);
        }

        /**
         * Parses a required constructor which is constructorName ( "(" args ")" )?
         * 
         * @return Constructor
         */
        public Constructor constructor() {
            if (!tokenizer.accept(TokenType.IDENTIFIER)) { throw syntaxException("a constructor name"); }
            final String name = tokenizer.lastSymbol();
            if (tokenizer.accept(TokenType.LPAREN)) {
                final List<Arg> args = args();
                if (!tokenizer.accept(TokenType.RPAREN)) {
                    throw syntaxException("')'");
                } else {
                    return new Constructor(name, args);
                }
            } else {
                return new Constructor(name, Collections.<Arg> emptyList());
            }
        }

        /** 
         * Parses a required list of constructor args which is arg ("," arg)*
         * 
         * @return List<Arg> non-empty list of args
         */
        public List<Arg> args() {
            final List<Arg> args = new ArrayList<Arg>();
            args.add(arg());
            while (tokenizer.accept(TokenType.COMMA)) {
                args.add(arg());
            }
            return Collections.unmodifiableList(args);
        }

        /**
         * parses a single required constructor argument which is a type followed by a name
         * 
         * @return Arg
         */
        public Arg arg() {
            final Type type = type();
            
            if (!tokenizer.accept(TokenType.IDENTIFIER)) {
                throw syntaxException("an argument name");
            } else {
                final String name = tokenizer.lastSymbol();
                return new Arg(type, name);
            }
        }
        
        /** 
         * Returns a required class or array type, giving a syntax error on a primitive
         * 
         * @return a RefType
         */
        public RefType refType() {
            return type().accept(new Type.Visitor<RefType>() {

                @Override
                public RefType visit(Ref x) {
                    return x.type;
                }

                @Override
                public RefType visit(Primitive x) {
                    throw new SyntaxException("an array or class type (type parameters may not be primitive).  Found " + x.toString() + " and then looked for [] ");
                }});
        }
        
        /**
         * Returns a required type where a type is a primitive or classType wrapped in an array
         * 
         * @return Type
         */
        public Type type() {
            final PrimitiveType primitive = primitiveType();
            final Type baseType = primitive == null ? _Ref(classType()) : _Primitive(primitive);
            return array(baseType);
        }
        
        /**
         * Recursively wraps a type in ArrayType based on the number of "[]" pairs immediately in the stream
         * @param heldType last type parsed
         * @return either the original held type or that type wrapped in ArrayType
         */
        public Type array(Type heldType) {
            if (tokenizer.accept(TokenType.LBRACKET)) {
                if (tokenizer.accept(TokenType.RBRACKET)) {
                    return array(_Ref(_ArrayType(heldType)));
                } else {
                    throw syntaxException("]");
                }
            }
            return heldType;            
        }
        
        /**
         * Returns a required classType, which is className ("<" refType ("," refType)* ">")?
         * 
         * @return RefType
         */
        public RefType classType() {
            if (!tokenizer.accept(TokenType.IDENTIFIER)) {
                throw syntaxException("a type");
            } else {
                final String baseName = tokenizer.lastSymbol();
                final List<RefType> typeArguments = Util.<RefType>list();
                if (tokenizer.accept(TokenType.LANGLE)) {
                    typeArguments.add(refType());
                    while(tokenizer.accept(TokenType.COMMA)) {
                        typeArguments.add(refType());
                    }
                    if (!tokenizer.accept(TokenType.RANGLE)) {
                        throw syntaxException(">");
                    }
                }
                return _ClassType(baseName, typeArguments);
            }
        }
        
        /** 
         * Optionally recognizes and returns any of the primitive types
         * 
         * @return PrimitiveType or null if the next token isn't a primitve type
         */
        public PrimitiveType primitiveType() {
            if (tokenizer.accept(TokenType.BOOLEAN)) {
                return(_BooleanType); 
            } else if (tokenizer.accept(TokenType.CHAR)) {
                return(_CharType); 
            } else if (tokenizer.accept(TokenType.SHORT)) {
                return(_ShortType); 
            } else if (tokenizer.accept(TokenType.INT)) {
                return(_IntType); 
            } else if (tokenizer.accept(TokenType.LONG)) {
                return(_LongType); 
            } else if (tokenizer.accept(TokenType.FLOAT)) {
                return(_FloatType); 
            } else if (tokenizer.accept(TokenType.DOUBLE)) {
                return(_DoubleType);
            } else {
                return null;
            }            
        }
        
        /**
         * Generates (but does not throw) a new syntax exception with source and line number information

         * @param expected the kind of thing expected
         * @return A SyntaxException with information about where the problem occurred, what was expected, and what was found
         */
        private SyntaxException syntaxException(String expected) {
            return new SyntaxException("While parsing " + tokenizer.srcInfo() + ". Expected " + expected + " but found " + tokenizer.lastSymbol() + " at line " + tokenizer.lineno());        
        }
        
    }
}
