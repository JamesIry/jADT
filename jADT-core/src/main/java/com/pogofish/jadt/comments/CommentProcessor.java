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

import static com.pogofish.jadt.ast.BlockToken._BlockWhiteSpace;
import static com.pogofish.jadt.ast.JDTagSection._JDTagSection;
import static com.pogofish.jadt.ast.JDToken._JDAsterisk;
import static com.pogofish.jadt.ast.JDToken._JDEOL;
import static com.pogofish.jadt.ast.JDToken._JDWhiteSpace;
import static com.pogofish.jadt.ast.JavaComment._JavaBlockComment;
import static com.pogofish.jadt.ast.JavaComment._JavaDocComment;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.pogofish.jadt.ast.BlockToken;
import com.pogofish.jadt.ast.BlockToken.BlockEOL;
import com.pogofish.jadt.ast.BlockToken.BlockWhiteSpace;
import com.pogofish.jadt.ast.BlockToken.BlockWord;
import com.pogofish.jadt.ast.JDTagSection;
import com.pogofish.jadt.ast.JDToken;
import com.pogofish.jadt.ast.JDToken.JDAsterisk;
import com.pogofish.jadt.ast.JDToken.JDEOL;
import com.pogofish.jadt.ast.JDToken.JDTag;
import com.pogofish.jadt.ast.JDToken.JDWhiteSpace;
import com.pogofish.jadt.ast.JDToken.JDWord;
import com.pogofish.jadt.ast.JavaComment;
import com.pogofish.jadt.ast.JavaComment.JavaBlockComment;
import com.pogofish.jadt.ast.JavaComment.JavaDocComment;
import com.pogofish.jadt.ast.JavaComment.JavaEOLComment;
import com.pogofish.jadt.util.Util;

/**
 * Methods for processing parsed comments
 * 
 * @author jiry
 */
public class CommentProcessor {
    /**
     * Turns a list of JavaComment into list of comments that only has javadoc comments
     */
    public List<JavaComment> javaDocOnly(final List<JavaComment> originals) {
        final List<JavaComment> results = new ArrayList<JavaComment>(
                originals.size());
        for (JavaComment comment : originals) {
            comment._switch(new JavaComment.SwitchBlockWithDefault() {
                
                @Override
                public void _case(JavaDocComment x) {
                    results.add(x);
                }

                @Override
                protected void _default(JavaComment x) {
                    // do nothing, we only want to add javadocs
                }
            });
        }
        return results;
    }
    
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
    
    /**
     * Produce a copy of a list of JavaDoc comments by pulling specified parameter tags out of an orignal list of comments.  
     * Block and EOL comments are skipped.  JavaDoc comments are stripped down to only have the content of the specified 
     * param tag
     */
    public List<JavaComment> paramDoc(final String paramName, List<JavaComment> originals) {
        final List<JavaComment> results = new ArrayList<JavaComment>(originals.size());
        for (JavaComment original : originals) {
            original._switch(new JavaComment.SwitchBlock() {
                @Override
                public void _case(JavaDocComment x) {
                    paramDocSections(paramName, x.tagSections, results);
                }

                @Override
                public void _case(JavaBlockComment x) {
                    // skip
                }

                @Override
                public void _case(JavaEOLComment x) {
                    // skip
                }
            });
        }
        return results;
    }
    

    private void paramDocSections(String paramName,
            List<JDTagSection> tagSections, List<JavaComment> results) {
        for (JDTagSection section : tagSections) {
            if (section.name.equals("@param")) {
                paramDocSection(paramName, section.tokens, results);
            }
        }
    }    
    
    private static enum ParamState {
        BEGIN, ASTERISK, TAGGED, ACCUMULATING, DEAD;
    }

