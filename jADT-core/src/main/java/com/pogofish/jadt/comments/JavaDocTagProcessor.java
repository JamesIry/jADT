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

import static com.pogofish.jadt.ast.JavaComment._JavaDocComment;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.pogofish.jadt.ast.JDTagSection;
import com.pogofish.jadt.ast.JavaComment;
import com.pogofish.jadt.ast.JavaComment.JavaBlockComment;
import com.pogofish.jadt.ast.JavaComment.JavaDocComment;
import com.pogofish.jadt.ast.JavaComment.JavaEOLComment;

/**
 * Methods for processing a parsed javadoc
 * 
 * @author jiry
 */
public class JavaDocTagProcessor {
    /**
     * Produce a copy of the original JavaDoc with specified tags removed
     * Other kinds of comment are left untouched
     * 
     * @param tagNames Set of tag names (including leading @) to be removed
     * @param original Original JavaDoc to be processed
     * @return a new JavaDoc with the specified tags removed
     */
    public JavaComment stripTags(final Set<String> tagNames, JavaComment original) {
        return original.match(new JavaComment.MatchBlock<JavaComment>() {

            @Override
            public JavaComment _case(JavaDocComment x) {
                final List<JDTagSection> newTagSections = new ArrayList<JDTagSection>(x.tagSections.size());
                for (JDTagSection tagSection : x.tagSections) {
                    if (!tagNames.contains(tagSection.name)) {
                        newTagSections.add(tagSection);
                    }
                }
                return _JavaDocComment(x.start, x.generalSection, newTagSections, x.end);
            }

            @Override
            public JavaComment _case(JavaBlockComment x) {
                return x;
            }

            @Override
            public JavaComment _case(JavaEOLComment x) {
                return x;
            }
        });

    }
}
