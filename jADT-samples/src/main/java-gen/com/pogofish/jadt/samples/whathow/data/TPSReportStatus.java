package com.pogofish.jadt.samples.whathow.data;

/*
This file was generated based on /Users/jiry/workspace/JADT/jADT-samples/src/main/jadt/WhatHowSamples.jadt using jADT version 0.2.0-SNAPSHOT http://jamesiry.github.com/jADT/ . Please do not modify directly.

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
  | Approved(final Manager approver)
  | Denied(final Manager rejector)

*/
public abstract class TPSReportStatus {

   private TPSReportStatus() {
   }

   private static final TPSReportStatus _Pending = new Pending();
   public static final  TPSReportStatus _Pending() { return _Pending; }
   public static final  TPSReportStatus _Approved(Manager approver) { return new Approved(approver); }
   public static final  TPSReportStatus _Denied(Manager rejector) { return new Denied(rejector); }

   public static interface MatchBlock<ResultType> {
      ResultType _case(Pending x);
      ResultType _case(Approved x);
      ResultType _case(Denied x);
   }

   public static abstract class MatchBlockWithDefault<ResultType> implements MatchBlock<ResultType> {
      @Override
      public ResultType _case(Pending x) { return _default(x); }

      @Override
      public ResultType _case(Approved x) { return _default(x); }

      @Override
      public ResultType _case(Denied x) { return _default(x); }

      protected abstract ResultType _default(TPSReportStatus x);
   }

   public static interface SwitchBlock {
      void _case(Pending x);
      void _case(Approved x);
      void _case(Denied x);
   }

   public static abstract class SwitchBlockWithDefault implements SwitchBlock {
      @Override
      public void _case(Pending x) { _default(x); }

      @Override
      public void _case(Approved x) { _default(x); }

      @Override
      public void _case(Denied x) { _default(x); }

      protected abstract void _default(TPSReportStatus x);
   }

   public static final class Pending extends TPSReportStatus {

      public Pending() {
      }

      @Override
      public <ResultType> ResultType match(MatchBlock<ResultType> matchBlock) { return matchBlock._case(this); }

      @Override
      public void _switch(SwitchBlock switchBlock) { switchBlock._case(this); }

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
      public <ResultType> ResultType match(MatchBlock<ResultType> matchBlock) { return matchBlock._case(this); }

      @Override
      public void _switch(SwitchBlock switchBlock) { switchBlock._case(this); }

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
      public <ResultType> ResultType match(MatchBlock<ResultType> matchBlock) { return matchBlock._case(this); }

      @Override
      public void _switch(SwitchBlock switchBlock) { switchBlock._case(this); }

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

   public abstract <ResultType> ResultType match(MatchBlock<ResultType> matchBlock);

   public abstract void _switch(SwitchBlock switchBlock);

}