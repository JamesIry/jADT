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
import static com.pogofish.jadt.ast.PrimitiveType._ByteType;
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import com.pogofish.jadt.ast.Arg;
import com.pogofish.jadt.ast.ArgModifier;
import com.pogofish.jadt.ast.Constructor;
import com.pogofish.jadt.ast.DataType;
import com.pogofish.jadt.ast.Doc;
import com.pogofish.jadt.ast.PrimitiveType;
import com.pogofish.jadt.ast.RefType;
import com.pogofish.jadt.ast.SyntaxError;
import com.pogofish.jadt.ast.Type;
import com.pogofish.jadt.ast.Type.Primitive;
import com.pogofish.jadt.ast.Type.Ref;
import com.pogofish.jadt.printer.ASTPrinter;
import com.pogofish.jadt.util.Util;

/**
 * Internal implementation of the Parser that lets Parser be non-stateful
 *
 * @author jiry
 */
class StandardParserImpl1 implements StandardParserImpl {
    /**
     * Tokenizer to be parsed
     */
    private final ITokenizer tokenizer;
    
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
    public StandardParserImpl1(ITokenizer tokenizer) {
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
        return peek(tokenizer.punctuation());
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

    /* (non-Javadoc)
     * @see com.pogofish.jadt.parser.StandardParserImpl#doc()
     */
    @Override
    public Doc doc() {
    	StandardParser.logger.finer("Parsing doc");
    	StandardParser.logger.finer("Parsing header");
        return new Doc(tokenizer.srcInfo(), pkg(), imports(), dataTypes());
    }

    /* (non-Javadoc)
     * @see com.pogofish.jadt.parser.StandardParserImpl#pkg()
     */
    @Override
    public String pkg() {
    	StandardParser.logger.finer("Parsing package");
        if (accept(TokenType.PACKAGE)) {
            final String name = packageName();
            StandardParser.logger.finer("package " + name);
            return name;
        } else {
        	StandardParser.logger.finer("no package");
            return "";
        }
    }
    
    /**
     * Recognizes a required token that is a valid package name
     * 
     * @return String the package name
     */
    private String packageName() {
        return dottedIdentifier("a package name");
    }
    
    /* (non-Javadoc)
     * @see com.pogofish.jadt.parser.StandardParserImpl#imports()
     */
    @Override
    public List<String> imports() {
    	StandardParser.logger.finer("Parsing imports");
        final List<String> imports = new ArrayList<String>();
        
        while (accept(TokenType.IMPORT)) {
        	final String name = packageName();
        	StandardParser.logger.finer("import " + name);
            imports.add(name);
        }
        return imports;
        
    }

    /* (non-Javadoc)
     * @see com.pogofish.jadt.parser.StandardParserImpl#dataTypes()
     */
    @Override
    public List<DataType> dataTypes() {
    	StandardParser.logger.finer("Parsing datatypes");
        final List<DataType> dataTypes = new ArrayList<DataType>();

        dataTypes.add(dataType());
        
        while (!accept(TokenType.EOF)) {
            dataTypes.add(dataType());
        }
        return Collections.unmodifiableList(dataTypes);
    }

    /* (non-Javadoc)
     * @see com.pogofish.jadt.parser.StandardParserImpl#dataType()
     */
    @Override
    public DataType dataType() {
    	StandardParser.logger.finer("Parsing datatype");
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

        StandardParser.logger.finer("datatype " + name );
        final List<String> typeArguments = typeArguments();

        if (!accept(TokenType.EQUALS)) {
            // with no equals we'll pretend it was there and try to move on
            error("'='");
        }

        return new DataType(name, typeArguments, constructors());
    }
    
    /* (non-Javadoc)
     * @see com.pogofish.jadt.parser.StandardParserImpl#typeArguments()
     */
    @Override
    public List<String> typeArguments() {
        StandardParser.logger.finest("parsing type arguments");
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
        	StandardParser.logger.finest("no type arguments");
        }
        return typeArguments;
    }
    
