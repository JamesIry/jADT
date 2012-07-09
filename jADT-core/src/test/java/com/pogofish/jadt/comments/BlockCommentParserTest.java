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

import static com.pogofish.jadt.ast.BlockToken._BlockEOL;
import static com.pogofish.jadt.ast.BlockToken._BlockWhiteSpace;
import static com.pogofish.jadt.ast.BlockToken._BlockWord;
import static com.pogofish.jadt.ast.JavaComment._JavaBlockComment;
import static com.pogofish.jadt.util.Util.list;
import static junit.framework.Assert.assertEquals;

import java.io.StringReader;

import org.junit.Test;

import com.pogofish.jadt.ast.BlockToken;
import com.pogofish.jadt.ast.JavaComment;

public class BlockCommentParserTest {
    private static final BlockToken EOL = _BlockEOL("\n");
    private static final BlockToken START = _BlockWord("/*");
    private static final BlockToken END = _BlockWord("*/");
    private static final BlockToken ONEWS = _BlockWhiteSpace(" ");

    @SuppressWarnings("unchecked")
    @Test
    public void test() {
        test("/* */", _JavaBlockComment(list(list(START, ONEWS, END))));
        test("/* hello */", _JavaBlockComment(list(list(START, ONEWS, _BlockWord("hello"), ONEWS, END))));
        test("/* hello\n * goodbye */", _JavaBlockComment(list(list(START, ONEWS, _BlockWord("hello"), EOL), list(ONEWS, _BlockWord("*"), ONEWS, _BlockWord("goodbye"), ONEWS, END))));
    }

    private void test(String string, JavaComment expected) {
        final BlockCommentParser parser = new BlockCommentParser();
        assertEquals(expected.toString(), parser.parse(new StringReader(string)).toString());       
    }
}
