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

import java.util.List;
import java.util.logging.Logger;

import com.pogofish.jadt.ast.Arg;
import com.pogofish.jadt.ast.ArgModifier;
import com.pogofish.jadt.ast.Constructor;
import com.pogofish.jadt.ast.PrimitiveType;
import com.pogofish.jadt.ast.RefType;
import com.pogofish.jadt.ast.Type;
import com.pogofish.jadt.ast.PrimitiveType.BooleanType;
import com.pogofish.jadt.ast.PrimitiveType.IntType;
import com.pogofish.jadt.ast.RefType.ArrayType;
import com.pogofish.jadt.ast.RefType.ClassType;
import com.pogofish.jadt.ast.Type.Primitive;
import com.pogofish.jadt.ast.Type.Ref;
import com.pogofish.jadt.printer.ASTPrinter;
import com.pogofish.jadt.target.Target;


public class StandardClassBodyEmitter implements ClassBodyEmitter {
	private static final Logger logger = Logger.getLogger(StandardClassBodyEmitter.class.toString());
    
    /* (non-Javadoc)
     * @see com.pogofish.jadt.emitter.ClassBodyEmitter#constructorFactory(com.pogofish.jadt.emitter.Target, java.lang.String, java.lang.String, com.pogofish.jadt.ast.Constructor)
     */
    @Override
    public void constructorFactory(Target target, String dataTypeName, String factoryName, List<String> typeParametrs, Constructor constructor) {
        if (constructor.args.isEmpty()) {
        	if (typeParametrs.isEmpty()) {
            	logger.finest("Generating no args, no types factory for " + constructor.name);
        	} else {
            	logger.finest("Generating no args, type arguments factory for " + constructor.name);
        		target.write("   @SuppressWarnings(\"rawtypes\")\n");
        	}
            target.write("   private static final " + dataTypeName + " _" + factoryName + " = new " + constructor.name + "();\n");
        	if (!typeParametrs.isEmpty()) {
        		target.write("   @SuppressWarnings(\"unchecked\")\n");
        	}
        	target.write("   public static final ");
        	emitParameterizedTypeName(target, typeParametrs);
        	target.write(" ");
        	target.write(dataTypeName);
        	emitParameterizedTypeName(target, typeParametrs);
        	target.write(" _" + factoryName + "() { return _" + factoryName + "; }");
        } else {
        	logger.finest("Generating args factory for " + constructor.name);
            target.write("   public static final ");
            emitParameterizedTypeName(target, typeParametrs);
        	target.write(" ");
        	target.write(dataTypeName);
            emitParameterizedTypeName(target, typeParametrs);
            target.write(" _" + factoryName + "(");
            constructorArgs(target, constructor, true);
            target.write(") { return new " + constructor.name);
            emitParameterizedTypeName(target, typeParametrs);
            target.write("("); 
            constructorArgs(target, constructor, false);
            target.write("); }");            
        }
    }
    
    private void constructorArgs(Target target, Constructor constructor, boolean withTypes) {
        boolean first = true;
        for (Arg arg : constructor.args) {
            if (first) {
                first = false;
            } else {
                target.write(", ");
            }
            target.write(constructorArg(arg, withTypes));
        }
    }
    
    
    /* (non-Javadoc)
     * @see com.pogofish.jadt.emitter.ClassBodyEmitter#emitConstructorMethod(com.pogofish.jadt.emitter.Target, com.pogofish.jadt.ast.Constructor)
     */
    @Override
    public void emitConstructorMethod(Target target, Constructor constructor) {
    	logger.finest("Generating constructor method for " + constructor.name);
        for (Arg arg : constructor.args) {
            final String finalName = arg.modifiers.contains(ArgModifier._Final()) ? "final " : "";
            target.write("      public " + finalName + ASTPrinter.print(arg.type) + " " + arg.name + ";\n");
        }
        target.write("\n      public " + constructor.name + "("); 
        constructorArgs(target, constructor, true);        
        target.write(") {");
        for (Arg arg : constructor.args) {
            target.write("\n         this." + arg.name + " = " + arg.name + ";");
        }
        target.write("\n      }");
    }

    /* (non-Javadoc)
     * @see com.pogofish.jadt.emitter.ClassBodyEmitter#emitToString(com.pogofish.jadt.emitter.Target, com.pogofish.jadt.ast.Constructor)
     */
    @Override
    public void emitToString(Target target, Constructor constructor) {
    	logger.finest("Generating toString() for " + constructor.name);
        target.write("      @Override\n");
        target.write("      public String toString() {\n");
        target.write("         return \"" + constructor.name);
        if (!constructor.args.isEmpty()) {
            target.write("(");
            boolean first = true;
            for (Arg arg : constructor.args) {
                if (first) {
                    first = false;
                } else {
                    target.write(", ");
                }
                target.write(arg.name + " = \" + " + arg.name + " + \"");
            }
            target.write(")");
        }
        target.write("\";\n");
        target.write("      }");
    }

