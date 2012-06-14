package com.pogofish.jadt.samples.ast.data;


/*
This file was generated based on /Users/jiry/workspace/JADT/jADT-samples/src/main/jadt/SampleAST.jadt using jADT version ${pom.version} http://jamesiry.github.com/jADT/ . Please do not modify directly.

The source was parsed as: 

package com.pogofish.jadt.samples.ast.data

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
  | IntLiteral(int value)
  | LongLiteral(long value)

*/
public abstract class Type {

   private Type() {
   }

   private static final Type _Int = new Int();
   public static final  Type _Int() { return _Int; }
   private static final Type _Long = new Long();
   public static final  Type _Long() { return _Long; }

   public static interface Visitor<ResultType> {
      ResultType visit(Int x);
      ResultType visit(Long x);
   }

   public static abstract class VisitorWithDefault<ResultType> implements Visitor<ResultType> {
      @Override
      public ResultType visit(Int x) { return getDefault(x); }

      @Override
      public ResultType visit(Long x) { return getDefault(x); }

      protected abstract ResultType getDefault(Type x);
   }

   public static interface VoidVisitor {
      void visit(Int x);
      void visit(Long x);
   }

   public static abstract class VoidVisitorWithDefault implements VoidVisitor {
      @Override
      public void visit(Int x) { doDefault(x); }

      @Override
      public void visit(Long x) { doDefault(x); }

      protected abstract void doDefault(Type x);
   }

   public static final class Int extends Type {

      public Int() {
      }

      @Override
      public <ResultType> ResultType accept(Visitor<ResultType> visitor) { return visitor.visit(this); }

      @Override
      public void accept(VoidVisitor visitor) { visitor.visit(this); }

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
         return "Int";
      }

   }

   public static final class Long extends Type {

      public Long() {
      }

      @Override
      public <ResultType> ResultType accept(Visitor<ResultType> visitor) { return visitor.visit(this); }

      @Override
      public void accept(VoidVisitor visitor) { visitor.visit(this); }

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
         return "Long";
      }

   }

   public abstract <ResultType> ResultType accept(Visitor<ResultType> visitor);

   public abstract void accept(VoidVisitor visitor);

}