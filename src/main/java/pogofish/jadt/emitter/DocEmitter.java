package pogofish.jadt.emitter;

import java.io.IOException;

import pogofish.jadt.ast.Doc;


public interface DocEmitter {

    public abstract void emit(Doc doc) throws IOException;

}