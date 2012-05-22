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

import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import pogofish.jadt.ast.*;
import pogofish.jadt.util.Util;

public class StandardParser implements Parser {
    private static final Map<String, Token> KEYWORDS = new HashMap<String, Token>();
    
    {        
        KEYWORDS.put("import", Token.IMPORT);
        KEYWORDS.put("package", Token.PACKAGE);

        KEYWORDS.put("boolean", Token.BOOLEAN);
        KEYWORDS.put("double", Token.DOUBLE);
        KEYWORDS.put("char", Token.CHAR);
        KEYWORDS.put("float", Token.FLOAT);
        KEYWORDS.put("int", Token.INT);
        KEYWORDS.put("long", Token.LONG);
        KEYWORDS.put("short", Token.SHORT);
        
        KEYWORDS.put("abstract", Token.JAVA_KEYWORD);
        KEYWORDS.put("assert", Token.JAVA_KEYWORD);
        KEYWORDS.put("break", Token.JAVA_KEYWORD);
        KEYWORDS.put("byte", Token.JAVA_KEYWORD);
        KEYWORDS.put("case", Token.JAVA_KEYWORD);
        KEYWORDS.put("catch", Token.JAVA_KEYWORD);
        KEYWORDS.put("class", Token.JAVA_KEYWORD);
        KEYWORDS.put("const", Token.JAVA_KEYWORD);
        KEYWORDS.put("continue", Token.JAVA_KEYWORD);
        KEYWORDS.put("default", Token.JAVA_KEYWORD);
        KEYWORDS.put("do", Token.JAVA_KEYWORD);
        KEYWORDS.put("else", Token.JAVA_KEYWORD);
        KEYWORDS.put("enum", Token.JAVA_KEYWORD);
        KEYWORDS.put("extends", Token.JAVA_KEYWORD);
        KEYWORDS.put("final", Token.JAVA_KEYWORD);
        KEYWORDS.put("finally", Token.JAVA_KEYWORD);
        KEYWORDS.put("for", Token.JAVA_KEYWORD);
        KEYWORDS.put("goto", Token.JAVA_KEYWORD);
        KEYWORDS.put("if", Token.JAVA_KEYWORD);
        KEYWORDS.put("implements", Token.JAVA_KEYWORD);
        KEYWORDS.put("instanceof", Token.JAVA_KEYWORD);
        KEYWORDS.put("interface", Token.JAVA_KEYWORD);
        KEYWORDS.put("native", Token.JAVA_KEYWORD);
        KEYWORDS.put("new", Token.JAVA_KEYWORD);
        KEYWORDS.put("private", Token.JAVA_KEYWORD);
        KEYWORDS.put("protected", Token.JAVA_KEYWORD);
        KEYWORDS.put("public", Token.JAVA_KEYWORD);
        KEYWORDS.put("return", Token.JAVA_KEYWORD);
        KEYWORDS.put("static", Token.JAVA_KEYWORD);
        KEYWORDS.put("strictfp", Token.JAVA_KEYWORD);
        KEYWORDS.put("super", Token.JAVA_KEYWORD);
        KEYWORDS.put("switch", Token.JAVA_KEYWORD);
        KEYWORDS.put("synchronized", Token.JAVA_KEYWORD);
        KEYWORDS.put("this", Token.JAVA_KEYWORD);
        KEYWORDS.put("throw", Token.JAVA_KEYWORD);
        KEYWORDS.put("throws", Token.JAVA_KEYWORD);
        KEYWORDS.put("transient", Token.JAVA_KEYWORD);
        KEYWORDS.put("try", Token.JAVA_KEYWORD);
        KEYWORDS.put("void", Token.JAVA_KEYWORD);
        KEYWORDS.put("volatile", Token.JAVA_KEYWORD);
        KEYWORDS.put("while", Token.JAVA_KEYWORD);
    }
    
    private static final String IDENTIFIER_CHUNK = "\\p{javaJavaIdentifierStart}\\p{javaJavaIdentifierPart}*";
    private static final Pattern IDENTIFIER_REGEX = Pattern.compile(IDENTIFIER_CHUNK);
    private static final Pattern DOTTED_IDENTIFIER_REGEX = Pattern.compile("(" + IDENTIFIER_CHUNK + "\\.)+" + IDENTIFIER_CHUNK);
    
