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
package com.pogofish.jadt.comments;

import static com.pogofish.jadt.util.Util.list;
import static org.junit.Assert.assertEquals;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.pogofish.jadt.ast.JavaComment;
import com.pogofish.jadt.printer.ASTPrinter;

public class CommentProcessorTest {
    @Test
    public void testLeftAlignBlock() {
        testBlockAlign("/* */\n", list("/* */"));
        testBlockAlign("/* */\n/* hello */\n", list("/* */", "/* hello */"));
        testBlockAlign("/* \n */\n", list("/* \n*/"));
        testBlockAlign("/* hello\n * goodbye *\n */\n", list("/* hello\n    * goodbye *\n*/"));
        testBlockAlign("/* hello\n     goodbye *\n */\n", list("/* hello\n     goodbye *\n*/"));
    }

    private void testBlockAlign(String expected, List<String> inputs) {
        final BlockCommentParser parser = new BlockCommentParser();
        final List<JavaComment> outputs = new ArrayList<JavaComment>(inputs.size());
        final CommentProcessor commentProcessor = new CommentProcessor();
        for (String input : inputs) {
            final JavaComment comment = parser.parse(new StringReader(input));
            outputs.add(comment);
        }
        final List<JavaComment> aligned = commentProcessor.leftAlign(outputs);
        final String actual = ASTPrinter.printComments(aligned);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testLeftAlignJavaDoc() {
        testJavaDocAlign("/** */\n", list("/** */"));
        testJavaDocAlign("/** */\n/** hello */\n", list("/** */", "/** hello */"));
        testJavaDocAlign("/** \n */\n", list("/** \n*/"));
        testJavaDocAlign("/** hello\n * goodbye *\n */\n", list("/** hello\n    * goodbye *\n*/"));
        testJavaDocAlign("/** hello\n     goodbye *\n */\n", list("/** hello\n     goodbye *\n*/"));
    }

    private void testJavaDocAlign(String expected, List<String> inputs) {
        final JavaDocParser parser = new JavaDocParser();
        final List<JavaComment> outputs = new ArrayList<JavaComment>(inputs.size());
        final CommentProcessor commentProcessor = new CommentProcessor();
        for (String input : inputs) {
            final JavaComment comment = parser.parse(new StringReader(input));
            outputs.add(comment);
        }
        final List<JavaComment> aligned = commentProcessor.leftAlign(outputs);
        final String actual = ASTPrinter.printComments(aligned);
        assertEquals(expected, actual);
    }
    
}
