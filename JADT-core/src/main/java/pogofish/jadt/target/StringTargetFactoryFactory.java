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

import static pogofish.jadt.util.Util.list;

import java.util.*;

/**
 * Factory that factories a factory, mostly for testing
 *
 * @author jiry
 */
public class StringTargetFactoryFactory implements TargetFactoryFactory {
    private Map<String, List<StringTargetFactory>> results = new HashMap<String, List<StringTargetFactory>>();
    
    @Override
    public StringTargetFactory createTargetFactory(String baseDir) {
        final StringTargetFactory result = new StringTargetFactory(baseDir);
        if (results.containsKey(baseDir)) {
            results.get(baseDir).add(result);
        } else {
            results.put(baseDir, list(result));
        }
        return result;
    }
    
    /**
     * A map from base dir to all the StringTargetFactories created for that base dir
     */
    public Map<String, List<StringTargetFactory>> results() {
        return results;
    }
}
