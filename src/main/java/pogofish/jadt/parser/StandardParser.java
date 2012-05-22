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

import static pogofish.jadt.util.Util.set;

import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import pogofish.jadt.ast.*;

public class StandardParser implements Parser {
    private static final Set<String> JAVA_TYPES = set("boolean", "char", "double", "float", "int",
            "long", "short");
    private static final Set<String> JAVA_KEYWORDS = set("abstract", "assert", "boolean", "break", "byte", "case",
            "catch", "char", "class", "const", "continue", "default", "do", "double", "else", "enum", "extends",
            "final", "finally", "float", "for", "goto", "if", "implements", "import", "instanceof", "int", "interface",
            "long", "native", "new", "package", "private", "protected", "public", "return", "short", "static",
            "strictfp", "super", "switch", "synchronized", "this", "throw", "throws", "transient", "try", "void",
            "volatile", "while");
    
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
        PACKAGE, IMPORT, EQUALS, IDENTIFIER, DOTTED_IDENTIFIER, COMMA, BAR, LPAREN, RRPAREN, LANGLE, RANGLE, LBRACKET, RBRACKET, EOF, JAVA_TYPE, JAVA_KEYWORD, UNKNOWN;
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
                if (symbol.equals("package")) {
                    return Token.PACKAGE; 
                } else if (symbol.equals("import")) {
                    return Token.IMPORT;
                } else if (JAVA_TYPES.contains(symbol)) {
                    return Token.JAVA_TYPE;
                } else if (JAVA_KEYWORDS.contains(symbol)) {
                    return Token.JAVA_KEYWORD;
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
            final String type = type();
            
            if (!accept(Token.IDENTIFIER)) {
                throw syntaxException("an argument name");
            } else {
                final String name = symbol;
                return new Arg(type, name);
            }
        }
        
        private String type() throws IOException {
            final StringBuilder type = new StringBuilder();
            if (accept(Token.JAVA_TYPE)) {
                type.append(symbol);
            } else if (accept(Token.IDENTIFIER)) {
                type.append(symbol);

                if (accept(Token.LANGLE)) {
                    type.append("<");
                    type.append(type());
                    while (accept(Token.COMMA)) {
                        type.append(", ");
                        type.append(type());
                    }
                    if (!accept(Token.RANGLE)) { throw syntaxException(">"); }
                    type.append(">");
                }
            } else {
                throw syntaxException("a type");
            }
            
            if (accept(Token.LBRACKET)) {
                if (!accept(Token.RBRACKET)) {
                    throw syntaxException("]");
                } else {
                    type.append("[]");
                }
            }

            return type.toString();
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
