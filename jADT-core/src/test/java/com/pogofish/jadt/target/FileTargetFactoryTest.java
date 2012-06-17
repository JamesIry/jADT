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
package com.pogofish.jadt.target;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;

import org.junit.Test;

import com.pogofish.jadt.target.FileTarget;
import com.pogofish.jadt.target.FileTargetFactory;


/**
 * Test that the FileTargeFacotry 
 *
 * @author jiry
 */
public class FileTargetFactoryTest {
    
    /**
     * Test that the factory does what it's supposed to. That means writing real files to the real file system
     */
    @Test
    public void testCreate() throws IOException {
        final String tempDir = new File(System.getProperty("java.io.tmpdir")).getCanonicalPath();
        final FileTargetFactory factory = new FileTargetFactory(tempDir);
        final FileTarget target = (FileTarget)factory.createTarget("bar.baz.Blah");
        try {
            assertTrue("Output file did not exist", target.outputFile.exists());
            assertEquals(new File(tempDir + "/bar/baz/Blah.java").getCanonicalPath(), target.outputFile.getCanonicalPath());
        } finally {
            target.outputFile.delete();
        }
    }

    /**
     * Make sure that the factory translates base directory without trailing slash properly
     */
    @Test
    public void testFactorySlash() {
        final FileTargetFactory factory = new FileTargetFactory("/germufabits/");
        final String path = factory.convertToPath("hello.world");
        assertEquals("/germufabits/hello/world.java", path);
    }
    
    /**
     * Make sure that the factory translates base directory with trailing slash properly
     */
    @Test
    public void testFactoryNoSlash() {
        final FileTargetFactory factory = new FileTargetFactory("/germufabits");
        final String path = factory.convertToPath("hello.world");
        assertEquals("/germufabits/hello/world.java", path);
    }
}
