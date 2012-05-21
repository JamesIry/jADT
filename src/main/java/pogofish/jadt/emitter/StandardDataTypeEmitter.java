package pogofish.jadt.emitter;

import java.io.IOException;
import java.util.Set;

import pogofish.jadt.ast.*;
import pogofish.jadt.util.Util;


public class StandardDataTypeEmitter implements DataTypeEmitter {
    private static final Set<String> PRIMITIVES = Util.set("boolean", "short", "char", "int", "long", "float", "double");
    
    /* (non-Javadoc)
     * @see sfdc.adt.emitter.DataTypeEmitter#emit(sfdc.adt.emitter.Target, sfdc.adt.ast.DataType, java.lang.String)
     */
    @Override
    public void emit(Target target, DataType dataType, String header) throws IOException {
        target.write(header);
        target.write("public abstract class " + dataType.name + " {\n\n");
        target.write("   private " + dataType.name + "() {\n");
        target.write("   }\n");
        
        for(Constructor constructor : dataType.constructors) {
            target.write("\n");
            constructorFactory(target, dataType.name, constructor);
        }
        target.write("\n\n");
        
        target.write("   public static interface Visitor<A> {\n");
        for(Constructor constructor : dataType.constructors) {
            target.write("      A visit(" + constructor.name + " x);");
            target.write("\n");
        }
        target.write("   }\n\n");
        
        target.write("   public static abstract class VisitorWithDefault<A> implements Visitor<A> {\n");
        for(Constructor constructor : dataType.constructors) {
            target.write("      @Override\n");
            target.write("      public A visit(" + constructor.name + " x) { return getDefault(x); }\n\n");
        }
        target.write("      public abstract A getDefault(" + dataType.name + " x);\n");
        target.write("   }");
        
        for(Constructor constructor : dataType.constructors) {
            target.write("\n\n");
            constructorDeclaration(target, constructor, dataType.name);
        }
        target.write("\n\n   public abstract <A> A accept(Visitor<A> visitor);\n\n");
        target.write("}");
    }
    
    private static void constructorFactory(Target target, String dataTypeName, Constructor constructor) throws IOException {
        if (constructor.args.isEmpty()) {
            target.write("   public static " + dataTypeName + " " + constructor.name + " = new " + constructor.name + "();");
        } else {
            target.write("   public static final " + dataTypeName + " " + constructor.name + "(");
            constructorArgs(target, constructor, true);
            target.write(") { return new " + constructor.name + "("); 
            constructorArgs(target, constructor, false);
            target.write("); }");            
        }
    }
    
    private static void constructorArgs(Target target, Constructor constructor, boolean withTypes) throws IOException {
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
    

    private static String constructorArg(Arg arg, boolean withType) {
        return withType ? (arg.type + " " + arg.name) : arg.name;
    }

    private static void constructorDeclaration(Target target, Constructor constructor, String dataTypeName) throws IOException {
        target.write("   public static final class " + constructor.name + " extends " + dataTypeName + " {\n");
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
        target.write("\n      }\n\n");
        target.write("      @Override\n");
        target.write("      public <A> A accept(Visitor<A> visitor) { return visitor.visit(this); }\n\n");
        
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
        target.write("      }\n\n");
        
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
        target.write("      }\n\n");
        
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
        target.write("      }\n\n");
        
        target.write("   }");
    }
    
    private static boolean isPrimitive(String type) {
        return PRIMITIVES.contains(type);
    }


}
