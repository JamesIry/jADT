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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;


/**
 * A source based on a File
 *
 * @author jiry
 */
public class FileSource implements Source {
    private final File srcFile;

    public FileSource(File srcFile) {
        this.srcFile = srcFile;
    }
    
    @Override
    public BufferedReader createReader() {
        try {
            return new BufferedReader(new InputStreamReader(new FileInputStream(srcFile), "UTF-8"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String getSrcInfo() {
        return srcFile.getAbsolutePath();
    }
}
