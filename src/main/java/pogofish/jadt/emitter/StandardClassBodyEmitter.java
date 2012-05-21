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
package pogofish.jadt.emitter;

import static pogofish.jadt.util.Util.set;

import java.io.IOException;
import java.util.Set;

import pogofish.jadt.ast.Arg;
import pogofish.jadt.ast.Constructor;

public class StandardClassBodyEmitter implements ClassBodyEmitter {
    private static final Set<String> PRIMITIVES = set("boolean", "short", "char", "int", "long", "float", "double");

    /* (non-Javadoc)
     * @see pogofish.jadt.emitter.ClassBodyEmitter#constructorFactory(pogofish.jadt.emitter.Target, java.lang.String, java.lang.String, pogofish.jadt.ast.Constructor)
     */
    @Override
    public void constructorFactory(Target target, String dataTypeName, String factoryName, Constructor constructor) throws IOException {
        if (constructor.args.isEmpty()) {
            target.write("   public static " + dataTypeName + " _" + factoryName + " = new " + constructor.name + "();");
        } else {
            target.write("   public static final " + dataTypeName + " _" + factoryName + "(");
            constructorArgs(target, constructor, true);
            target.write(") { return new " + constructor.name + "("); 
            constructorArgs(target, constructor, false);
            target.write("); }");            
        }
    }
    
    private void constructorArgs(Target target, Constructor constructor, boolean withTypes) throws IOException {
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
     * @see pogofish.jadt.emitter.ClassBodyEmitter#emitConstructorMethod(pogofish.jadt.emitter.Target, pogofish.jadt.ast.Constructor)
     */
    @Override
    public void emitConstructorMethod(Target target, Constructor constructor) throws IOException {
        for (Arg arg : constructor.args) {
            target.write("      public final " + arg.type + " " + arg.name + ";");
            target.write("\n");
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
     * @see pogofish.jadt.emitter.ClassBodyEmitter#emitToString(pogofish.jadt.emitter.Target, pogofish.jadt.ast.Constructor)
     */
    @Override
    public void emitToString(Target target, Constructor constructor) throws IOException {
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
     * @see pogofish.jadt.emitter.ClassBodyEmitter#emitEquals(pogofish.jadt.emitter.Target, pogofish.jadt.ast.Constructor)
     */
    @Override
    public void emitEquals(Target target, Constructor constructor) throws IOException {
        target.write("      @Override\n");
        target.write("      public boolean equals(Object obj) {\n");
        target.write("         if (this == obj) return true;\n");
        target.write("         if (obj == null) return false;\n");
        target.write("         if (getClass() != obj.getClass()) return false;\n");
        if (!constructor.args.isEmpty()) {
            target.write("         " + constructor.name + " other = (" + constructor.name + ")obj;\n");
            
            for (Arg arg : constructor.args) {
                if (isPrimitive(arg.type)) {
                    target.write("         if (" + arg.name + " != other." + arg.name + ") return false;\n");                
                } else {
                    target.write("         if (" + arg.name + " == null) {\n");
                    target.write("            if (other." + arg.name + " != null) return false;\n");
                    target.write("         } else if (!" + arg.name + ".equals(other." + arg.name + ")) return false;\n");
                }
            }
        }
        target.write("         return true;\n");
        target.write("      }");
    }

    /* (non-Javadoc)
     * @see pogofish.jadt.emitter.ClassBodyEmitter#emitHashCode(pogofish.jadt.emitter.Target, pogofish.jadt.ast.Constructor)
     */
    @Override
    public void emitHashCode(Target target, Constructor constructor) throws IOException {
        target.write("      @Override\n");
        target.write("      public int hashCode() {\n");
        if (constructor.args.isEmpty()) {
            target.write("          return 0;\n");
        } else {
            target.write("          final int prime = 31;\n");
            target.write("          int result = 1;\n");
            for (Arg arg : constructor.args) {
                if (isPrimitive(arg.type)) {
                    if (arg.type.equals("int")) {
                        target.write("          result = prime * result + " + arg.name + ";\n");
                    } else {
                        target.write("          result = prime * result + (int)" + arg.name + ";\n");                    
                    }
                } else {
                    target.write("          result = prime * result + ((" + arg.name + " == null) ? 0 : " + arg.name + ".hashCode());\n");                
                }
            }
            target.write("          return result;\n");
        }
        target.write("      }");
    }
     
    
    private static String constructorArg(Arg arg, boolean withType) {
        return withType ? (arg.type + " " + arg.name) : arg.name;
    }    
    
    
    private static boolean isPrimitive(String type) {
        return PRIMITIVES.contains(type);
    }    

}
