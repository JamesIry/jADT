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
package com.pogofish.jadt.emitter;

import static com.pogofish.jadt.util.Util.set;

import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import com.pogofish.jadt.ast.Arg;
import com.pogofish.jadt.ast.Constructor;
import com.pogofish.jadt.ast.JavaComment;
import com.pogofish.jadt.ast.PrimitiveType;
import com.pogofish.jadt.ast.PrimitiveType.BooleanType;
import com.pogofish.jadt.ast.PrimitiveType.ByteType;
import com.pogofish.jadt.ast.PrimitiveType.CharType;
import com.pogofish.jadt.ast.PrimitiveType.IntType;
import com.pogofish.jadt.ast.PrimitiveType.ShortType;
import com.pogofish.jadt.ast.RefType;
import com.pogofish.jadt.ast.RefType.ArrayType;
import com.pogofish.jadt.ast.RefType.ClassType;
import com.pogofish.jadt.ast.Type;
import com.pogofish.jadt.ast.Type.Primitive;
import com.pogofish.jadt.ast.Type.Ref;
import com.pogofish.jadt.comments.CommentProcessor;
import com.pogofish.jadt.printer.ASTPrinter;
import com.pogofish.jadt.sink.Sink;

public class StandardClassBodyEmitter implements ClassBodyEmitter {
    private static final Set<String> CONSTRUCTOR_METHOD_STRIP = set("@return");
    private static final CommentProcessor commentProcessor = new CommentProcessor();
	private static final Logger logger = Logger.getLogger(StandardClassBodyEmitter.class.toString());
    
    /* (non-Javadoc)
     * @see com.pogofish.jadt.emitter.ClassBodyEmitter#constructorFactory(com.pogofish.jadt.emitter.Sink, java.lang.String, java.lang.String, com.pogofish.jadt.ast.Constructor)
     */
    @Override
    public void constructorFactory(Sink sink, String dataTypeName, String factoryName, List<String> typeParametrs, Constructor constructor) {
        if (constructor.args.isEmpty()) {
        	if (typeParametrs.isEmpty()) {
            	logger.finest("Generating no args, no types factory for " + constructor.name);
        	} else {
            	logger.finest("Generating no args, type arguments factory for " + constructor.name);
        		sink.write("   @SuppressWarnings(\"rawtypes\")\n");
        	}
            sink.write("   private static final " + dataTypeName + " _" + factoryName + " = new " + constructor.name + "();\n");
            sink.write(ASTPrinter.printComments("   ", commentProcessor.leftAlign(commentProcessor.javaDocOnly(constructor.comments))));
        	if (!typeParametrs.isEmpty()) {
        		sink.write("   @SuppressWarnings(\"unchecked\")\n");
        	}
        	sink.write("   public static final ");
        	emitParameterizedTypeName(sink, typeParametrs);
        	sink.write(" ");
        	sink.write(dataTypeName);
        	emitParameterizedTypeName(sink, typeParametrs);
        	sink.write(" _" + factoryName + "() { return _" + factoryName + "; }");
        } else {
        	logger.finest("Generating args factory for " + constructor.name);
            sink.write(ASTPrinter.printComments("   ", commentProcessor.leftAlign(constructor.comments)));
            sink.write("   public static final ");
            emitParameterizedTypeName(sink, typeParametrs);
        	sink.write(" ");
        	sink.write(dataTypeName);
            emitParameterizedTypeName(sink, typeParametrs);
            sink.write(" _" + factoryName + "(");
            constructorArgs(sink, constructor, true);
            sink.write(") { return new " + constructor.name);
            emitParameterizedTypeName(sink, typeParametrs);
            sink.write("("); 
            constructorArgs(sink, constructor, false);
            sink.write("); }");            
        }
    }
    
