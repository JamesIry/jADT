package pogofish.jadt.emitter;

import java.util.*;

import pogofish.jadt.emitter.Target;
import pogofish.jadt.emitter.TargetFactory;

public class StringTargetFactory implements TargetFactory {
    private Map<String, String> results = new HashMap<String, String>();
    
    @Override
    public Target createTarget(String className) {
        return new StringTarget(className, results);
    }
    
    public Map<String, String> getResults() {
        return Collections.unmodifiableMap(results);
    }
}
