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
package com.pogofish.jadt.ast;

import java.util.Collections;
import java.util.List;

/**
 * Helpful constants for working with the AST
 * 
 * @author jiry
 */
public class ASTConstants {
    public static final List<JavaComment> NO_COMMENTS = Collections.emptyList();
    
    public static final Pkg EMPTY_PKG = Pkg._Pkg(NO_COMMENTS, "");
    
    public static final List<Imprt> NO_IMPORTS = Collections.emptyList();
}
