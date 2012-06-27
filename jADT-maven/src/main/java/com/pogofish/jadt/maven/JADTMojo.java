package com.pogofish.jadt.maven;

import java.io.File;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.project.MavenProject;

import com.pogofish.jadt.JADT;

/**
 * Maven plugin for doing jADT code generation during a Maven build
 * 
 * @phase generate-sources
 * @goal jADT
 */
public class JADTMojo  extends AbstractMojo {
    /**
     * This is the driver that will be executed.  It's made accessible for unit testing
     */
    JADT jadt = JADT.standardConfigDriver();
    
    /**
     * The jadt file or directory that will be parsed
     * 
     * @parameter default-value="src/main/jadt"
     */
    File srcPath = new File("src/main/jadt");
    
    /**
     * The base directory where files will be output
     *
     * @parameter default-value="target/generated-sources/jadt"
     */
    File destDir = new File("target/generated-sources/jadt");    
    
    /**
     * MavenProject supplied by maven at runtime.  It's used to add the destination directory to 
     * maven's sources directory
     * 
     * @parameter expression="${project}"
     * @required
     * @readonly
     * @since 1.0
     */
    MavenProject project = null;    
    
    @Override
    public void execute() throws MojoExecutionException {
        try {
            // tell Maven that the destination dir is a location from which it needs to compile
            project.addCompileSourceRoot( destDir.getCanonicalPath() );            
            
            jadt.parseAndEmit(srcPath.getCanonicalPath(), destDir.getCanonicalPath());
        } catch (Exception e) {
            throw new MojoExecutionException("Error in processing jADT", e);
        }
    }


    public void setSrcPath(File srcPath) {
        this.srcPath = srcPath;
    }


    public void setDestDir(File destDir) {
        this.destDir = destDir;
    }

    public void setProject(MavenProject project) {
        this.project = project;
    }
    
    
}
