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
public abstract class Statement {

   private Statement() {
   }

   public static final  Statement _Declaration(Type type, String name, Expression expression) { return new Declaration(type, name, expression); }
   public static final  Statement _Assignment(String name, Expression expression) { return new Assignment(name, expression); }
   public static final  Statement _Return(Expression expression) { return new Return(expression); }

   public static interface Visitor<ResultType> {
      ResultType visit(Declaration x);
      ResultType visit(Assignment x);
      ResultType visit(Return x);
   }

   public static abstract class VisitorWithDefault<ResultType> implements Visitor<ResultType> {
      @Override
      public ResultType visit(Declaration x) { return getDefault(x); }

      @Override
      public ResultType visit(Assignment x) { return getDefault(x); }

      @Override
      public ResultType visit(Return x) { return getDefault(x); }

      protected abstract ResultType getDefault(Statement x);
   }

   public static interface VoidVisitor {
      void visit(Declaration x);
      void visit(Assignment x);
      void visit(Return x);
   }

   public static abstract class VoidVisitorWithDefault implements VoidVisitor {
      @Override
      public void visit(Declaration x) { doDefault(x); }

      @Override
      public void visit(Assignment x) { doDefault(x); }

      @Override
      public void visit(Return x) { doDefault(x); }

      protected abstract void doDefault(Statement x);
   }

   public static final class Declaration extends Statement {
      public final Type type;
      public final String name;
      public final Expression expression;

      public Declaration(Type type, String name, Expression expression) {
         this.type = type;
         this.name = name;
         this.expression = expression;
      }

      @Override
      public <ResultType> ResultType accept(Visitor<ResultType> visitor) { return visitor.visit(this); }

      @Override
      public void accept(VoidVisitor visitor) { visitor.visit(this); }

      @Override
      public int hashCode() {
          final int prime = 31;
          int result = 1;
          result = prime * result + ((type == null) ? 0 : type.hashCode());
          result = prime * result + ((name == null) ? 0 : name.hashCode());
          result = prime * result + ((expression == null) ? 0 : expression.hashCode());
          return result;
      }

      @Override
      public boolean equals(Object obj) {
         if (this == obj) return true;
         if (obj == null) return false;
         if (getClass() != obj.getClass()) return false;
         Declaration other = (Declaration)obj;
         if (type == null) {
            if (other.type != null) return false;
         } else if (!type.equals(other.type)) return false;
         if (name == null) {
            if (other.name != null) return false;
         } else if (!name.equals(other.name)) return false;
         if (expression == null) {
            if (other.expression != null) return false;
         } else if (!expression.equals(other.expression)) return false;
         return true;
      }

      @Override
      public String toString() {
         return "Declaration(type = " + type + ", name = " + name + ", expression = " + expression + ")";
      }

   }

   public static final class Assignment extends Statement {
      public final String name;
      public final Expression expression;

      public Assignment(String name, Expression expression) {
         this.name = name;
         this.expression = expression;
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
          result = prime * result + ((expression == null) ? 0 : expression.hashCode());
          return result;
      }

      @Override
      public boolean equals(Object obj) {
         if (this == obj) return true;
         if (obj == null) return false;
         if (getClass() != obj.getClass()) return false;
         Assignment other = (Assignment)obj;
         if (name == null) {
            if (other.name != null) return false;
         } else if (!name.equals(other.name)) return false;
         if (expression == null) {
            if (other.expression != null) return false;
         } else if (!expression.equals(other.expression)) return false;
         return true;
      }

      @Override
      public String toString() {
         return "Assignment(name = " + name + ", expression = " + expression + ")";
      }

   }

   public static final class Return extends Statement {
      public final Expression expression;

      public Return(Expression expression) {
         this.expression = expression;
      }

      @Override
      public <ResultType> ResultType accept(Visitor<ResultType> visitor) { return visitor.visit(this); }

      @Override
      public void accept(VoidVisitor visitor) { visitor.visit(this); }

      @Override
      public int hashCode() {
          final int prime = 31;
          int result = 1;
          result = prime * result + ((expression == null) ? 0 : expression.hashCode());
          return result;
      }

      @Override
      public boolean equals(Object obj) {
         if (this == obj) return true;
         if (obj == null) return false;
         if (getClass() != obj.getClass()) return false;
         Return other = (Return)obj;
         if (expression == null) {
            if (other.expression != null) return false;
         } else if (!expression.equals(other.expression)) return false;
         return true;
      }

      @Override
      public String toString() {
         return "Return(expression = " + expression + ")";
      }

   }

   public abstract <ResultType> ResultType accept(Visitor<ResultType> visitor);

   public abstract void accept(VoidVisitor visitor);

}