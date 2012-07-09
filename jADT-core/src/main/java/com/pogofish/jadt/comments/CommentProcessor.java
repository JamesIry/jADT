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

import static com.pogofish.jadt.ast.JDTagSection._JDTagSection;
import static com.pogofish.jadt.ast.JDToken._JDWhiteSpace;
import static com.pogofish.jadt.ast.JavaComment.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.pogofish.jadt.ast.BlockToken;
import com.pogofish.jadt.ast.JDTagSection;
import com.pogofish.jadt.ast.JDToken;
import com.pogofish.jadt.ast.BlockToken.BlockEOL;
import com.pogofish.jadt.ast.BlockToken.BlockWhiteSpace;
import com.pogofish.jadt.ast.BlockToken.BlockWord;
import com.pogofish.jadt.ast.JDToken.JDAsterisk;
import com.pogofish.jadt.ast.JDToken.JDEOL;
import com.pogofish.jadt.ast.JDToken.JDWhiteSpace;
import com.pogofish.jadt.ast.JavaComment;
import com.pogofish.jadt.ast.JavaComment.JavaBlockComment;
import com.pogofish.jadt.ast.JavaComment.JavaDocComment;
import com.pogofish.jadt.ast.JavaComment.JavaEOLComment;
import static com.pogofish.jadt.ast.BlockToken.*;

/**
 * Methods for processing a parsed javadoc
 * 
 * @author jiry
 */
public class CommentProcessor {
    /**
     * Produce a copy of a list of comments where all javadoc comments have had
     * the specified tags removed. Non javadoc comments are left alone.
     * 
     * @param tagNames
     *            Set of tag names (including leading @) to be removed
     * @param originals
     *            List of Comments to be processed
     * @return a new list of comments with the specified tags removed
     */
    public List<JavaComment> stripTags(final Set<String> tagNames,
            List<JavaComment> originals) {
        final List<JavaComment> results = new ArrayList<JavaComment>(
                originals.size());
        for (JavaComment original : originals) {
            results.add(original
                    .match(new JavaComment.MatchBlock<JavaComment>() {

                        @Override
                        public JavaComment _case(JavaDocComment x) {
                            final List<JDTagSection> newTagSections = new ArrayList<JDTagSection>(
                                    x.tagSections.size());
                            for (JDTagSection tagSection : x.tagSections) {
                                if (!tagNames.contains(tagSection.name)) {
                                    newTagSections.add(tagSection);
                                }
                            }
                            return _JavaDocComment(x.start, x.generalSection,
                                    newTagSections, x.end);
                        }

                        @Override
                        public JavaComment _case(JavaBlockComment x) {
                            return x;
                        }

                        @Override
                        public JavaComment _case(JavaEOLComment x) {
                            return x;
                        }
                    }));
        }
        return results;
    }

    public List<JavaComment> leftAlign(List<JavaComment> originals) {
        final List<JavaComment> results = new ArrayList<JavaComment>(
                originals.size());
        for (JavaComment original : originals) {
            results.add(original
                    .match(new JavaComment.MatchBlock<JavaComment>() {

                        @Override
                        public JavaComment _case(JavaDocComment x) {
                            return _JavaDocComment(x.start, leftAlignSection(x.generalSection, x.tagSections.isEmpty()), leftAlignSections(x.tagSections), x.end);
                        }


                        @Override
                        public JavaComment _case(JavaBlockComment x) {
                            return _JavaBlockComment(leftAlignBlock(x.lines));
                        }

                        @Override
                        public JavaComment _case(JavaEOLComment x) {
                            return x;
                        }
                    }));
        }
        return results;        
    }
    

    private List<JDTagSection> leftAlignSections(
            List<JDTagSection> tagSections) {
        final int size = tagSections.size();
        final List<JDTagSection> newSections = new ArrayList<JDTagSection>(size);
        int count = 0;
        for (JDTagSection section : tagSections) {
            count ++;
            newSections.add(_JDTagSection(section.name, leftAlignSection(section.tokens, count == size)));
        }
        return newSections;
    }    

