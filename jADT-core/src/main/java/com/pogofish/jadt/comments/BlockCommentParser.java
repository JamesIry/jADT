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

import java.io.Reader;

import com.pogofish.jadt.ast.JavaComment;
import com.pogofish.jadt.comments.javacc.generated.BlockCommentParserImpl;
import com.pogofish.jadt.parser.javacc.JavaCCReader;
import com.pogofish.jadt.util.ExceptionAction;
import com.pogofish.jadt.util.Util;

/**
 * Parser for BlockComment.
 * 
 * @author jiry
 */
public class BlockCommentParser {
    public JavaComment parse(final Reader BlockComment) {
        return Util.execute(new ExceptionAction<JavaComment>() {

            @Override
            public JavaComment doAction() throws Throwable {

                BlockCommentParserImpl impl = new BlockCommentParserImpl(
                        new JavaCCReader(BlockComment));
                return impl.blockComment();
            }
        });
    }
}