    /* (non-Javadoc)
     * @see com.pogofish.jadt.parser.StandardParserImpl#typeArgument()
     */
    @Override
    public String typeArgument() {
        if (accept(TokenType.IDENTIFIER)) {
            final String name = tokenizer.lastSymbol();
            StandardParser.logger.finest("type argument " + name);
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

    /* (non-Javadoc)
     * @see com.pogofish.jadt.parser.StandardParserImpl#constructors()
     */
    @Override
    public List<Constructor> constructors() {
    	StandardParser.logger.finer("parsing constructors");
        final List<Constructor> constructors = new ArrayList<Constructor>();
        constructors.add(constructor());
        while (accept(TokenType.BAR)) {
            constructors.add(constructor());
        }
        return Collections.unmodifiableList(constructors);
    }

    /* (non-Javadoc)
     * @see com.pogofish.jadt.parser.StandardParserImpl#constructor()
     */
    @Override
    public Constructor constructor() {
    	StandardParser.logger.finer("parsing constructor");
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
        
        StandardParser.logger.finer("constructor " + name);
        return new Constructor(name, args());
    }

    /* (non-Javadoc)
     * @see com.pogofish.jadt.parser.StandardParserImpl#args()
     */
    @Override
    public List<Arg> args() {
        final List<Arg> args = new ArrayList<Arg>();
        if (accept(TokenType.LPAREN)) {
            StandardParser.logger.finest("parsing args");
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

    /* (non-Javadoc)
     * @see com.pogofish.jadt.parser.StandardParserImpl#arg()
     */
    @Override
    public Arg arg() {
        final List<ArgModifier> modifiers = argModifiers();
        
        final Type type = type();            

        final String name;
        if (accept(TokenType.IDENTIFIER)) {
            name = tokenizer.lastSymbol();
            StandardParser.logger.finest("arg " + name);
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

    /* (non-Javadoc)
     * @see com.pogofish.jadt.parser.StandardParserImpl#argModifiers()
     */
    @Override
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
    
    /* (non-Javadoc)
     * @see com.pogofish.jadt.parser.StandardParserImpl#argModifier()
     */
    @Override
    public ArgModifier argModifier() {
        if (accept(TokenType.FINAL)) {
            return ArgModifier._Final();
        }
        return null;
    }

    /* (non-Javadoc)
     * @see com.pogofish.jadt.parser.StandardParserImpl#refType()
     */
    @Override
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
    
    /* (non-Javadoc)
     * @see com.pogofish.jadt.parser.StandardParserImpl#type()
     */
    @Override
    public Type type() {
        final PrimitiveType primitive = primitiveType();
        final Type type = primitive == null ? _Ref(classType()) : _Primitive(primitive);
       	StandardParser.logger.finest("type " + type);
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
    
    /* (non-Javadoc)
     * @see com.pogofish.jadt.parser.StandardParserImpl#classType()
     */
    @Override
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
    
    /* (non-Javadoc)
     * @see com.pogofish.jadt.parser.StandardParserImpl#className()
     */
    @Override
    public String className() {
        return dottedIdentifier("a class name");       
    }
    
    /* (non-Javadoc)
     * @see com.pogofish.jadt.parser.StandardParserImpl#dottedIdentifier(java.lang.String)
     */
    @Override
    public String dottedIdentifier(String expected) {
        final StringBuilder builder = new StringBuilder();
        if (accept(TokenType.IDENTIFIER)) {
            builder.append(tokenizer.lastSymbol());
            if (accept(TokenType.DOT)) {
                builder.append(".");
                builder.append(dottedIdentifier(expected));
            }
        } else {
            error(expected);
            if (peekPunctuation()) {
                // most punctaion probably just means a missing name.  Leave the punctuation for somebody else
                builder.append("NO_IDENTIFIER" + nextId());
            } else {
                // otherwise consume whatever is there as if it were the name
                builder.append("BAD_IDENTIFIER_" + consumeAnything() + nextId());
            }
        }
        return builder.toString();
    }
    
    /* (non-Javadoc)
     * @see com.pogofish.jadt.parser.StandardParserImpl#primitiveType()
     */
    @Override
    public PrimitiveType primitiveType() {
        if (accept(TokenType.BOOLEAN)) {
            return(_BooleanType()); 
        } else if (accept(TokenType.BYTE)) {
            return(_ByteType()); 
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

    @Override
    public List<SyntaxError> errors() {
        return errors;
    }
}