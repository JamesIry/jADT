package pogofish.jadt.emitter;

import java.io.*;

public class FileTarget implements Target {
    private final Writer writer;
    final File outputFile;
        
    public FileTarget(String outputFileName) throws IOException {
        super();
        
        outputFile = new File(outputFileName);
        final File parentDir = outputFile.getParentFile();
        if (parentDir != null && !parentDir.exists()) {
            parentDir.mkdirs();
        }
        
        if (outputFile.exists()) {
            outputFile.delete();
        }
        
        outputFile.createNewFile();
        
        writer = new OutputStreamWriter(new FileOutputStream(outputFile), "UTF-8");
    }
    
    @Override
    public void write(String data) throws IOException {
        writer.write(data);
    }
    
    @Override
    public void close() throws IOException {
        writer.close();
    }
}
