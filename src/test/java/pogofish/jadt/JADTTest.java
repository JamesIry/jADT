package pogofish.jadt;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

import java.io.*;

import org.junit.Test;

import pogofish.jadt.JADT;
import pogofish.jadt.ast.DataType;
import pogofish.jadt.ast.Doc;
import pogofish.jadt.emitter.DocEmitter;
import pogofish.jadt.parser.Parser;
import pogofish.jadt.util.Util;


public class JADTTest {
    
    @Test
    public void test() throws IOException {
        final StringReader reader = new StringReader("hello");
        try {
            final StringWriter writer = new StringWriter();
            try {
                final Doc doc = new Doc("srcInfo", "pkg", Util.<String>list(), Util.<DataType>list());
                final JADT adt = new JADT(new Parser(){
                    @Override
                    public Doc parse(String srcInfo, Reader reader) throws IOException {
                        assertEquals(srcInfo, srcInfo);                        
                        assertEquals("hello", new BufferedReader(reader).readLine());
                        return doc;
                    }}, new DocEmitter(){
        
                    @Override
                    public void emit(Doc arg) throws IOException {
                        assertSame(doc, arg);
                        writer.write("all good!");
                    }});
                adt.parseAndEmit("srcInfo", reader);
            } finally {
                writer.close();
            }
            assertEquals("all good!", writer.toString());
        } finally {
            reader.close();
        }
    }
}
