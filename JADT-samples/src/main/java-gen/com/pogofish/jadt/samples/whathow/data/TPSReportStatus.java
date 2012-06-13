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
public abstract class TPSReportStatus {

   private TPSReportStatus() {
   }

   private static final TPSReportStatus _Pending = new Pending();
   public static final  TPSReportStatus _Pending() { return _Pending; }
   public static final  TPSReportStatus _Approved(Manager approver) { return new Approved(approver); }
   public static final  TPSReportStatus _Denied(Manager rejector) { return new Denied(rejector); }

   public static interface Visitor<ResultType> {
      ResultType visit(Pending x);
      ResultType visit(Approved x);
      ResultType visit(Denied x);
   }

   public static abstract class VisitorWithDefault<ResultType> implements Visitor<ResultType> {
      @Override
      public ResultType visit(Pending x) { return getDefault(x); }

      @Override
      public ResultType visit(Approved x) { return getDefault(x); }

      @Override
      public ResultType visit(Denied x) { return getDefault(x); }

      protected abstract ResultType getDefault(TPSReportStatus x);
   }

   public static interface VoidVisitor {
      void visit(Pending x);
      void visit(Approved x);
      void visit(Denied x);
   }

   public static abstract class VoidVisitorWithDefault implements VoidVisitor {
      @Override
      public void visit(Pending x) { doDefault(x); }

      @Override
      public void visit(Approved x) { doDefault(x); }

      @Override
      public void visit(Denied x) { doDefault(x); }

      protected abstract void doDefault(TPSReportStatus x);
   }

   public static final class Pending extends TPSReportStatus {

      public Pending() {
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
         return "Pending";
      }

   }

   public static final class Approved extends TPSReportStatus {
      public final Manager approver;

      public Approved(Manager approver) {
         this.approver = approver;
      }

      @Override
      public <ResultType> ResultType accept(Visitor<ResultType> visitor) { return visitor.visit(this); }

      @Override
      public void accept(VoidVisitor visitor) { visitor.visit(this); }

      @Override
      public int hashCode() {
          final int prime = 31;
          int result = 1;
          result = prime * result + ((approver == null) ? 0 : approver.hashCode());
          return result;
      }

      @Override
      public boolean equals(Object obj) {
         if (this == obj) return true;
         if (obj == null) return false;
         if (getClass() != obj.getClass()) return false;
         Approved other = (Approved)obj;
         if (approver == null) {
            if (other.approver != null) return false;
         } else if (!approver.equals(other.approver)) return false;
         return true;
      }

      @Override
      public String toString() {
         return "Approved(approver = " + approver + ")";
      }

   }

   public static final class Denied extends TPSReportStatus {
      public final Manager rejector;

      public Denied(Manager rejector) {
         this.rejector = rejector;
      }

      @Override
      public <ResultType> ResultType accept(Visitor<ResultType> visitor) { return visitor.visit(this); }

      @Override
      public void accept(VoidVisitor visitor) { visitor.visit(this); }

      @Override
      public int hashCode() {
          final int prime = 31;
          int result = 1;
          result = prime * result + ((rejector == null) ? 0 : rejector.hashCode());
          return result;
      }

      @Override
      public boolean equals(Object obj) {
         if (this == obj) return true;
         if (obj == null) return false;
         if (getClass() != obj.getClass()) return false;
         Denied other = (Denied)obj;
         if (rejector == null) {
            if (other.rejector != null) return false;
         } else if (!rejector.equals(other.rejector)) return false;
         return true;
      }

      @Override
      public String toString() {
         return "Denied(rejector = " + rejector + ")";
      }

   }

   public abstract <ResultType> ResultType accept(Visitor<ResultType> visitor);

   public abstract void accept(VoidVisitor visitor);

}