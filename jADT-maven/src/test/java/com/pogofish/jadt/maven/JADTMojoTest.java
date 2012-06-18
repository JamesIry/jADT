package com.pogofish.jadt.maven;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.File;
import java.util.Collections;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.project.MavenProject;
import org.junit.Test;

import com.pogofish.jadt.JADT;
import com.pogofish.jadt.ast.SemanticError;
import com.pogofish.jadt.ast.SyntaxError;
import com.pogofish.jadt.target.StringTargetFactoryFactory;

/**
 * Unit tests for the JADTMojo
 * 
 * @author jiry
 *
 */
public class JADTMojoTest {
    /**
     * Make sure that everything works on the happy path
     */
    @Test
    public void testHappy() throws Exception {
        final File srcFile = new File(JADT.TEST_SRC_INFO);
        final File destDir = new File(JADT.TEST_DIR);
  
        final JADTMojo mojo = new JADTMojo();
        final StringTargetFactoryFactory factory = new StringTargetFactoryFactory();        
        mojo.jadt = JADT.createDummyJADT(Collections.<SyntaxError>emptySet(), Collections.<SemanticError>emptySet(), srcFile.getCanonicalPath(), factory);
        
        mojo.setSrcPath(srcFile);
        mojo.setDestDir(destDir);
        mojo.setProject(new MavenProject());
        mojo.execute();
        
        final String result = factory.results().get(destDir.getCanonicalPath()).get(0).getResults().get(JADT.TEST_CLASS_NAME);
        assertEquals(JADT.TEST_SRC_INFO, result);
        assertEquals(1, mojo.project.getCompileSourceRoots().size());
        assertEquals(destDir.getCanonicalPath(), mojo.project.getCompileSourceRoots().get(0));
    }
    
    /**
     * Make sure that exceptions are handled properly
     */
    @Test
    public void testException() throws Exception {
        final File srcFile = new File(JADT.TEST_SRC_INFO);
        final File destDir = new File(JADT.TEST_DIR);
        
        final JADTMojo mojo = new JADTMojo();
        final StringTargetFactoryFactory factory = new StringTargetFactoryFactory();        
        mojo.jadt = JADT.createDummyJADT(Collections.<SyntaxError>emptySet(), Collections.<SemanticError>singleton(SemanticError._DuplicateConstructor("whatever", "something")), srcFile.getCanonicalPath(), factory);
        
        mojo.setSrcPath(srcFile);
        mojo.setDestDir(destDir);
        mojo.setProject(new MavenProject());
        
        try {
            mojo.execute();

            final String result = factory.results().get(destDir.getCanonicalPath()).get(0).getResults().get(JADT.TEST_CLASS_NAME);
            fail("Did not get exception, got " + result);
        } catch (MojoExecutionException e) {
            // yay
        }
    }
    
    
}
