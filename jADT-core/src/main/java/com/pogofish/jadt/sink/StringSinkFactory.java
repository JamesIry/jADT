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
package com.pogofish.jadt.sink;

import java.util.*;

/**
 * Factory that creates StringSinks, mostly useful for testing
 *
 * @author jiry
 */
public class StringSinkFactory implements SinkFactory {
    final String baseDir;
    private final Map<String, StringSink> sinks = new HashMap<String, StringSink>();
    
    /**
     * Creates a StringSink.  The baseDir is only remembered for the purposes of testing, it isn't used anywhere
     * 
     * @param baseDir
     */
    public StringSinkFactory(String baseDir) {
        super();
        this.baseDir = baseDir;
    }

    @Override
    public Sink createSink(String className) {
        StringSink sink = new StringSink(className);
        sinks.put(className, sink);
        return sink;
    }
    
    /**
     * Get the results as a Map from names to String data.  The names are composed of
     * @return Map with results
     */
    public Map<String, String> getResults() {
        final Map<String, String> results = new HashMap<String, String>(sinks.size());
        for (Map.Entry<String, StringSink> entry : sinks.entrySet()) {
            results.put(entry.getKey(), entry.getValue().result());
        }
        return Collections.unmodifiableMap(results);
    }
}
