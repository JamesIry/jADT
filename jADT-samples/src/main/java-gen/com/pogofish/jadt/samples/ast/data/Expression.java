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
public abstract class Expression {

   private Expression() {
   }

   public static final  Expression _Add(Expression left, Expression right) { return new Add(left, right); }
   public static final  Expression _Variable(String name) { return new Variable(name); }
   public static final  Expression _IntLiteral(int value) { return new IntLiteral(value); }
   public static final  Expression _LongLiteral(long value) { return new LongLiteral(value); }

   public static interface Visitor<ResultType> {
      ResultType visit(Add x);
      ResultType visit(Variable x);
      ResultType visit(IntLiteral x);
      ResultType visit(LongLiteral x);
   }

   public static abstract class VisitorWithDefault<ResultType> implements Visitor<ResultType> {
      @Override
      public ResultType visit(Add x) { return getDefault(x); }

      @Override
      public ResultType visit(Variable x) { return getDefault(x); }

      @Override
      public ResultType visit(IntLiteral x) { return getDefault(x); }

      @Override
      public ResultType visit(LongLiteral x) { return getDefault(x); }

      protected abstract ResultType getDefault(Expression x);
   }

   public static interface VoidVisitor {
      void visit(Add x);
      void visit(Variable x);
      void visit(IntLiteral x);
      void visit(LongLiteral x);
   }

   public static abstract class VoidVisitorWithDefault implements VoidVisitor {
      @Override
      public void visit(Add x) { doDefault(x); }

      @Override
      public void visit(Variable x) { doDefault(x); }

      @Override
      public void visit(IntLiteral x) { doDefault(x); }

      @Override
      public void visit(LongLiteral x) { doDefault(x); }

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

   public static final class IntLiteral extends Expression {
      public final int value;

      public IntLiteral(int value) {
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
         IntLiteral other = (IntLiteral)obj;
         if (value != other.value) return false;
         return true;
      }

      @Override
      public String toString() {
         return "IntLiteral(value = " + value + ")";
      }

   }

   public static final class LongLiteral extends Expression {
      public final long value;

      public LongLiteral(long value) {
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
          result = prime * result + (int)value;
          return result;
      }

      @Override
      public boolean equals(Object obj) {
         if (this == obj) return true;
         if (obj == null) return false;
         if (getClass() != obj.getClass()) return false;
         LongLiteral other = (LongLiteral)obj;
         if (value != other.value) return false;
         return true;
      }

      @Override
      public String toString() {
         return "LongLiteral(value = " + value + ")";
      }

   }

   public abstract <ResultType> ResultType accept(Visitor<ResultType> visitor);

   public abstract void accept(VoidVisitor visitor);

}