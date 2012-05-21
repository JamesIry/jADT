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
package pogofish.jadt;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

import java.io.*;

import org.junit.Test;

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
