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
public abstract class Expression {

   private Expression() {
   }

   public static final  Expression _Add(Expression left, Expression right) { return new Add(left, right); }
   public static final  Expression _Variable(String name) { return new Variable(name); }
   public static final  Expression _Literal(int value) { return new Literal(value); }

   public static interface Visitor<ResultType> {
      ResultType visit(Add x);
      ResultType visit(Variable x);
      ResultType visit(Literal x);
   }

   public static abstract class VisitorWithDefault<ResultType> implements Visitor<ResultType> {
      @Override
      public ResultType visit(Add x) { return getDefault(x); }

      @Override
      public ResultType visit(Variable x) { return getDefault(x); }

      @Override
      public ResultType visit(Literal x) { return getDefault(x); }

      protected abstract ResultType getDefault(Expression x);
   }

   public static interface VoidVisitor {
      void visit(Add x);
      void visit(Variable x);
      void visit(Literal x);
   }

   public static abstract class VoidVisitorWithDefault implements VoidVisitor {
      @Override
      public void visit(Add x) { doDefault(x); }

      @Override
      public void visit(Variable x) { doDefault(x); }

      @Override
      public void visit(Literal x) { doDefault(x); }

      protected abstract void doDefault(Expression x);
   }

   public static final class Add extends Expression {
      public final Expression left;
      public final Expression right;

      public Add(Expression left, Expression right) {
         this.left = left;
         this.right = right;
      }

      @Override
      public <ResultType> ResultType accept(Visitor<ResultType> visitor) { return visitor.visit(this); }

      @Override
      public void accept(VoidVisitor visitor) { visitor.visit(this); }

      @Override
      public int hashCode() {
          final int prime = 31;
          int result = 1;
          result = prime * result + ((left == null) ? 0 : left.hashCode());
          result = prime * result + ((right == null) ? 0 : right.hashCode());
          return result;
      }

      @Override
      public boolean equals(Object obj) {
         if (this == obj) return true;
         if (obj == null) return false;
         if (getClass() != obj.getClass()) return false;
         Add other = (Add)obj;
         if (left == null) {
            if (other.left != null) return false;
         } else if (!left.equals(other.left)) return false;
         if (right == null) {
            if (other.right != null) return false;
         } else if (!right.equals(other.right)) return false;
         return true;
      }

      @Override
      public String toString() {
         return "Add(left = " + left + ", right = " + right + ")";
      }

   }

   public static final class Variable extends Expression {
      public final String name;

      public Variable(String name) {
         this.name = name;
      }

      @Override
      public <ResultType> ResultType accept(Visitor<ResultType> visitor) { return visitor.visit(this); }

      @Override
      public void accept(VoidVisitor visitor) { visitor.visit(this); }

      @Override
      public int hashCode() {
          final int prime = 31;
          int result = 1;
          result = prime * result + ((name == null) ? 0 : name.hashCode());
          return result;
      }

      @Override
      public boolean equals(Object obj) {
         if (this == obj) return true;
         if (obj == null) return false;
         if (getClass() != obj.getClass()) return false;
         Variable other = (Variable)obj;
         if (name == null) {
            if (other.name != null) return false;
         } else if (!name.equals(other.name)) return false;
         return true;
      }

      @Override
      public String toString() {
         return "Variable(name = " + name + ")";
      }

   }

   public static final class Literal extends Expression {
      public final int value;

      public Literal(int value) {
         this.value = value;
      }

      @Override
      public <ResultType> ResultType accept(Visitor<ResultType> visitor) { return visitor.visit(this); }

      @Override
      public void accept(VoidVisitor visitor) { visitor.visit(this); }

      @Override
      public int hashCode() {
          final int prime = 31;
          int result = 1;
          result = prime * result + value;
          return result;
      }

      @Override
      public boolean equals(Object obj) {
         if (this == obj) return true;
         if (obj == null) return false;
         if (getClass() != obj.getClass()) return false;
         Literal other = (Literal)obj;
         if (value != other.value) return false;
         return true;
      }

      @Override
      public String toString() {
         return "Literal(value = " + value + ")";
      }

   }

   public abstract <ResultType> ResultType accept(Visitor<ResultType> visitor);

   public abstract void accept(VoidVisitor visitor);

}