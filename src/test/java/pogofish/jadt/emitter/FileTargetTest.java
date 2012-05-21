package pogofish.jadt.emitter;

import static org.junit.Assert.assertEquals;

import java.io.*;

import org.junit.Test;

import pogofish.jadt.emitter.FileTarget;


public class FileTargetTest {
    @Test
    public void testMissingFile() throws IOException {
        final File temp = File.createTempFile("testFactory", "java");
        temp.delete();
        final FileTarget target = new FileTarget(temp.getAbsolutePath());
        try {
            target.write("hello");
        } finally {
            target.close();
        }
        
        final BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(temp), "UTF-8"));
        final String contents = reader.readLine();
        assertEquals("hello", contents);
    }
    
    @Test
    public void testExistingFile() throws IOException {
        final File temp = File.createTempFile("testFactory", "java");
        temp.createNewFile();
        final FileTarget target = new FileTarget(temp.getAbsolutePath());
        try {
            target.write("hello");
        } finally {
            target.close();
        }
        
        final BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(temp), "UTF-8"));
        final String contents = reader.readLine();
        assertEquals("hello", contents);
    }
    
}
