package pogofish.jadt.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.util.*;

import org.junit.Test;

public class UtilTest {
    @Test
    public void constructorTest() {
        // cobertura isn't happy unless the (implicit) constructor is called
        final Util util = new Util();
        assertFalse(util.toString().isEmpty());
    }

    @Test
    public void testList() {
        final List<String> list = new ArrayList<String>();
        list.add("hello");
        list.add("world");
        assertEquals(list, Util.list("hello", "world"));
    }
    
    @Test
    public void testSet() {
        final Set<String> set = new HashSet<String>();
        set.add("hello");
        set.add("world");
        assertEquals(set, Util.set("hello", "world"));
    }
    
    @Test
    public void testMakeString() {
        final List<String> list = new ArrayList<String>();
        assertEquals("", Util.makeString(list, ", "));
        list.add("hello");
        assertEquals("hello", Util.makeString(list, ", "));
        list.add("world");
        assertEquals("hello, world", Util.makeString(list, ", "));
        list.add("goodbye");
        assertEquals("hello, world, goodbye", Util.makeString(list, ", "));
    }
    
}