    /* (non-Javadoc)
     * @see sfdc.adt.IParser#parse(java.lang.String, java.io.Reader)
     */
    @Override
    public Doc parse(String srcInfo, Reader reader) throws IOException {
        final Impl impl = new Impl(srcInfo, reader);
        return impl.doc();
    }


    private static enum Token {
        PACKAGE, IMPORT, EQUALS, IDENTIFIER, DOTTED_IDENTIFIER, COMMA, BAR, LPAREN, RRPAREN, LANGLE, RANGLE, LBRACKET, RBRACKET, EOF, JAVA_KEYWORD, UNKNOWN, BOOLEAN, DOUBLE, CHAR, FLOAT, INT, LONG, SHORT;
    }
    private class Impl {
        
        private final String srcInfo;
        private final StreamTokenizer tokenizer;
    
        private String symbol = null;
    
        private Impl(String srcInfo, final Reader reader) throws IOException {
            this.srcInfo = srcInfo;
            tokenizer = new StreamTokenizer(reader);
            tokenizer.resetSyntax();
            tokenizer.slashSlashComments(true);
            tokenizer.slashStarComments(true);
            tokenizer.wordChars('a', 'z');
            tokenizer.wordChars('A', 'Z');
            tokenizer.wordChars('0', '9');
            tokenizer.wordChars('.', '.');
            tokenizer.ordinaryChars('<', '<');
            tokenizer.ordinaryChars('>', '>');
            tokenizer.ordinaryChar('=');
            tokenizer.ordinaryChar('(');
            tokenizer.ordinaryChar(')');
            tokenizer.ordinaryChar(',');
            tokenizer.ordinaryChar('|');
            tokenizer.ordinaryChar('[');
            tokenizer.ordinaryChar(']');
            tokenizer.whitespaceChars(' ', ' ');
            tokenizer.whitespaceChars('\n', '\n');
            tokenizer.whitespaceChars('\r', '\r');
            tokenizer.eolIsSignificant(false);
        }
    
        private Token getToken() throws IOException {
            final int tokenType = tokenizer.nextToken();
            switch (tokenType) {
            case StreamTokenizer.TT_EOF:
                return Token.EOF;
            case StreamTokenizer.TT_WORD:
                symbol = tokenizer.sval;
                if (KEYWORDS.containsKey(symbol)) {
                    return KEYWORDS.get(symbol);
                } else {
                    final Matcher identifierMatcher = IDENTIFIER_REGEX.matcher(symbol);
                    if (identifierMatcher.matches()) {
                        return Token.IDENTIFIER;
                    }
                    final Matcher dottedIdentifierMatcher = DOTTED_IDENTIFIER_REGEX.matcher(symbol);
                    if (dottedIdentifierMatcher.matches()) {
                        return Token.DOTTED_IDENTIFIER;
                    }
                    return Token.UNKNOWN;
                }
            case '(':
                symbol = "(";
                return Token.LPAREN;
            case ')':
                symbol = ")";
                return Token.RRPAREN;
            case '<':
                symbol = "<";
                return Token.LANGLE;
            case '>':
                symbol = ">";
                return Token.RANGLE;
            case '[':
                symbol = "<";
                return Token.LBRACKET;
            case ']':
                symbol = ">";
                return Token.RBRACKET;
            case '=':
                symbol = "=";
                return Token.EQUALS;
            case ',':
                symbol = ",";
                return Token.COMMA;
            case '|':
                symbol = "|";
                return Token.BAR;
            default:
                symbol = "" + (char)tokenType;
                return Token.UNKNOWN;
            }
        }
    
        private boolean accept(Token expected) throws IOException {
            final Token token = getToken();
            if (token.equals(expected)) {
                return true;
            } else {
                tokenizer.pushBack();
                return false;
            }
        }
    
        public Doc doc() throws IOException {
            final String pkg = pkg();
            return new Doc(srcInfo, pkg, imports(), dataTypes());
        }
        
        private List<String> imports() throws IOException {
            final List<String> imports = new ArrayList<String>();
            
            while (accept(Token.IMPORT)) {
                imports.add(packageName());
            }
            return imports;
            
        }
    
        private List<DataType> dataTypes() throws IOException {
            final List<DataType> dataTypes = new ArrayList<DataType>();
    
            while (!accept(Token.EOF)) {
                dataTypes.add(dataType());
            }
            return Collections.unmodifiableList(dataTypes);
        }
    
