package pogofish.jadt.printer;

import pogofish.jadt.ast.*;


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
        return arg.type + " " + arg.name;
    }
}
