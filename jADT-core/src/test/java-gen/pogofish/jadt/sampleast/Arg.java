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
public final class Arg {

   public static final  Arg _Arg(Type type, String name) { return new Arg(type, name); }

      public final Type type;
      public final String name;

      public Arg(Type type, String name) {
         this.type = type;
         this.name = name;
      }

      @Override
      public int hashCode() {
          final int prime = 31;
          int result = 1;
          result = prime * result + ((type == null) ? 0 : type.hashCode());
          result = prime * result + ((name == null) ? 0 : name.hashCode());
          return result;
      }

      @Override
      public boolean equals(Object obj) {
         if (this == obj) return true;
         if (obj == null) return false;
         if (getClass() != obj.getClass()) return false;
         Arg other = (Arg)obj;
         if (type == null) {
            if (other.type != null) return false;
         } else if (!type.equals(other.type)) return false;
         if (name == null) {
            if (other.name != null) return false;
         } else if (!name.equals(other.name)) return false;
         return true;
      }

      @Override
      public String toString() {
         return "Arg(type = " + type + ", name = " + name + ")";
      }

}