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
package com.pogofish.jadt.source;

import java.util.List;

import com.pogofish.jadt.util.Util;

/**
 * Factory that creates StringSources, mostly useful for testing
 *
 * @author jiry
 */
public class StringSourceFactory implements SourceFactory {
    /**
     * The string that will be read by the source
     */
    private final String source;
        
    /**
     * Create a factory that will create StringSources that will read from the given string
     * @param source
     */
    public StringSourceFactory(String source) {
        super();
        this.source = source;
    }

    /**
     * Create a list with a single StringSource - the sourceFileName will be used as the srcInfo in the resulting Source
     */
    @Override
    public List<StringSource> createSources(String sourceFileName) {
        return Util.list(new StringSource(sourceFileName, source));
    }

}
