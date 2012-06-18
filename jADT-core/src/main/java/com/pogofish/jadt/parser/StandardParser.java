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
package com.pogofish.jadt.parser;

import static com.pogofish.jadt.ast.PrimitiveType._BooleanType;
import static com.pogofish.jadt.ast.PrimitiveType._CharType;
import static com.pogofish.jadt.ast.PrimitiveType._DoubleType;
import static com.pogofish.jadt.ast.PrimitiveType._FloatType;
import static com.pogofish.jadt.ast.PrimitiveType._IntType;
import static com.pogofish.jadt.ast.PrimitiveType._LongType;
import static com.pogofish.jadt.ast.PrimitiveType._ShortType;
import static com.pogofish.jadt.ast.RefType._ArrayType;
import static com.pogofish.jadt.ast.RefType._ClassType;
import static com.pogofish.jadt.ast.Type._Primitive;
import static com.pogofish.jadt.ast.Type._Ref;
import static com.pogofish.jadt.util.Util.set;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import com.pogofish.jadt.ast.Arg;
import com.pogofish.jadt.ast.ArgModifier;
import com.pogofish.jadt.ast.Constructor;
import com.pogofish.jadt.ast.DataType;
import com.pogofish.jadt.ast.Doc;
import com.pogofish.jadt.ast.ParseResult;
import com.pogofish.jadt.ast.PrimitiveType;
import com.pogofish.jadt.ast.RefType;
import com.pogofish.jadt.ast.SyntaxError;
import com.pogofish.jadt.ast.Type;
import com.pogofish.jadt.ast.Type.Primitive;
import com.pogofish.jadt.ast.Type.Ref;
import com.pogofish.jadt.printer.ASTPrinter;
import com.pogofish.jadt.source.Source;
import com.pogofish.jadt.util.IOExceptionAction;
import com.pogofish.jadt.util.Util;

/**
 * The standard parse for jADT description files
 *
 * @author jiry
 */
