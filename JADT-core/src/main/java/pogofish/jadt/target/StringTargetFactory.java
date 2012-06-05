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
package pogofish.jadt.target;

import java.util.*;

/**
 * Factory that creates StringTargets, mostly useful for testing
 *
 * @author jiry
 */
public class StringTargetFactory implements TargetFactory {
    final String baseDir;
    private final Map<String, StringTarget> targets = new HashMap<String, StringTarget>();
    
    /**
     * Creates a StringTarget.  The baseDir is only remembered for the purposes of testing, it isn't used anywhere
     * 
     * @param baseDir
     */
    public StringTargetFactory(String baseDir) {
        super();
        this.baseDir = baseDir;
    }

    @Override
    public Target createTarget(String className) {
        StringTarget target = new StringTarget(className);
        targets.put(className, target);
        return target;
    }
    
    /**
     * Get the results as a Map from names to String data.  The names are composed of
     * @return Map with results
     */
    public Map<String, String> getResults() {
        final Map<String, String> results = new HashMap<String, String>(targets.size());
        for (Map.Entry<String, StringTarget> entry : targets.entrySet()) {
            results.put(entry.getKey(), entry.getValue().result());
        }
        return Collections.unmodifiableMap(results);
    }
}
