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


public class Printer {
    public String print(Doc doc) {
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

    public String print(DataType dataType) {
        final StringBuilder builder = new StringBuilder("data " + dataType.name + " =\n    ");
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

    public String print(Constructor constructor) {
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

    public String print(Arg arg) {
        return print(arg.type) + " " + arg.name;
    }
    
    public String print(Type type) {
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
    
    public String print(RefType type) {
        return type.accept(new RefType.Visitor<String>() {

            @Override
            public String visit(ClassType x) {
                final StringBuilder builder = new StringBuilder(x.baseName);
                if (!x.typeArguments.isEmpty()) {
                    builder.append("<");
                    for (RefType typeArgument : x.typeArguments) {
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
    
    public String print(PrimitiveType type) {
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
