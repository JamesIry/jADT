package pogofish.jadt.printer;

import pogofish.jadt.ast.*;
import pogofish.jadt.ast.PrimitiveType.BooleanType;
import pogofish.jadt.ast.PrimitiveType.CharType;
import pogofish.jadt.ast.PrimitiveType.DoubleType;
import pogofish.jadt.ast.PrimitiveType.FloatType;
import pogofish.jadt.ast.PrimitiveType.IntType;
import pogofish.jadt.ast.PrimitiveType.LongType;
import pogofish.jadt.ast.PrimitiveType.ShortType;
import pogofish.jadt.ast.RefType.ArrayType;
import pogofish.jadt.ast.RefType.ClassType;
import pogofish.jadt.ast.Type.Primitive;
import pogofish.jadt.ast.Type.Ref;

/**
 * Pretty printer for the JADT AST.  Note that in the context of pretty printers "print" just means "make a nice looking string"
 *
 * @author jiry
 */
public class Printer  {
    /**
     * ( "package" packageName )?
     * (
     * ( "import" import \n)*
     * )?
     * dataType
     * (dataType\n)*
     * 
     * @param doc Document to be printed
     * @return pretty string
     */
    public static String print(Doc doc) {
        final StringBuilder builder = new StringBuilder(doc.pkg.isEmpty() ? "" : ("package " + doc.pkg + "\n\n"));
        if (!doc.imports.isEmpty()) {
            for (String imp : doc.imports) {
                builder.append("import " + imp + "\n");
            }
            builder.append("\n");
        }
        for (DataType dataType : doc.dataTypes) {
            builder.append(print(dataType));
            builder.append("\n");
        }
        return builder.toString();  
        
    }

    /**
     * Prints a dataType as
     * dataType = constructor
     * (\n  | constructor)*
     * 
     * @param dataType DataType to be printed
     * @return pretty string
     */
    public static String print(DataType dataType) {
        final StringBuilder builder = new StringBuilder(dataType.name + " =\n    ");
        boolean first = true;
        for (Constructor constructor : dataType.constructors) {
            if (first) {
                first = false;
            } else {
                builder.append("\n  | ");
            }
            builder.append(print(constructor));
        }
        return builder.toString();
    }

    /**
     * Prints a constructor as
     * name("("arg(", "arg)*")")?
     * 
     * @param constructor
     * @return
     */
    public static String print(Constructor constructor) {
        final StringBuilder builder = new StringBuilder(constructor.name);
        if (! constructor.args.isEmpty()) {
            builder.append("(");
            boolean first = true;
            for (Arg arg : constructor.args) {
                if (first) {
                    first = false;
                } else {
                    builder.append(", ");
                }
                builder.append(print(arg));
            }
            builder.append(")");
        }
        return builder.toString();
    }

    /**
     * prints an arg as
     * type name
     * 
     * @param arg Arg to be printed
     * @return pretty string
     */
    public static String print(Arg arg) {
        return print(arg.type) + " " + arg.name;
    }
    
    /**
     * Prints a type as either a ref type or a primitive
     * 
     * @param type Type to be printed
     * @return pretty string
     */
    public static String print(Type type) {
        return type.accept(new Type.Visitor<String>(){
            @Override
            public String visit(Ref x) {
                return print(x.type);
            }

            @Override
            public String visit(Primitive x) {
                return print(x.type);
            }
            
        });
    }
    
    /**
     * Prints a type as either an array or class type. 
     * 
     * For a an array, it prints as
     * type[]
     * 
     * For a class it prints as
     * className("<" refType(", " refType)*">")?
     * 
     * @param type RefType to be printed
     * @return pretty string
     */
    public static String print(RefType type) {
        return type.accept(new RefType.Visitor<String>() {

            @Override
            public String visit(ClassType x) {
                final StringBuilder builder = new StringBuilder(x.baseName);
                if (!x.typeArguments.isEmpty()) {
                    builder.append("<");
                    boolean first = true;
                    for (RefType typeArgument : x.typeArguments) {
                        if (first) {
                            first = false;
                        } else {
                            builder.append(", ");
                        }
                        builder.append(print(typeArgument));
                    }
                    builder.append(">");
                }
                return builder.toString();
            }

            @Override
            public String visit(ArrayType x) {
                return print(x.heldType) + "[]";
            }});
    }
    
    /**
     * Prints a primitive type as "boolean" | "char" | "short" | "int" | "long" | "float" | "double"
     * @param type PrimitiveType to be printed
     * @return pretty string
     */
    public static String print(PrimitiveType type) {
        return type.accept(new PrimitiveType.Visitor<String>() {

            @Override
            public String visit(BooleanType x) {
                return "boolean";
            }

            @Override
            public String visit(CharType x) {
                return "char";
            }

            @Override
            public String visit(DoubleType x) {
                return "double";
            }

            @Override
            public String visit(FloatType x) {
                return "float";
            }

            @Override
            public String visit(IntType x) {
                return "int";
            }

            @Override
            public String visit(LongType x) {
                return "long";
            }

            @Override
            public String visit(ShortType x) {
                return "short";
            }});
    }

}
