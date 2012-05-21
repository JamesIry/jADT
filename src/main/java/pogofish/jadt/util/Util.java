package pogofish.jadt.util;

import java.util.*;

public class Util {
    public static <A> List<A> list(A... elements) {
        final List<A> list = new ArrayList<A>(elements.length);
        for (A element : elements) {
            list.add(element);
        }
        return list;
    }
    
    public static <A> Set<A> set(A... elements) {
        final Set<A> set = new HashSet<A>(elements.length);
        for (A element : elements) {
            set.add(element);
        }
        return set;
    }
    
}
