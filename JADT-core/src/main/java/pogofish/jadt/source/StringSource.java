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
package pogofish.jadt.source;

import java.io.*;

import pogofish.jadt.util.IOExceptionAction;

/**
 * A Source that reads from a supplied string, mostly useful for testing
 *
 * @author jiry
 */
public class StringSource implements Source {
    /**
     * Internal reader used to read from the string
     */
    private final Reader reader;
    /**
     * Information about the source
     */
    private final String srcInfo;

    
    public StringSource(String srcInfo, String source) {
        this.srcInfo = srcInfo;
        this.reader = new StringReader(source);
    }

    @Override
    public Reader getReader() {
        return reader;
    }

    @Override
    public String getSrcInfo() {
        return srcInfo;
    }
    
    @Override
    public void close() {
        new IOExceptionAction<Void>() {
            @Override
            public Void doAction() throws IOException {
                reader.close();
                return null;
            }}.execute();        
    }
}
