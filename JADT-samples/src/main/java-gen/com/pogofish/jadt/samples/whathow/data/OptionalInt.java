package com.pogofish.jadt.samples.whathow.data;

/*
This file was generated based on /Users/jiry/workspace/JADT/JADT-samples/../JADT-samples/src/main/jadt/WhatHowSamples.jadt using JADT version null http://jamesiry.github.com/JADT/ . Please do not modify directly.

The source was parsed as: 

package com.pogofish.jadt.samples.whathow.data

IntBinaryTree =
    Node(int value, IntBinaryTree left, IntBinaryTree right)
  | EmptyTree
OptionalInt =
    Some(int value)
  | None
BinaryTree =
    Node(T value, BinaryTree<T> left, BinaryTree<T> right)
  | EmptyTree
Manager =
    Manager(String name)
TPSReportStatus =
    Pending
  | Approved(Manager approver)
  | Denied(Manager rejector)

*/
public abstract class OptionalInt {

   private OptionalInt() {
   }

   public static final  OptionalInt _Some(int value) { return new Some(value); }
   private static final OptionalInt _None = new None();
   public static final  OptionalInt _None() { return _None; }

   public static interface Visitor<ResultType> {
      ResultType visit(Some x);
      ResultType visit(None x);
   }

   public static abstract class VisitorWithDefault<ResultType> implements Visitor<ResultType> {
      @Override
      public ResultType visit(Some x) { return getDefault(x); }

      @Override
      public ResultType visit(None x) { return getDefault(x); }

      protected abstract ResultType getDefault(OptionalInt x);
   }

   public static interface VoidVisitor {
      void visit(Some x);
      void visit(None x);
   }

   public static abstract class VoidVisitorWithDefault implements VoidVisitor {
      @Override
      public void visit(Some x) { doDefault(x); }

      @Override
      public void visit(None x) { doDefault(x); }

      protected abstract void doDefault(OptionalInt x);
   }

   public static final class Some extends OptionalInt {
      public final int value;

      public Some(int value) {
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
         Some other = (Some)obj;
         if (value != other.value) return false;
         return true;
      }

      @Override
      public String toString() {
         return "Some(value = " + value + ")";
      }

   }

   public static final class None extends OptionalInt {

      public None() {
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
         return "None";
      }

   }

   public abstract <ResultType> ResultType accept(Visitor<ResultType> visitor);

   public abstract void accept(VoidVisitor visitor);

}