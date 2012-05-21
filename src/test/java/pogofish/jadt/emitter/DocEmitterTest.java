package pogofish.jadt.emitter;

import static org.junit.Assert.assertEquals;
import static pogofish.jadt.util.Util.list;

import java.io.IOException;
import java.util.Map;

import org.junit.Test;

import pogofish.jadt.ast.*;
import pogofish.jadt.emitter.DocEmitter;
import pogofish.jadt.emitter.StandardDocEmitter;
import pogofish.jadt.util.Util;


public class DocEmitterTest {
    private static final String FULL_HEADER =
    "package some.package;\n" +
    "\n" +
    "import wow.man;\n" +
    "import flim.flam;\n" +
    "\n" +
    "/*\n" +
    "This file was generated based on EmitterTest. Please do not modify directly.\n" +
    "\n" +
    "The source was parsed as: \n" +
    "\n" +
    "package some.package\n" +
    "\n" +
    "import wow.man\n" +
    "import flim.flam\n" +
    "\n" +
    "data FooBar =\n" +
    "    Foo(int yeah, String hmmm)\n" +
    "  | Bar\n" +
    "data Whatever =\n" +
    "    Whatever\n" +
    "\n" +
    "*/\n";

    private static final String FOOBAR = 
    "FooBar";
    
    private static final String WHATEVER =
    "Whatever";
    
    @Test
    public void test() throws IOException {
        final Doc doc = new Doc("EmitterTest", "some.package", list("wow.man", "flim.flam"), list(
                new DataType("FooBar", list(
                        new Constructor("Foo", list(
                                new Arg("int", "yeah"),
                                new Arg("String", "hmmm")
                        )),
                        new Constructor("Bar", Util.<Arg>list())
                )),
                new DataType("Whatever", list(
                        new Constructor("Whatever", Util.<Arg>list())
                ))
                
        ));
        final StringTargetFactory factory = new StringTargetFactory();
        final DocEmitter emitter = new StandardDocEmitter(factory, new DummyDataTypeEmitter());
        emitter.emit(doc);
        final Map<String, String> results = factory.getResults();
        assertEquals("Got the wrong number of results", 2, results.size());
        final String foobar = results.get("some.package.FooBar");
        assertEquals(FULL_HEADER+FOOBAR, foobar);
        assertEquals(FULL_HEADER+WHATEVER, results.get("some.package.Whatever"));
    }

}
