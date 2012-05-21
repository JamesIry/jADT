package pogofish.jadt.emitter;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.junit.Test;

import pogofish.jadt.emitter.FileTargetFactory;


public class FileTargetFactoryTest {
    @Test
    public void testFactorySlash() throws IOException {
        final FileTargetFactory factory = new FileTargetFactory("/germufabits/");
        final String path = factory.convertToPath("hello.world");
        assertEquals("/germufabits/hello/world.java", path);
    }
    
    @Test
    public void testFactoryNoSlash() throws IOException {
        final FileTargetFactory factory = new FileTargetFactory("/germufabits");
        final String path = factory.convertToPath("hello.world");
        assertEquals("/germufabits/hello/world.java", path);
    }
}
