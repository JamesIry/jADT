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
package com.pogofish.jadt.source;

import java.io.*;

import com.pogofish.jadt.util.IOExceptionAction;


/**
 * A source based on a File
 *
 * @author jiry
 */
public class FileSource implements Source {
    private final String srcInfo;
    private final Reader reader;        

    public FileSource(String srcFileName) {
        try {
            final File srcFile = new File(srcFileName);
            srcInfo = srcFile.getAbsolutePath();
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(srcFile), "UTF-8"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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
