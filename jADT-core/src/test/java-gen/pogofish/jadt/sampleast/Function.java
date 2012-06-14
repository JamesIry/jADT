package com.pogofish.jadt.sampleast;

import java.util.List;

/*
This file was generated based on /Users/jiry/workspace/JADT/JADT-core/src/test/jadt/SampleAST.jadt using JADT version 0.0.1-SNAPSHOT (http://jamesiry.github.com/JADT/). Please do not modify directly.

The source was parsed as: 

package com.pogofish.jadt.sampleast

import java.util.List

Type =
    Int
  | Long
Function =
    Function(Type returnType, String name, List<Arg> args, List<Statement> statements)
Arg =
    Arg(Type type, String name)
Statement =
    Declaration(Type type, String name, Expression expression)
  | Assignment(String name, Expression expression)
  | Return(Expression expression)
Expression =
    Add(Expression left, Expression right)
  | Variable(String name)
  | Literal(int value)
Option =
    Some(A value)
  | None

*/
public final class Function {

   public static final  Function _Function(Type returnType, String name, List<Arg> args, List<Statement> statements) { return new Function(returnType, name, args, statements); }

      public final Type returnType;
      public final String name;
      public final List<Arg> args;
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