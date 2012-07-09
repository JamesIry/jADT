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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
     * delegate to your to an ExceptionAction and wraps checked exceptions in RuntimExceptions, leaving unchecked exceptions alone.
     * 
     * @return A
     * @throws Either Error or RuntimeException.  Error on Errors in doAction and RuntimeException if doError throws a RuntimeException or an Exception.  In the latter case
     * the RuntimeException will contain the original Exception as its cause
     */
    public static <A> A execute(ExceptionAction<A> action) {
        try {
            return action.doAction();
        } catch (RuntimeException e) {
            throw e;
        } catch (Error e) {
            throw e;
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }
}
