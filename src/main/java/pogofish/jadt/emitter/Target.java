package pogofish.jadt.emitter;

import java.io.IOException;

public interface Target {

    public abstract void close() throws IOException;

    public abstract void write(String data) throws IOException;

}
