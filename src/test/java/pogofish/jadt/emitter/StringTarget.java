package pogofish.jadt.emitter;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Map;

import pogofish.jadt.emitter.Target;

public class StringTarget implements Target {
    private final String name;
    private final StringWriter writer;
    private final Map<String, String> results;
    
    public StringTarget(String name, Map<String, String> results) {
        super();
        this.name = name;
        this.writer = new StringWriter();
        this.results = results;
    }
    
    @Override
    public void write(String data) throws IOException {
        writer.write(data);
    }
    
    @Override
    public void close() throws IOException {
        writer.close();
        results.put(name, writer.toString());
    }

}
