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
package com.pogofish.jadt.parser.javacc;

import java.io.IOException;
import java.io.Reader;

/**
 * JavaCC's generated lexer likes to swallow IOException, so this turns them into RuntimeException.  Except
 * JavaCC also deliberately tries to read after a Reader has been closed and counts on that IOException 
 * so we can't wrap IOException after a close. 
 * 
 * @author jiry
 */
class JavaCCReader extends Reader {
    private final Reader reader;
    
    /**
     * If the reader has been closed we'll just pass all IOExceptions as is
     */
    private boolean closed = false;

    public JavaCCReader(Reader reader) {
        super();
        this.reader = reader;
    }

    @Override
    public void close() throws IOException {
        reader.close();
        closed = true;
    }

    @Override
    public int read(char[] cbuf, int off, int len) throws IOException {
        try {
            return reader.read(cbuf, off, len);
        } catch (IOException e) {
            return handleIOException(e);
        }
    }

    /**
     * If the stream hasn't been closed then this is a "real" IOException which we'll
     * wrap as a RuntimException so that JavaCC doesn't squelch it.  If the stream has
     * been closed then pass the IOException on as is
     */
    private int handleIOException(IOException e) throws IOException {
        if (closed) {
            throw e;
        } else {
            throw new RuntimeException(e);
        }
    }
}
