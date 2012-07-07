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
package com.pogofish.jadt.javadoc;

import static com.pogofish.jadt.ast.JDTagSection._JDTagSection;
import static com.pogofish.jadt.ast.JDToken._JDAsterisk;
import static com.pogofish.jadt.ast.JDToken._JDEOL;
import static com.pogofish.jadt.ast.JDToken._JDTag;
import static com.pogofish.jadt.ast.JDToken._JDWhiteSpace;
import static com.pogofish.jadt.ast.JDToken._JDWord;
import static com.pogofish.jadt.ast.JavaDoc._JavaDoc;
import static com.pogofish.jadt.util.Util.list;
import static junit.framework.Assert.assertEquals;

import java.io.StringReader;
import java.util.List;

import org.junit.Test;

import com.pogofish.jadt.ast.JDTagSection;
import com.pogofish.jadt.ast.JDToken;
import com.pogofish.jadt.ast.JavaDoc;
import com.pogofish.jadt.printer.ASTPrinter;
import com.pogofish.jadt.util.Util;

/**
 * Test the raw output of the javadoc parser
 * 
 * @author jiry
 */
public class JavaDocParserTest {
    private static final JDToken ONEEOL = _JDEOL("\n");
    private static final JDToken ONEWS = _JDWhiteSpace(" ");
    private static final List<JDToken> NO_TOKENS = Util.<JDToken>list();
    private static final List<JDTagSection> NO_TAG_SECTIONS = Util.<JDTagSection>list();
    
    @Test
    public void testGeneralSection() {
        test("/** */", _JavaDoc("/**", list(ONEWS), NO_TAG_SECTIONS, "*/"));
        test("/*** **/", _JavaDoc("/***", list(ONEWS), NO_TAG_SECTIONS, "**/"));
        test("/** * */", _JavaDoc("/**", list(ONEWS, _JDAsterisk(), ONEWS), NO_TAG_SECTIONS, "*/"));
        test("/** *\n */", _JavaDoc("/**", list(ONEWS, _JDAsterisk(), ONEEOL, ONEWS), NO_TAG_SECTIONS, "*/"));
        test("/**\n * hello\n * world\n */", _JavaDoc("/**", list(ONEEOL, ONEWS, _JDAsterisk(), ONEWS, _JDWord("hello"), ONEEOL, ONEWS, _JDAsterisk(), ONEWS, _JDWord("world"), ONEEOL, ONEWS), NO_TAG_SECTIONS, "*/"));
        test("/**\n * hello @foo */", _JavaDoc("/**", list(ONEEOL, ONEWS, _JDAsterisk(), ONEWS, _JDWord("hello"), ONEWS, _JDTag("@foo"), ONEWS), NO_TAG_SECTIONS, "*/"));
        test("/**\n * hello\n * * @world\n */", _JavaDoc("/**", list(ONEEOL, ONEWS, _JDAsterisk(), ONEWS, _JDWord("hello"), ONEEOL, ONEWS, _JDAsterisk(), ONEWS, _JDAsterisk(), ONEWS, _JDTag("@world"), ONEEOL, ONEWS), NO_TAG_SECTIONS, "*/"));
    }
    
    @Test
    public void testTagSections() {
        test("/**@Foo*/", _JavaDoc("/**", NO_TOKENS, list(_JDTagSection("@Foo", list(_JDTag("@Foo")))), "*/"));        
        test("/**@Foo hello\n * world*/", _JavaDoc("/**", NO_TOKENS, list(_JDTagSection("@Foo", list(_JDTag("@Foo"), ONEWS, _JDWord("hello"), ONEEOL, ONEWS, _JDAsterisk(), ONEWS, _JDWord("world")))), "*/"));        
        test("/**@Foo hello\n * world\n@Bar whatever*/", _JavaDoc("/**", NO_TOKENS, list(_JDTagSection("@Foo", list(_JDTag("@Foo"), ONEWS, _JDWord("hello"), ONEEOL, ONEWS, _JDAsterisk(), ONEWS, _JDWord("world"), ONEEOL)), _JDTagSection("@Bar", list(_JDTag("@Bar"), ONEWS, _JDWord("whatever")))), "*/"));        
    }
    
    @Test
    public void testFull() {
        test("/**\n * hello\n * * @world\n @Foo hello\n * world\n@Bar whatever*/", _JavaDoc("/**", list(ONEEOL, ONEWS, _JDAsterisk(), ONEWS, _JDWord("hello"), ONEEOL, ONEWS, _JDAsterisk(), ONEWS, _JDAsterisk(), ONEWS, _JDTag("@world"), ONEEOL, ONEWS), list(_JDTagSection("@Foo", list(_JDTag("@Foo"), ONEWS, _JDWord("hello"), ONEEOL, ONEWS, _JDAsterisk(), ONEWS, _JDWord("world"), ONEEOL)), _JDTagSection("@Bar", list(_JDTag("@Bar"), ONEWS, _JDWord("whatever")))), "*/"));
    }
    
    @Test
    public void testRoundTrip() {
        testRoundTrip("/** */");
        testRoundTrip("/*** **/");
        testRoundTrip("/** * */");
        testRoundTrip("/** *\n */");
        testRoundTrip("/**\n * hello\n * world\n */");
        testRoundTrip("/**\n * hello @foo */");
        testRoundTrip("/**\n * hello\n * * @world\n */");
        testRoundTrip("/**@Foo*/");        
        testRoundTrip("/**@Foo hello\n * world*/");        
        testRoundTrip("/**@Foo hello\n * world\n@Bar whatever*/");        
    }

    private void testRoundTrip(String string) {
        final JavaDocParser parser = new JavaDocParser();
        assertEquals(string, ASTPrinter.print(parser.parse(new StringReader(string))));
        
    }

    private void test(String string, JavaDoc expected) {
        final JavaDocParser parser = new JavaDocParser();
        assertEquals(expected.toString(), parser.parse(new StringReader(string)).toString());
        
    }
}
