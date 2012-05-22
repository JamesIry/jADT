package pogofish.jadt.printer;

import pogofish.jadt.ast.*;

public interface Printer {

    public abstract String print(PrimitiveType type);

    public abstract String print(RefType type);

    public abstract String print(Type type);

    public abstract String print(Arg arg);

    public abstract String print(Constructor constructor);

    public abstract String print(DataType dataType);

    public abstract String print(Doc doc);

}