public class StandardParser implements Parser {
	private static final Logger logger = Logger.getLogger(StandardParser.class.toString());

    
    /* (non-Javadoc)
     * @see sfdc.adt.IParser#parse(java.lang.String, java.io.Reader)
     */
    @Override
    public ParseResult parse(Source source)  {
    	logger.fine("Parsing " + source.getSrcInfo());
    	final BufferedReader reader = source.createReader();
    	try {
            final Tokenizer tokenizer = new Tokenizer(source.getSrcInfo(), reader);
            final Impl impl = new Impl(tokenizer);
            final Doc doc = impl.doc();
            return new ParseResult(doc, impl.errors);
    	} finally {
    	    new IOExceptionAction<Void>() {
                @Override
                public Void doAction() throws IOException {
                    reader.close();
                    return null;
                }
            }.execute();
    	}
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
         * Whether the parser is currently recovering from an error. If true, new errors
         * will not be added to the errors list 
         */
        private boolean recovering = false;
        
        /**
         * Errors found during parse
         */
        List<SyntaxError> errors = new ArrayList<SyntaxError>();
        
        /**
         * Unique id generator to try to prevent creating duplicate names when tokens are injected
         */
        private int _nextId = 0;
        

        /**
         * Unique id generator to try to prevent creating duplicate names when tokens are injected
         */
        private String nextId() {
            return "@" + (++_nextId);
        }
        
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
         * Peeks at the next available token without removing it, returning true if it's in the given set
         * 
         * @return
         */
        private boolean peek(Set<TokenType> expected) {
            final TokenType token = tokenizer.getTokenType();
            tokenizer.pushBack();
            return expected.contains(token);
        }
        
        /**
         * Peeks at the next available token without removing, returning true if it's in the set of punctuation tokens
         * @return
         */
        private boolean peekPunctuation() {
            return peek(tokenizer.punctuation);
        }

        /**
         * If the next token type is the expected token type then it is consumed and true is returned
         * otherwise false is returned and the token is not consumed
         * @param expected
         */
        private boolean accept(TokenType expected) {
            final TokenType token = tokenizer.getTokenType();
            if (token.equals(expected)) {
                recovering = false;
                return true;
            } else {
                tokenizer.pushBack();
                return false;
            }
        }
        
        /**
         * Consume any token type and return the symbol.  Used during error recovery
         */
        private String consumeAnything() {
            tokenizer.getTokenType();
            return tokenizer.lastSymbol();
        }

        /**
         * Parses a complete document which is pkg imports dataTypes
         * 
         * @return Doc
         */
        public Doc doc() {
        	logger.finer("Parsing doc");
        	logger.finer("Parsing header");
            return new Doc(tokenizer.srcInfo(), pkg(), imports(), dataTypes());
        }

        /** 
         * Gets an optional package declaration "pacakage" packageName
         * 
         * @return String the package name, or, because package is optional returns an empty string ""
         */
        public String pkg() {
        	logger.finer("Parsing package");
            if (accept(TokenType.PACKAGE)) {
                final String name = packageName();
                logger.finer("package " + name);
                return name;
            } else {
            	logger.finer("no package");
                return "";
            }
        }
        
        /**
         * Recognizes a required token that is a valid package name
         * 
         * @return String the package name
         */
        private String packageName() {
            if (accept(TokenType.IDENTIFIER)) {
                return tokenizer.lastSymbol();
            } else if (accept(TokenType.DOTTED_IDENTIFIER)) {
                return tokenizer.lastSymbol();
            } else {
                error("a package name");
                if (peek(set(TokenType.IMPORT, TokenType.PACKAGE, TokenType.EOF))) {
                    // if the token was import or package then assume they just forgot the package name
                    // and move on
                    return "NO_PACKAGE_NAME" + nextId();                    
                } else {
                    // otherwise assume that they used a keyword as a package name.  Consume it and move on
                    return "BAD_PACKAGE_NAME_" + consumeAnything() + nextId();
                }
            }
        }
        
        /**
         * Parses an optional list of imports which is ("import" packageName)*
         * 
         * @return List<String> possibly empty list of imports
         */
        public List<String> imports() {
        	logger.finer("Parsing imports");
            final List<String> imports = new ArrayList<String>();
            
            while (accept(TokenType.IMPORT)) {
            	final String name = packageName();
            	logger.finer("import " + name);
                imports.add(name);
            }
            return imports;
            
        }

        /**
         * Parses a required list of datatypes which is dataType (dataType)*
         * 
         * @return List<DataType> non empty list of DataTypes
         */
        public List<DataType> dataTypes() {
        	logger.finer("Parsing datatypes");
            final List<DataType> dataTypes = new ArrayList<DataType>();

            dataTypes.add(dataType());
            
            while (!accept(TokenType.EOF)) {
                dataTypes.add(dataType());
            }
            return Collections.unmodifiableList(dataTypes);
        }

        /**
         * Parses a dataType which is (name typeArguments? "=" constructors)
         * 
         * @return DataType
         */
        public DataType dataType() {
        	logger.finer("Parsing datatype");
        	final String name;
        	if (accept(TokenType.IDENTIFIER)) {
        	    name = tokenizer.lastSymbol();
        	} else {
                error("a data type name");               
        	    if (peekPunctuation()) {
        	        // if the token was punctuation then they just forgot a name, make one up and move on
                    name = "NO_DATA_TYPE_NAME" + nextId();        	        
        	    } else {
        	        // otherwise let's consume the token as if it was the name and try to move on
        	        name = "BAD_DATA_TYPE_NAME_" + consumeAnything() + nextId();
        	    }
        	}

            logger.finer("datatype " + name );
            final List<String> typeArguments = typeArguments();

            if (!accept(TokenType.EQUALS)) {
                // with no equals we'll pretend it was there and try to move on
                error("'='");
            }

            return new DataType(name, typeArguments, constructors());
        }
        
        /**
         * Parses an optional type arguments which is ('<' typeArgument (',' typeArgument)* '>')?
         * 
         * @return list of string names of type arguments. Empty if none
         */
        public List<String> typeArguments() {
            logger.finest("parsing type arguments");
            final List<String> typeArguments = new ArrayList<String>();
        	
            if(accept(TokenType.LANGLE)) {
            	typeArguments.add(typeArgument());
	            while (accept(TokenType.COMMA)) {
	            	typeArguments.add(typeArgument());
	            }
	            boolean done = false;
	            while (!done) {	                
    	            if (accept(TokenType.RANGLE)) {
    	            	done = true;
    	            } else {
                        error("'>'");
                        if (peekPunctuation()) {
                            // just forget the >, move on, leaving the equals to be found later
                            done = true;
    
                        } else {
                            // probably just missing a comma, consume whatever you have, add it to the list,
                            // and keep looking for the >
                            typeArguments.add(typeArgument());
                        }
    	            }
	            }
            } else {
            	logger.finest("no type arguments");
            }
            return typeArguments;
        }
        
        /**
         * A required type argument which is just any identifier
         */
        public String typeArgument() {
            if (accept(TokenType.IDENTIFIER)) {
                final String name = tokenizer.lastSymbol();
                logger.finest("type argument " + name);
                return tokenizer.lastSymbol();  
            } else {
                error("a type parameter");
                if (peekPunctuation()) {
                    // missing a name, leave the punctuation for later
                    return "NO_TYPE_ARGUMENT" + nextId();
                } else {
                    // consume whatever you have and pretend it's a type argument
                    return "BAD_TYPE_ARGUMENT_" + consumeAnything() + nextId();
                }                
            }
        }

        /**
         * Parses a required list of constructors which is constructor ("|" constructor)*
         * 
         * @return List<Constructor> non empty List of constructors
         */
        public List<Constructor> constructors() {
        	logger.finer("parsing constructors");
            final List<Constructor> constructors = new ArrayList<Constructor>();
            constructors.add(constructor());
            while (accept(TokenType.BAR)) {
                constructors.add(constructor());
            }
            return Collections.unmodifiableList(constructors);
        }

        /**
         * Parses a required constructor which is constructorName args
         * 
         * @return Constructor
         */
        public Constructor constructor() {
        	logger.finer("parsing constructor");
        	final String name;
        	
            if (accept(TokenType.IDENTIFIER)) {
                name = tokenizer.lastSymbol();
            } else {
                error("a constructor name");
                if (peekPunctuation()) {
                    // we're proably missing a constructor name.  Leave the punctuatino somebody else
                    name = "NO_CONSTRUCTOR_NAME" + nextId();
                } else {
                    // otherwise consume whatever we have as the name
                    name = "BAD_CONSTRUCTOR_NAME_" + consumeAnything() + nextId(); 
                }
            }
            
            logger.finer("constructor " + name);
            return new Constructor(name, args());
        }

        /** 
         * Parses an optional list of constructor args which is ("(" arg ("," arg)* ")")?
         * 
         * @return List<Arg> non-empty list of args
         */
        public List<Arg> args() {
            final List<Arg> args = new ArrayList<Arg>();
            if (accept(TokenType.LPAREN)) {
                logger.finest("parsing args");
                args.add(arg());
                while (accept(TokenType.COMMA)) {
                    args.add(arg());
                }
                boolean done = false;
                while (!done) {
                    if (accept(TokenType.RPAREN)) {
                        done = true;
                    } else {
                        error("')'");
                        if (peekPunctuation()) {
                            // probably just forgot a closing paren, we're done, leave the punctuation for somebody else
                            done = true;
                        } else {
                            // probably just missing a comma, consume whatever you have, add it to the list,
                            // and keep looking for the >
                            args.add(arg());
                        }

                    }
                }
            }           
            return Collections.unmodifiableList(args);
        }

        /**
         * parses a single required constructor argument which is a list of modifiers followed by a type followed by a name
         * 
         * @return Arg
         */
        public Arg arg() {
            final List<ArgModifier> modifiers = argModifiers();
            
            final Type type = type();            

            final String name;
            if (accept(TokenType.IDENTIFIER)) {
                name = tokenizer.lastSymbol();
                logger.finest("arg " + name);
            } else {             
                error("an argument name");
                if (peekPunctuation()) {
                    // Probably just missing a name.  Leave the token for somebody else
                    name = "NO_ARG_NAME" + nextId();
                } else {
                    // it's not a good name but is used as one, consume it
                    name = "BAD_ARG_NAME_" + consumeAnything() + nextId();
                }
            }
            return new Arg(modifiers, type, name);
        }

        /**
         * Parses a possibly empty list of arg modifiers
         */
        public List<ArgModifier> argModifiers() {
            final List<ArgModifier> modifiers = Util.<ArgModifier>list();

            ArgModifier modifier;
            do {
                modifier = argModifier();
                if (modifier != null) {
                    modifiers.add(modifier);
                }
            } while (modifier != null);

            return modifiers;
        }
        
        /**
         * Parses one optional arg modifier or null if there isn't one
         */
        public ArgModifier argModifier() {
            if (accept(TokenType.FINAL)) {
                return ArgModifier._Final();
            }
            return null;
        }

        /** 
         * Returns a required class or array type, giving a syntax error on a primitive
         * 
         * @return a RefType
         */
        public RefType refType() {
            return type().match(new Type.MatchBlock<RefType>() {

                @Override
                public RefType _case(Ref x) {
                    return x.type;
                }

                @Override
                public RefType _case(Primitive x) {
                    final String primitiveName = ASTPrinter.print(x);
                    error ("an array or class type", primitiveName);
                    // we got a primitive when we expected an array or class, so make up a class
                    return _ClassType("BAD_CLASS_" + primitiveName + nextId(), Util.<RefType>list());

                }});
        }
        
        /**
         * Returns a required type where a type is a primitive or classType wrapped in an array
         * 
         * @return Type
         */
        public Type type() {
            final PrimitiveType primitive = primitiveType();
            final Type type = primitive == null ? _Ref(classType()) : _Primitive(primitive);
           	logger.finest("type " + type);
            return array(type);
        }
        
        /**
         * Recursively wraps a type in ArrayType based on the number of "[]" pairs immediately in the stream
         * @param heldType last type parsed
         * @return either the original held type or that type wrapped in ArrayType
         */
        public Type array(Type heldType) {
            if (accept(TokenType.LBRACKET)) {
                if (!accept(TokenType.RBRACKET)) {
                    error("']'");
                    // leave whatever is lying around for somebody else to figure out                    
                }
                return array(_Ref(_ArrayType(heldType)));
            }
            return heldType;            
        }
        
        /**
         * Returns a required classType, which is className ("<" refType ("," refType)* ">")?
         * 
         * @return RefType
         */
        public RefType classType() {
            final String className = className();
            
            final List<RefType> typeArguments = Util.<RefType>list();
            if (accept(TokenType.LANGLE)) {
                typeArguments.add(refType());
                while(accept(TokenType.COMMA)) {
                    typeArguments.add(refType());
                }
                boolean done = false;
                while (!done) {
                    if (accept(TokenType.RANGLE)) {
                        done = true;
                    } else {
                        error("'>'");
                        if (peekPunctuation()) {
                            // got punctuation...um...pretend we're done and leave the punctuation for later
                            done = true;
                        } else {
                            // got something else, try to treat it as a refType
                            typeArguments.add(refType());
                        }
                    }
                }
            }
            return _ClassType(className, typeArguments);
        }
        
        /**
         * Returns a class name which is either an identifier or a dotted identifier
         */
        public String className() {
            if (accept(TokenType.IDENTIFIER) || (accept(TokenType.DOTTED_IDENTIFIER))) {
                return tokenizer.lastSymbol();
            } else {
                error("a class name");
                if (peekPunctuation()) {
                    // most punctaion probably just means a missing name.  Leave the punctuation for somebody else
                    return "NO_CLASS_NAME" + nextId();
                } else {
                    // otherwise consume whatever is there as if it were the name
                    return "BAD_CLASS_NAME_" + consumeAnything() + nextId();
                }
            }           
        }
        
        /** 
         * Optionally recognizes and returns any of the primitive types
         * 
         * @return PrimitiveType or null if the next token isn't a primitive type
         */
        public PrimitiveType primitiveType() {
            if (accept(TokenType.BOOLEAN)) {
                return(_BooleanType()); 
            } else if (accept(TokenType.CHAR)) {
                return(_CharType()); 
            } else if (accept(TokenType.SHORT)) {
                return(_ShortType()); 
            } else if (accept(TokenType.INT)) {
                return(_IntType()); 
            } else if (accept(TokenType.LONG)) {
                return(_LongType()); 
            } else if (accept(TokenType.FLOAT)) {
                return(_FloatType()); 
            } else if (accept(TokenType.DOUBLE)) {
                return(_DoubleType());
            } else {
                return null;
            }            
        }

        /**
         * If not currently recovering, adds an error to the errors list and sets recovering to true.
         * Actual is assumed to be the last symbol from the tokenizer.

         * @param expected the kind of thing expected
         */
        private void error(String expected) {
            error(expected, tokenizer.lastSymbol());
        }
        
        /**
         * If not currently recovering, adds an error to the errors list and sets recovering to true
         * @param expected the kind of thing expected
         */
        private void error(String expected, String actual) {
            if (!recovering) {
                final String outputString = "<EOF>".equals(actual) ? actual : "'" + actual + "'";
                errors.add(SyntaxError._UnexpectedToken(expected, outputString, tokenizer.lineno()));
                recovering = true;
            }
        }
    }
}
