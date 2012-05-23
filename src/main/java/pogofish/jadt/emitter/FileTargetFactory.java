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

import java.io.File;

/**
 * Factor that creates FileTargets within a given base directory
 *
 * @author jiry
 */
public class FileTargetFactory implements TargetFactory {
    private final String destDirName;

    /**
     * Creates a FileTargetFactory that will output to the given directory
     * 
     * @param destDirName
     */
    public FileTargetFactory(String destDirName) {
        this.destDirName = destDirName;
    }

    @Override
    public Target createTarget(String className) {
        return new FileTarget(convertToPath(className));
    }

    /**
     * Turns a class name like foo.bar.Baz into the path destDirName/foo/bar/Baz.java
     * 
     * @param className
     * @return
     */
    public String convertToPath(String className) {
        final String fixedDir = destDirName.endsWith(File.separator) ? destDirName: destDirName + File.separator;
        final String fixedClassName = className.replace('.', '/');
        return fixedDir + fixedClassName + ".java";
    }      
}
