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
package com.pogofish.jadt.parser;

import java.io.BufferedReader;
import java.io.IOException;

import com.pogofish.jadt.ast.Doc;
import com.pogofish.jadt.source.Source;


/**
 * A dummy parser that compares its input with a test string and produces a test Doc as its output
 *
 * @author jiry
 */
public class DummyParser implements Parser {
    private final Doc testDoc;
    private final String testSrcInfo;
    private final String testString;

    /**
     * 
     * @param testDoc The document that will be returned by this parser
     * @param testSrcInfo The source info that this parser expects
     * @param testString The string that this parser expects
     */
    public DummyParser(Doc testDoc, String testSrcInfo, String testString) {
        this.testDoc = testDoc;
        this.testSrcInfo = testSrcInfo;
        this.testString = testString;
    }

    /**
     * When called this examines the source to make sure the scrcInfo and data are the expected
     * testSrcInfo and testString.  It then produces the testDoc as a result
     */
    @Override
    public Doc parse(Source source) {
        if (!testSrcInfo.equals(source.getSrcInfo())) {
            throw new RuntimeException("testSrcInfo and source.getSrcInfo were not equal. testSrcInfo = " + testSrcInfo + ", source.getSrcInfo = " + source.getSrcInfo());
        }
        try {
            BufferedReader reader = new BufferedReader(source.getReader());
            try {
                if (!testString.equals(reader.readLine())) {
                    throw new RuntimeException("testString and reader.readLine() were not equal");
                }
                final String secondLine = reader.readLine();
                if (secondLine != null) {
                    throw new RuntimeException("got a second line '" + secondLine + "' from the reader");
                }
            } finally {
                reader.close();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return testDoc;
    }
}