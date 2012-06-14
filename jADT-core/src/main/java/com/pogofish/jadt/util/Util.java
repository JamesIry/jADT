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
package com.pogofish.jadt.util;

import java.util.*;

/**
 * Some convenient static utility methods
 *
 * @author jiry
 */
public class Util {
    /**
     * Returns an array list of elements
     */
    public static <A> List<A> list(A... elements) {
        final List<A> list = new ArrayList<A>(elements.length);
        for (A element : elements) {
            list.add(element);
        }
        return list;
    }

    /**
     * Returns a hash set of elements
     */
    public static <A> Set<A> set(A... elements) {
        final Set<A> set = new HashSet<A>(elements.length);
        for (A element : elements) {
            set.add(element);
        }
        return set;
    }

    /**
     * Makes a string by concetenating the toString of objects together separated by the separator
     */
    public static String makeString(Iterable<?> objects, String separator) {
        final StringBuilder builder = new StringBuilder();
        boolean first = true;
        for (Object object : objects) {
            if (first) {
                first = false;
            } else {
                builder.append(separator);
            }
            builder.append(object);
        }
        return builder.toString();
    }
    
}
