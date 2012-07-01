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
import java.nio.CharBuffer;

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
            handleIOException(e);
            return 0;
        }
    }

    @Override
    public void mark(int readAheadLimit) throws IOException {
        try {
            reader.mark(readAheadLimit);
        } catch (IOException e) {
            handleIOException(e);
        }
    }

    @Override
   public boolean markSupported() {
        return reader.markSupported();
    }

    @Override
    public int read() throws IOException {
        try {
            return reader.read();
        } catch (IOException e) {
            handleIOException(e);
            return 0;
        }
    }

    @Override
    public int read(char[] cbuf) throws IOException {
        try {
            return reader.read(cbuf);
        } catch (IOException e) {
            handleIOException(e);
            return 0;
        }
    }

    @Override
    public int read(CharBuffer target) throws IOException {
        try {
            return reader.read(target);
        } catch (IOException e) {
            handleIOException(e);
            return 0;
        }
    }

    @Override
    public boolean ready() throws IOException {
        try {
            return reader.ready();
        } catch (IOException e) {
            handleIOException(e);
            return false;
        }
    }

    @Override
    public void reset() throws IOException {
        try {
            reader.reset();
        } catch (IOException e) {
            handleIOException(e);
        }
    }

    @Override
    public long skip(long n) throws IOException {
        try {
            return reader.skip(n);
        } catch (IOException e) {
            handleIOException(e);
            return 0;
        }
    }

    /**
     * If the stream hasn't been closed then this is a "real" IOException which we'll
     * wrap as a RuntimException so that JavaCC doesn't squelch it.  If the stream has
     * been closed then pass the IOException on as is
     */
    private void handleIOException(IOException e) throws IOException {
        if (closed) {
            throw e;
        } else {
            throw new RuntimeException(e);
        }
    }
}