    private void paramDocSection(final String paramName, final List<JDToken> tokens,
            List<JavaComment> results) {
        final ParamState state[] = new ParamState[]{ParamState.BEGIN};
        @SuppressWarnings("unchecked")
        final List<JDToken>[] accum = new List[1];
        
        for (JDToken token : tokens) {
            token._switch(new JDToken.SwitchBlock() {
                
                @Override
                public void _case(JDWhiteSpace x) {
                    if (state[0] == ParamState.ACCUMULATING) {
                        accum[0].add(x);
                    }                    
                }
                
                @Override
                public void _case(JDWord x) {
                    switch(state[0]) {
                    case ACCUMULATING:
                        accum[0].add(x);
                        break;
                    case TAGGED: 
                        if (x.word.equals(paramName)) {
                            state[0] = ParamState.ACCUMULATING;
                            accum[0] = new ArrayList<JDToken>(tokens.size() - 1);
                            accum[0].add(_JDEOL("\n"));
                            accum[0].add(_JDWhiteSpace(" "));
                            accum[0].add(_JDAsterisk());
                        } else {
                            state[0] = ParamState.DEAD;
                        }
                        break;
                    default:
                        state[0] = ParamState.DEAD;
                        break;
                    }                 
                }
                
                @Override
                public void _case(JDTag x) {
                    switch(state[0]) {
                    case ACCUMULATING:
                        accum[0].add(x);
                        break;
                    case BEGIN:
                        state[0] = ParamState.TAGGED;
                        break;
                    case ASTERISK:
                        state[0] = ParamState.TAGGED;
                        break;
                    default:
                        state[0] = ParamState.DEAD;
                        break;
                    }    
                }
                
                @Override
                public void _case(JDEOL x) {
                    if (state[0] == ParamState.ACCUMULATING) {
                        accum[0].add(x);
                    }                    
                }
                
                @Override
                public void _case(JDAsterisk x) {
                    switch(state[0]) {
                    case ACCUMULATING:
                        accum[0].add(x);
                        break;
                    case BEGIN:
                        state[0] = ParamState.ASTERISK;
                        break;
                    default:
                        state[0] = ParamState.DEAD;
                        break;
                    }  
                }
            });
        }
        
        if(state[0] == ParamState.ACCUMULATING) {
            results.add(_JavaDocComment("/**", accum[0], Util.<JDTagSection>list(), "*/"));
        }
        
    }

    /**
     * Align a list of comments on the left marging.  EOLComments are left alone.  Block and JavaDoc comments are aligned by making every line that starts
     * with a * be one space in from the comment opener.  Lines that don't start with * are left alone.
     */
    public List<JavaComment> leftAlign(List<JavaComment> originals) {
        final List<JavaComment> results = new ArrayList<JavaComment>(
                originals.size());
        for (JavaComment original : originals) {
            results.add(original
                    .match(new JavaComment.MatchBlock<JavaComment>() {

                        @Override
                        public JavaComment _case(JavaDocComment x) {
                            final List<JDToken> leadingWhiteSpace = new ArrayList<JDToken>(1);
                            final LeftAlignState state[] = new LeftAlignState[] { LeftAlignState.IN_LINE };
                            return _JavaDocComment(x.start, leftAlignSection(x.generalSection, x.tagSections.isEmpty(), leadingWhiteSpace, state), leftAlignSections(x.tagSections, leadingWhiteSpace, state), x.end);
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
            List<JDTagSection> tagSections, List<JDToken> leadingWhiteSpace, LeftAlignState[] state) {
        final int size = tagSections.size();
        final List<JDTagSection> newSections = new ArrayList<JDTagSection>(size);
        int count = 0;
        for (JDTagSection section : tagSections) {
            count ++;
            newSections.add(_JDTagSection(section.name, leftAlignSection(section.tokens, count == size, leadingWhiteSpace, state)));
        }
        return newSections;
    }    

    private List<JDToken> leftAlignSection(List<JDToken> originalSection, boolean lastSection, final List<JDToken> leadingWhiteSpace, final LeftAlignState[] state) {
        final List<JDToken> result = new ArrayList<JDToken>(
                originalSection.size() + 1);

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
        
        if (lastSection && state[0] == LeftAlignState.START_LINE) {
           result.add(_JDWhiteSpace(" "));         
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
