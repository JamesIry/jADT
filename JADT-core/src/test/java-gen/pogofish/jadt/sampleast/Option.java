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
public abstract class Option<A> {

   private Option() {
   }

   public static final <A> Option<A> _Some(A value) { return new Some<A>(value); }
   @SuppressWarnings("rawtypes")
   private static final Option _None = new None();
   @SuppressWarnings("unchecked")
   public static final <A> Option<A> _None() { return _None; }

   public static interface Visitor<A, ResultType> {
      ResultType visit(Some<A> x);
      ResultType visit(None<A> x);
   }

   public static abstract class VisitorWithDefault<A, ResultType> implements Visitor<A, ResultType> {
      @Override
      public ResultType visit(Some<A> x) { return getDefault(x); }

      @Override
      public ResultType visit(None<A> x) { return getDefault(x); }

      protected abstract ResultType getDefault(Option<A> x);
   }

   public static interface VoidVisitor<A> {
      void visit(Some<A> x);
      void visit(None<A> x);
   }

   public static abstract class VoidVisitorWithDefault<A> implements VoidVisitor<A> {
      @Override
      public void visit(Some<A> x) { doDefault(x); }

      @Override
      public void visit(None<A> x) { doDefault(x); }

      protected abstract void doDefault(Option<A> x);
   }

   public static final class Some<A> extends Option<A> {
      public final A value;

      public Some(A value) {
         this.value = value;
      }

      @Override
      public <ResultType> ResultType accept(Visitor<A, ResultType> visitor) { return visitor.visit(this); }

      @Override
      public void accept(VoidVisitor<A> visitor) { visitor.visit(this); }

      @Override
      public int hashCode() {
          final int prime = 31;
          int result = 1;
          result = prime * result + ((value == null) ? 0 : value.hashCode());
          return result;
      }

      @Override
      public boolean equals(Object obj) {
         if (this == obj) return true;
         if (obj == null) return false;
         if (getClass() != obj.getClass()) return false;
         @SuppressWarnings("rawtypes")         Some other = (Some)obj;
         if (value == null) {
            if (other.value != null) return false;
         } else if (!value.equals(other.value)) return false;
         return true;
      }

      @Override
      public String toString() {
         return "Some(value = " + value + ")";
      }

   }

   public static final class None<A> extends Option<A> {

      public None() {
      }

      @Override
      public <ResultType> ResultType accept(Visitor<A, ResultType> visitor) { return visitor.visit(this); }

      @Override
      public void accept(VoidVisitor<A> visitor) { visitor.visit(this); }

      @Override
      public int hashCode() {
          return 0;
      }

      @Override
      public boolean equals(Object obj) {
         if (this == obj) return true;
         if (obj == null) return false;
         if (getClass() != obj.getClass()) return false;
         return true;
      }

      @Override
      public String toString() {
         return "None";
      }

   }

   public abstract <ResultType> ResultType accept(Visitor<A, ResultType> visitor);

   public abstract void accept(VoidVisitor<A> visitor);

}