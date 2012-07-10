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
/*
 * This jADT file is an example AST for a toy language.
 * It is marked up with START SNIPPET and END SNIPPET boundaries 
 * to support /jADT/src/site/apt/index.apt
 */
package com.pogofish.jadt.samples.ast.data;

import java.util.List;

/*
This file was generated based on /Users/jiry/workspace/JADT/jADT-samples/src/main/jadt/SampleAST.jadt using jADT version 0.3.0-SNAPSHOT http://jamesiry.github.com/jADT/ . Please do not modify directly.

The source was parsed as: 

Function =
    Function(final Type returnType, final String name, List<Arg> args, final List<Statement> statements)
*/
public final class Function {

   public static final  Function _Function(Type returnType, String name, List<Arg> args, List<Statement> statements) { return new Function(returnType, name, args, statements); }

      public final Type returnType;
      public final String name;
      public List<Arg> args;
      public final List<Statement> statements;

      public Function(Type returnType, String name, List<Arg> args, List<Statement> statements) {
         this.returnType = returnType;
         this.name = name;
         this.args = args;
         this.statements = statements;
      }

      @Override
      public int hashCode() {
          final int prime = 31;
          int result = 1;
          result = prime * result + ((returnType == null) ? 0 : returnType.hashCode());
          result = prime * result + ((name == null) ? 0 : name.hashCode());
          result = prime * result + ((args == null) ? 0 : args.hashCode());
          result = prime * result + ((statements == null) ? 0 : statements.hashCode());
          return result;
      }

      @Override
      public boolean equals(Object obj) {
         if (this == obj) return true;
         if (obj == null) return false;
         if (getClass() != obj.getClass()) return false;
         Function other = (Function)obj;
         if (returnType == null) {
            if (other.returnType != null) return false;
         } else if (!returnType.equals(other.returnType)) return false;
         if (name == null) {
            if (other.name != null) return false;
         } else if (!name.equals(other.name)) return false;
         if (args == null) {
            if (other.args != null) return false;
         } else if (!args.equals(other.args)) return false;
         if (statements == null) {
            if (other.statements != null) return false;
         } else if (!statements.equals(other.statements)) return false;
         return true;
      }

      @Override
      public String toString() {
         return "Function(returnType = " + returnType + ", name = " + name + ", args = " + args + ", statements = " + statements + ")";
      }

}