        private DataType dataType() throws IOException {
            if (!accept(Token.IDENTIFIER)) { throw syntaxException("a data type name"); }
            final String name = symbol;
    
            if (!accept(Token.EQUALS)) { throw syntaxException("'='"); }
    
            return new DataType(name, constructors());
    
        }
    
        private List<Constructor> constructors() throws IOException {
            final List<Constructor> constructors = new ArrayList<Constructor>();
            constructors.add(constructor());
            while (accept(Token.BAR)) {
                constructors.add(constructor());
            }
            return Collections.unmodifiableList(constructors);
        }
    
        private Constructor constructor() throws IOException {
            if (!accept(Token.IDENTIFIER)) { throw syntaxException("a constructor name"); }
            final String name = symbol;
            if (accept(Token.LPAREN)) {
                final List<Arg> args = args();
                if (!accept(Token.RRPAREN)) {
                    throw syntaxException("')'");
                } else {
                    return new Constructor(name, args);
                }
            } else {
                return new Constructor(name, Collections.<Arg> emptyList());
            }
        }
    
        private List<Arg> args() throws IOException {
            final List<Arg> args = new ArrayList<Arg>();
            args.add(arg());
            while (accept(Token.COMMA)) {
                args.add(arg());
            }
            return Collections.unmodifiableList(args);
        }
    
        private Arg arg() throws IOException {
            final Type type = type();
            
            if (!accept(Token.IDENTIFIER)) {
                throw syntaxException("an argument name");
            } else {
                final String name = symbol;
                return new Arg(type, name);
            }
        }
        
        private Type type() throws IOException {
            final PrimitiveType primitive = primitiveType();
            Type type = primitive == null ? _Ref(classType()) : _Primitive(primitive);
            
            while (accept(Token.LBRACKET)) {
                if (accept(Token.RBRACKET)) {
                    type = _Ref(_ArrayType(type));
                } else {
                    throw syntaxException("]");
                }
            }
            return type;
        }
        
        private RefType classType() throws IOException {
            if (!accept(Token.IDENTIFIER)) {
                syntaxException("a type");
            }
            final String baseName = symbol;
            final List<RefType> typeArguments = Util.<RefType>list();
            if (accept(Token.LANGLE)) {
                typeArguments.add(refType());
                while(accept(Token.COMMA)) {
                    typeArguments.add(refType());
                }
                if (!accept(Token.RANGLE)) {
                    throw syntaxException(">");
                }
            }
            return _ClassType(baseName, typeArguments);
        }

        private RefType refType() throws IOException {
            RefType type = null;
            final PrimitiveType primitive = primitiveType();
            if (primitive != null) {
                if (accept(Token.LBRACKET)) {
                    if (accept(Token.RBRACKET)) {
                        type = _ArrayType(_Primitive(primitive));
                    } else {
                        throw syntaxException("]");
                    }
                } else {
                    throw syntaxException("non primitive type");
                }
            } else {
                type = classType();
            }
            
            while (accept(Token.LBRACKET)) {
                if (accept(Token.RBRACKET)) {
                    type = _ArrayType(_Ref(type));
                } else {
                    throw syntaxException("]");
                }
            }
            
            return type;
        }
        
        private PrimitiveType primitiveType() throws IOException {
            if (accept(Token.BOOLEAN)) {
                return(_BooleanType); 
            } else if (accept(Token.CHAR)) {
                return(_CharType); 
            } else if (accept(Token.SHORT)) {
                return(_ShortType); 
            } else if (accept(Token.INT)) {
                return(_IntType); 
            } else if (accept(Token.LONG)) {
                return(_LongType); 
            } else if (accept(Token.FLOAT)) {
                return(_FloatType); 
            } else if (accept(Token.DOUBLE)) {
                return(_LongType);
            } else {
                return null;
            }            
        }

        private String pkg() throws IOException {
            if (accept(Token.PACKAGE)) {
                return packageName();
            } else {
                return "";
            }
        }
        
        private String packageName() throws IOException {
            if (accept(Token.IDENTIFIER)) {
                return symbol;
            } else if (accept(Token.DOTTED_IDENTIFIER)) {
                return symbol;
            } else {
                throw syntaxException("a package name");
            }
        }
        
        private SyntaxException syntaxException(String expected) {
            return new SyntaxException("While parsing " + srcInfo + ". Expected " + expected + " but found " + symbol + " at line " + tokenizer.lineno());        
        }
    }
}
