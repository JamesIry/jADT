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
package com.pogofish.jadt.sink;

import java.io.IOException;
import java.io.StringWriter;

import com.pogofish.jadt.util.ExceptionAction;
import com.pogofish.jadt.util.Util;


/**
 * Sink that creates a String, mostly useful for testing
 *
 * @author jiry
 */
public class StringSink implements Sink {
    private final StringWriter writer;
    private boolean closed = false;
    private final String name;
    
    @Override
	public String getInfo() {
		return name;
	}

	public StringSink(final String name) {
        super();
        this.writer = new StringWriter();
        this.name = name;
    }
    
    @Override
    public void write(String data) {
        writer.write(data);
    }
    
    @Override
    public void close() {
        Util.execute(new ExceptionAction<Void>() {

            @Override
            public Void doAction() throws IOException {
                writer.close();
                return null;
            }});
        closed = true;
    }
    
    /**
     * The data written to this sink, or an exception if it wasn't closed
     */
    public String result() {
        if (!closed) {
            throw new RuntimeException("sink was not closed");
        }
        return writer.toString();
    }

}