    /* (non-Javadoc)
     * @see com.pogofish.jadt.emitter.ClassBodyEmitter#emitEquals(com.pogofish.jadt.emitter.Target, com.pogofish.jadt.ast.Constructor)
     */
    @Override
    public void emitEquals(final Target target, Constructor constructor, List<String> typeArguments) {
    	logger.finest("Generating equals() for " + constructor.name);
        target.write("      @Override\n");
        target.write("      public boolean equals(Object obj) {\n");
        target.write("         if (this == obj) return true;\n");
        target.write("         if (obj == null) return false;\n");
        target.write("         if (getClass() != obj.getClass()) return false;\n");
        if (!constructor.args.isEmpty()) {
        	if (!typeArguments.isEmpty()) {
        		target.write("         @SuppressWarnings(\"rawtypes\")");
        	}
            target.write("         " + constructor.name + " other = (" + constructor.name + ")obj;\n");
            
            for (final Arg arg : constructor.args) {
                arg.type._switch(new Type.SwitchBlock(){
                    @Override
                    public void _case(Ref x) {
                        x.type._switch(new RefType.SwitchBlock() {
                            @Override
                            public void _case(ClassType x) {
                                target.write("         if (" + arg.name + " == null) {\n");
                                target.write("            if (other." + arg.name + " != null) return false;\n");
                                target.write("         } else if (!" + arg.name + ".equals(other." + arg.name + ")) return false;\n");
                            }

                            @Override
                            public void _case(ArrayType x) {
                                target.write("         if (!java.util.Arrays.equals(" + arg.name + ", other." + arg.name + ")) return false;\n");
                            }});
                    }

                    @Override
                    public void _case(Primitive x) {
                        target.write("         if (" + arg.name + " != other." + arg.name + ") return false;\n");
                    }});
            }
        }
        target.write("         return true;\n");
        target.write("      }");
    }

    /* (non-Javadoc)
     * @see com.pogofish.jadt.emitter.ClassBodyEmitter#emitHashCode(com.pogofish.jadt.emitter.Target, com.pogofish.jadt.ast.Constructor)
     */
    @Override
    public void emitHashCode(final Target target, Constructor constructor) {
    	logger.finest("Generating hashCode() for " + constructor.name);
        target.write("      @Override\n");
        target.write("      public int hashCode() {\n");
        if (constructor.args.isEmpty()) {
            target.write("          return 0;\n");
        } else {
            target.write("          final int prime = 31;\n");
            target.write("          int result = 1;\n");
            for (final Arg arg : constructor.args) {
                arg.type._switch(new Type.SwitchBlock(){
                    @Override
                    public void _case(Ref x) {
                        x.type._switch(new RefType.SwitchBlock() {
                            @Override
                            public void _case(ClassType x) {
                                target.write("          result = prime * result + ((" + arg.name + " == null) ? 0 : " + arg.name + ".hashCode());\n");                
                            }

                            @Override
                            public void _case(ArrayType x) {
                                target.write("          result = prime * result + java.util.Arrays.hashCode(" + arg.name + ");\n");                
                            }});
                    }

                    @Override
                    public void _case(Primitive x) {
                        x.type._switch(new PrimitiveType.SwitchBlockWithDefault() {

                            @Override
                            public void _case(BooleanType x) {
                                target.write("          result = prime * result + (" + arg.name + " ? 1 : 0);\n");
                            }

                            @Override
                            public void _case(IntType x) {
                                target.write("          result = prime * result + " + arg.name + ";\n");
                            }

                            @Override
                            public void _default(PrimitiveType x) {
                                target.write("          result = prime * result + (int)" + arg.name + ";\n");
                            }});
                    }});
            }
            target.write("          return result;\n");
        }
        target.write("      }");
    }

    

    @Override
	public void emitParameterizedTypeName(Target target, List<String> typeArguments) {
		if (!typeArguments.isEmpty()) {
			target.write("<");
			boolean first = true;
			for (String typeArgument : typeArguments) {
				if (first) {
					first = false;
				} else {
					target.write(", ");
				}
				target.write(typeArgument);
			}
			target.write(">");
		}
		
	}    
	private String constructorArg(Arg arg, boolean withType) {
        return withType ? (ASTPrinter.print(arg.type) + " " + arg.name) : arg.name;
    }    
}
