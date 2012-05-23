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

import java.io.IOException;
import java.io.StringWriter;

public class StringTarget implements Target {
    private final StringWriter writer;
    private boolean closed = false;
    
    public StringTarget() {
        super();
        this.writer = new StringWriter();
    }
    
    @Override
    public void write(String data) {
        writer.write(data);
    }
    
    @Override
    public void close() {
        try {
            writer.close();
            closed = true;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    
    public String result() {
        if (!closed) {
            throw new RuntimeException("target was not closed");
        }
        return writer.toString();
    }

}
