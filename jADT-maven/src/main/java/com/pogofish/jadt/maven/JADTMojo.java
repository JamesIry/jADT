package com.pogofish.jadt.maven;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;

/**
 * Says "Hi" to the user.
 * @goal sayhi
 */
public class JADTMojo  extends AbstractMojo {
    public void execute() throws MojoExecutionException {
        getLog().info( "Hello, world." );
    }
}