    private List<JDToken> leftAlignSection(List<JDToken> originalSection, boolean lastSection) {
        final List<JDToken> result = new ArrayList<JDToken>(
                originalSection.size() + 1);
        final List<JDToken> leadingWhiteSpace = new ArrayList<JDToken>(1);
        final LeftAlignState state[] = new LeftAlignState[] { LeftAlignState.IN_LINE };

        for (JDToken token : originalSection) {
            token._switch(new JDToken.SwitchBlockWithDefault() {
                @Override
                public void _case(JDWhiteSpace x) {
                    switch (state[0]) {
                    case START_LINE:
                        leadingWhiteSpace.add(x);
                        break;
                    case IN_LINE:
                        result.add(x);
                        break;
                    }
                }

                @Override
                public void _case(JDEOL x) {
                    switch (state[0]) {
                    case START_LINE:
                        result.addAll(leadingWhiteSpace);
                        leadingWhiteSpace.clear();
                        result.add(x);
                        break;
                    case IN_LINE:
                        result.add(x);
                        break;
                    }
                    state[0] = LeftAlignState.START_LINE;
                }

                @Override
                public void _case(JDAsterisk x) {
                    switch (state[0]) {
                    case START_LINE:
                        result.add(_JDWhiteSpace(" "));
                        leadingWhiteSpace.clear();
                        result.add(x);
                        state[0] = LeftAlignState.IN_LINE;
                        break;
                    case IN_LINE:
                        result.add(x);
                        break;
                    }
                }

                @Override
                protected void _default(JDToken x) {
                    switch (state[0]) {
                    case START_LINE:
                        result.addAll(leadingWhiteSpace);
                        leadingWhiteSpace.clear();
                        result.add(x);
                        state[0] = LeftAlignState.IN_LINE;
                        break;
                    case IN_LINE:
                        result.add(x);
                        break;
                    }
                }
            });
        }
        
        if (lastSection) {
            switch (state[0]) {
            case START_LINE:
                result.add(_JDWhiteSpace(" "));
                break;
            case IN_LINE:
                break;
            }            
        }

        return result;
    }

    private static enum LeftAlignState {
        START_LINE, IN_LINE;
    }
    

    private List<List<BlockToken>> leftAlignBlock(
            List<List<BlockToken>> lines) {
        final List<List<BlockToken>> results = new ArrayList<List<BlockToken>>(lines.size());
        for (List<BlockToken> line : lines) {
            results.add(leftAlignLign(line));
        }
        return results;
    }

    private List<BlockToken> leftAlignLign(List<BlockToken> line) {
        final List<BlockToken> result = new ArrayList<BlockToken>(line.size() + 1);
        final List<BlockToken> leadingWhiteSpace = new ArrayList<BlockToken>(1);
        final LeftAlignState state[] = new LeftAlignState[] { LeftAlignState.START_LINE };
        
        for(BlockToken token: line) {
            token._switch(new BlockToken.SwitchBlock() {
                
                @Override
                public void _case(BlockEOL x) {
                    switch (state[0]) {
                    case START_LINE:
                        result.addAll(leadingWhiteSpace);
                        leadingWhiteSpace.clear();
                        result.add(x);
                        break;
                    case IN_LINE:
                        result.add(x);
                        break;
                    }
                    state[0] = LeftAlignState.START_LINE;
                }
                
                @Override
                public void _case(BlockWhiteSpace x) {
                    switch (state[0]) {
                    case START_LINE:
                        leadingWhiteSpace.add(x);
                        break;
                    case IN_LINE:
                        result.add(x);
                        break;
                    }
                }
                
                @Override
                public void _case(BlockWord x) {
                    if (x.word.startsWith("*")) {
                        switch (state[0]) {
                        case START_LINE:
                            result.add(_BlockWhiteSpace(" "));
                            leadingWhiteSpace.clear();
                            result.add(x);
                            state[0] = LeftAlignState.IN_LINE;
                            break;
                        case IN_LINE:
                            result.add(x);
                            break;
                        }
                    } else {
                        switch (state[0]) {
                        case START_LINE:
                            result.addAll(leadingWhiteSpace);
                            leadingWhiteSpace.clear();
                            result.add(x);
                            state[0] = LeftAlignState.IN_LINE;
                            break;
                        case IN_LINE:
                            result.add(x);
                            break;
                        }                        
                    }
                }
            });
        }
        
        return result;
    }
}
