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
import com.pogofish.jadt.ast.ArgModifier;
import com.pogofish.jadt.ast.Constructor;
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
            sink.write(ASTPrinter.printComments("   ", commentProcessor.leftAlign(commentProcessor.javaDocOnly(constructor.comments))));
            sink.write("   private static final " + dataTypeName + " _" + factoryName + " = new " + constructor.name + "();\n");
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
    public void emitConstructorMethod(Sink sink, Constructor constructor) {
    	logger.finest("Generating constructor method for " + constructor.name);
    	// TODO javadoc for fields
    	for (Arg arg : constructor.args) {
            final String finalName = arg.modifiers.contains(ArgModifier._Final()) ? "final " : "";
            sink.write("      public " + finalName + ASTPrinter.print(arg.type) + " " + arg.name + ";\n");
        }
        sink.write(ASTPrinter.printComments("      ", commentProcessor.leftAlign(commentProcessor.stripTags(CONSTRUCTOR_METHOD_STRIP, commentProcessor.javaDocOnly(constructor.comments)))));    	
        sink.write("\n      public " + constructor.name + "("); 
        constructorArgs(sink, constructor, true);        
        sink.write(") {");
        for (Arg arg : constructor.args) {
            sink.write("\n         this." + arg.name + " = " + arg.name + ";");
        }
        sink.write("\n      }");
    }

    /* (non-Javadoc)
     * @see com.pogofish.jadt.emitter.ClassBodyEmitter#emitToString(com.pogofish.jadt.emitter.Sink, com.pogofish.jadt.ast.Constructor)
     */
    @Override
    public void emitToString(Sink sink, Constructor constructor) {
    	logger.finest("Generating toString() for " + constructor.name);
        sink.write("      @Override\n");
        sink.write("      public String toString() {\n");
        sink.write("         return \"" + constructor.name);
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
        sink.write("      }");
    }

    /* (non-Javadoc)
     * @see com.pogofish.jadt.emitter.ClassBodyEmitter#emitEquals(com.pogofish.jadt.emitter.Sink, com.pogofish.jadt.ast.Constructor)
     */
    @Override
    public void emitEquals(final Sink sink, Constructor constructor, List<String> typeArguments) {
    	logger.finest("Generating equals() for " + constructor.name);
        sink.write("      @Override\n");
        sink.write("      public boolean equals(Object obj) {\n");
        sink.write("         if (this == obj) return true;\n");
        sink.write("         if (obj == null) return false;\n");
        sink.write("         if (getClass() != obj.getClass()) return false;\n");
        if (!constructor.args.isEmpty()) {
        	if (!typeArguments.isEmpty()) {
        		sink.write("         @SuppressWarnings(\"rawtypes\")");
        	}
            sink.write("         " + constructor.name + " other = (" + constructor.name + ")obj;\n");
            
            for (final Arg arg : constructor.args) {
                arg.type._switch(new Type.SwitchBlock(){
                    @Override
                    public void _case(Ref x) {
                        x.type._switch(new RefType.SwitchBlock() {
                            @Override
                            public void _case(ClassType x) {
                                sink.write("         if (" + arg.name + " == null) {\n");
                                sink.write("            if (other." + arg.name + " != null) return false;\n");
                                sink.write("         } else if (!" + arg.name + ".equals(other." + arg.name + ")) return false;\n");
                            }

                            @Override
                            public void _case(ArrayType x) {
                                sink.write("         if (!java.util.Arrays.equals(" + arg.name + ", other." + arg.name + ")) return false;\n");
                            }});
                    }

                    @Override
                    public void _case(Primitive x) {
                        sink.write("         if (" + arg.name + " != other." + arg.name + ") return false;\n");
                    }});
            }
        }
        sink.write("         return true;\n");
        sink.write("      }");
    }

    /* (non-Javadoc)
     * @see com.pogofish.jadt.emitter.ClassBodyEmitter#emitHashCode(com.pogofish.jadt.emitter.Sink, com.pogofish.jadt.ast.Constructor)
     */
    @Override
    public void emitHashCode(final Sink sink, Constructor constructor) {
    	logger.finest("Generating hashCode() for " + constructor.name);
        sink.write("      @Override\n");
        sink.write("      public int hashCode() {\n");
        if (constructor.args.isEmpty()) {
            sink.write("          return 0;\n");
        } else {
            sink.write("          final int prime = 31;\n");
            sink.write("          int result = 1;\n");
            for (final Arg arg : constructor.args) {
                arg.type._switch(new Type.SwitchBlock(){
                    @Override
                    public void _case(Ref x) {
                        x.type._switch(new RefType.SwitchBlock() {
                            @Override
                            public void _case(ClassType x) {
                                sink.write("          result = prime * result + ((" + arg.name + " == null) ? 0 : " + arg.name + ".hashCode());\n");                
                            }

                            @Override
                            public void _case(ArrayType x) {
                                sink.write("          result = prime * result + java.util.Arrays.hashCode(" + arg.name + ");\n");                
                            }});
                    }

                    @Override
                    public void _case(Primitive x) {
                        x.type._switch(new PrimitiveType.SwitchBlockWithDefault() {

                            @Override
                            public void _case(BooleanType x) {
                                sink.write("          result = prime * result + (" + arg.name + " ? 1 : 0);\n");
                            }

                            @Override
                            public void _case(IntType x) {
                                uncastedHashChunk(sink, arg);
                            }

                            private void uncastedHashChunk(final Sink sink, final Arg arg) {
                                sink.write("          result = prime * result + " + arg.name + ";\n");
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
                                sink.write("          result = prime * result + (int)" + arg.name + ";\n");
                            }});
                    }});
            }
            sink.write("          return result;\n");
        }
        sink.write("      }");
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
