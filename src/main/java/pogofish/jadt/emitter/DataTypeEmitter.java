package pogofish.jadt.emitter;

import java.io.IOException;

import pogofish.jadt.ast.DataType;


public interface DataTypeEmitter {

    public abstract void emit(Target target, DataType dataType, String header) throws IOException;

}