    private void constructorArgs(Sink sink, Constructor constructor, boolean withTypes) {
        boolean first = true;
        for (Arg arg : constructor.args) {
            if (first) {
                first = false;
            } else {
                sink.write(", ");
            }
            sink.write(constructorArg(arg, withTypes));
        }
    }
    
    
    /* (non-Javadoc)
     * @see com.pogofish.jadt.emitter.ClassBodyEmitter#emitConstructorMethod(com.pogofish.jadt.emitter.Sink, com.pogofish.jadt.ast.Constructor)
     */
    @Override
    public void emitConstructorMethod(Sink sink, String indent, Constructor constructor) {
    	logger.finest("Generating constructor method for " + constructor.name);
    	final List<JavaComment> javaDoc = commentProcessor.leftAlign(commentProcessor.javaDocOnly(constructor.comments));
    	for (Arg arg : constructor.args) {
    	    final List<JavaComment> paramDoc = commentProcessor.paramDoc(arg.name, javaDoc);
    	    sink.write(ASTPrinter.printComments(indent, paramDoc));
            sink.write(indent + "public ");
            sink.write(ASTPrinter.printArgModifiers(arg.modifiers));
            sink.write(ASTPrinter.print(arg.type) + " " + arg.name + ";\n");
        }
    	sink.write("\n");
        sink.write(ASTPrinter.printComments(indent, commentProcessor.leftAlign(commentProcessor.stripTags(CONSTRUCTOR_METHOD_STRIP, commentProcessor.javaDocOnly(constructor.comments)))));    	
        sink.write(indent + "public " + constructor.name + "("); 
        constructorArgs(sink, constructor, true);        
        sink.write(") {");
        for (Arg arg : constructor.args) {
            sink.write("\n" + indent + "   this." + arg.name + " = " + arg.name + ";");
        }
        sink.write("\n" + indent + "}");
    }

    /* (non-Javadoc)
     * @see com.pogofish.jadt.emitter.ClassBodyEmitter#emitToString(com.pogofish.jadt.emitter.Sink, com.pogofish.jadt.ast.Constructor)
     */
    @Override
    public void emitToString(Sink sink, String indent, Constructor constructor) {
    	logger.finest("Generating toString() for " + constructor.name);
        sink.write(indent + "@Override\n");
        sink.write(indent + "public String toString() {\n");
        sink.write(indent + "   return \"" + constructor.name);
        if (!constructor.args.isEmpty()) {
            sink.write("(");
            boolean first = true;
            for (Arg arg : constructor.args) {
                if (first) {
                    first = false;
                } else {
                    sink.write(", ");
                }
                sink.write(arg.name + " = \" + " + arg.name + " + \"");
            }
            sink.write(")");
        }
        sink.write("\";\n");
        sink.write(indent + "}");
    }

    /* (non-Javadoc)
     * @see com.pogofish.jadt.emitter.ClassBodyEmitter#emitEquals(com.pogofish.jadt.emitter.Sink, com.pogofish.jadt.ast.Constructor)
     */
    @Override
    public void emitEquals(final Sink sink, final String indent, Constructor constructor, List<String> typeArguments) {
    	logger.finest("Generating equals() for " + constructor.name);
        sink.write(indent + "@Override\n");
        sink.write(indent + "public boolean equals(Object obj) {\n");
        sink.write(indent + "   if (this == obj) return true;\n");
        sink.write(indent + "   if (obj == null) return false;\n");
        sink.write(indent + "   if (getClass() != obj.getClass()) return false;\n");
        if (!constructor.args.isEmpty()) {
        	if (!typeArguments.isEmpty()) {
        		sink.write(indent + "   @SuppressWarnings(\"rawtypes\")");
        	}
            sink.write(indent + "   " + constructor.name + " other = (" + constructor.name + ")obj;\n");
            
            for (final Arg arg : constructor.args) {
                arg.type._switch(new Type.SwitchBlock(){
                    @Override
                    public void _case(Ref x) {
                        x.type._switch(new RefType.SwitchBlock() {
                            @Override
                            public void _case(ClassType x) {
                                sink.write(indent + "   if (" + arg.name + " == null) {\n");
                                sink.write(indent + "      if (other." + arg.name + " != null) return false;\n");
                                sink.write(indent + "   } else if (!" + arg.name + ".equals(other." + arg.name + ")) return false;\n");
                            }

                            @Override
                            public void _case(ArrayType x) {
                                sink.write(indent + "   if (!java.util.Arrays.equals(" + arg.name + ", other." + arg.name + ")) return false;\n");
                            }});
                    }

                    @Override
                    public void _case(Primitive x) {
                        sink.write(indent + "   if (" + arg.name + " != other." + arg.name + ") return false;\n");
                    }});
            }
        }
        sink.write(indent + "   return true;\n");
        sink.write(indent + "}");
    }

