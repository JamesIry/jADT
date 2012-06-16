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

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;

import com.pogofish.jadt.util.Util;

/**
 * Factory to create a FileSource given a file name
 *
 * @author jiry
 */
public class FileSourceFactory implements SourceFactory {
    private static final FilenameFilter FILTER = new FilenameFilter() {            
        @Override
        public boolean accept(File dir, String name) {
            return name.endsWith(".jadt");
        }
    };
    
    /**
     * Return sa list of FileSources based on the name.  If the name
     * is a directory then it returns all the files in that directory
     * ending with .jadt.  Otherwise it is assumed that the name
     * is a file.
     */
    @Override
    public List<FileSource> createSources(String srcName) {
        final File dirOrFile = new File(srcName);

        final File[] files = dirOrFile.listFiles(FILTER);
        if (files != null) {
            final List<FileSource> sources = new ArrayList<FileSource>(files.length);
            for (File file : files) {
                sources.add(new FileSource(file));
            }
            return sources;
        } else {
            return Util.list(new FileSource(dirOrFile));
        }
    }

}
