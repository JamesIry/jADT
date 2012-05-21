package pogofish.jadt.emitter;

import java.io.IOException;

import pogofish.jadt.ast.DataType;
import pogofish.jadt.emitter.DataTypeEmitter;
import pogofish.jadt.emitter.Target;


public class DummyDataTypeEmitter implements DataTypeEmitter {

    @Override
    public void emit(Target target, DataType dataType, String header) throws IOException {
        target.write(header + dataType.name);
    }

}