    /* (non-Javadoc)
     * @see com.pogofish.jadt.emitter.ClassBodyEmitter#emitHashCode(com.pogofish.jadt.emitter.Sink, com.pogofish.jadt.ast.Constructor)
     */
    @Override
    public void emitHashCode(final Sink sink, final String indent, Constructor constructor) {
    	logger.finest("Generating hashCode() for " + constructor.name);
        sink.write(indent + "@Override\n");
        sink.write(indent + "public int hashCode() {\n");
        if (constructor.args.isEmpty()) {
            sink.write(indent + "    return 0;\n");
        } else {
            sink.write(indent + "    final int prime = 31;\n");
            sink.write(indent + "    int result = 1;\n");
            for (final Arg arg : constructor.args) {
                arg.type._switch(new Type.SwitchBlock(){
                    @Override
                    public void _case(Ref x) {
                        x.type._switch(new RefType.SwitchBlock() {
                            @Override
                            public void _case(ClassType x) {
                                sink.write(indent + "    result = prime * result + ((" + arg.name + " == null) ? 0 : " + arg.name + ".hashCode());\n");                
                            }

                            @Override
                            public void _case(ArrayType x) {
                                sink.write(indent + "    result = prime * result + java.util.Arrays.hashCode(" + arg.name + ");\n");                
                            }});
                    }

                    @Override
                    public void _case(Primitive x) {
                        x.type._switch(new PrimitiveType.SwitchBlockWithDefault() {

                            @Override
                            public void _case(BooleanType x) {
                                sink.write(indent + "    result = prime * result + (" + arg.name + " ? 1 : 0);\n");
                            }

                            @Override
                            public void _case(IntType x) {
                                uncastedHashChunk(sink, arg);
                            }

                            private void uncastedHashChunk(final Sink sink, final Arg arg) {
                                sink.write(indent + "    result = prime * result + " + arg.name + ";\n");
                            }                            
                            
                            @Override
                            public void _case(ByteType x) {
                                uncastedHashChunk(sink, arg);
                            }

                            @Override
                            public void _case(CharType x) {
                                uncastedHashChunk(sink, arg);
                            }

                            @Override
                            public void _case(ShortType x) {
                                uncastedHashChunk(sink, arg);
                            }

                            @Override
                            public void _default(PrimitiveType x) {
                                sink.write(indent + "    result = prime * result + (int)" + arg.name + ";\n");
                            }});
                    }});
            }
            sink.write(indent + "    return result;\n");
        }
        sink.write(indent + "}");
    }

    

    @Override
	public void emitParameterizedTypeName(Sink sink, List<String> typeArguments) {
		if (!typeArguments.isEmpty()) {
			sink.write("<");
			boolean first = true;
			for (String typeArgument : typeArguments) {
				if (first) {
					first = false;
				} else {
					sink.write(", ");
				}
				sink.write(typeArgument);
			}
			sink.write(">");
		}
		
	}    
	private String constructorArg(Arg arg, boolean withType) {
        return withType ? (ASTPrinter.print(arg.type) + " " + arg.name) : arg.name;
    }    
}
