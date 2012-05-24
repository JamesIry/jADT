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
package pogofish.jadt.util;

import java.io.IOException;

/**
 * The sole reason this exists is because normal try/catch(IOException) blocks can be hard to cover
 * completely using unit testing.  By implementing doAction you can hide the try catch in this 
 * base class where it's easily tested
 *
 * @author jiry
 */
public abstract class IOExceptionAction<A> {
    /**
     * Call this method, it will delegate to your doAction and wrap up any exceptions
     * 
     * @return A
     * @throws RuntimeException containing an IOException if that occurs duing doAction
     */
    public final A execute() {
        try {
            return doAction();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Implement this puppy
     * 
     * @return result
     * @throws IOException on failure
     */
    public abstract A doAction() throws IOException;
}
