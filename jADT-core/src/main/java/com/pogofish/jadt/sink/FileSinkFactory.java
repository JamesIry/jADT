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

import java.io.File;

/**
 * Factor that creates FileSinks within a given base directory
 *
 * @author jiry
 */
public class FileSinkFactory implements SinkFactory {
    final String destDirName;

    /**
     * Creates a FileSinkFactory that will output to the given directory
     * 
     * @param destDirName
     */
    public FileSinkFactory(String destDirName) {
        this.destDirName = destDirName;
    }

    @Override
    public Sink createSink(String className) {
        return new FileSink(convertToPath(className));
    }

    /**
     * Turns a class name like foo.bar.Baz into the path destDirName/foo/bar/Baz.java
     * 
     * @param className
     * @return a path name based onthe class and destDirName
     */
    public String convertToPath(String className) {
        final String fixedDir = destDirName.endsWith(File.separator) ? destDirName: destDirName + File.separator;
        final String fixedClassName = className.replace('.', '/');
        return fixedDir + fixedClassName + ".java";
    }      
}
