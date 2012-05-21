/*
Copyright 2012 James Iry

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/
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
