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
package pogofish.jadt.ant;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;

import pogofish.jadt.JADT;

/**
 * Task for running JADT during an Ant build
 *
 * @author jiry
 */
public class JADTAntTask extends Task {
    /**
     * This is the driver that will be executed.  It's made accessible for unit testing
     */
    JADT jadt = JADT.standardConfigDriver();
    private String srcFile;
    private String destDir;
    
    /**
     * Set by ant as a property, this is the name of the source file that will be parsed
     */
    public void setSrcFile(String srcFile) {
        this.srcFile = srcFile;
    }
    
    /**
     * Set by ant as a property, this is the name base directory under which all classes will be generated
     */
    public void setDestDir(String destDir) {
        this.destDir = destDir;
    }
    
    @Override
    public void execute() throws BuildException {
        try {
            jadt.parseAndEmit(srcFile, destDir);
        } catch (RuntimeException e) {
            throw new BuildException(e);
        }
    }
}
