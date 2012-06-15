package com.pogofish.jadt.samples.ast.data;


/*
This file was generated based on /Users/jiry/workspace/JADT/jADT-samples/src/main/jadt/SampleAST.jadt using jADT version 0.2.0-SNAPSHOT http://jamesiry.github.com/jADT/ . Please do not modify directly.

The source was parsed as: 

package com.pogofish.jadt.samples.ast.data

import java.util.List

Type =
    Int
  | Long
Function =
    Function(final Type returnType, final String name, List<Arg> args, final List<Statement> statements)
Arg =
    Arg(final Type type, final String name)
Statement =
    Declaration(final Type type, final String name, final Expression expression)
  | Assignment(final String name, final Expression expression)
  | Return(final Expression expression)
Expression =
    Add(final Expression left, final Expression right)
  | Variable(final String name)
  | IntLiteral(final int value)
  | LongLiteral(final long value)

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