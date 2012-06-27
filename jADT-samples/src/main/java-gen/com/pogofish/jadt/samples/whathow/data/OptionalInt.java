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
public abstract class OptionalInt {

   private OptionalInt() {
   }

   public static final  OptionalInt _Some(int value) { return new Some(value); }
   private static final OptionalInt _None = new None();
   public static final  OptionalInt _None() { return _None; }

   public static interface MatchBlock<ResultType> {
      ResultType _case(Some x);
      ResultType _case(None x);
   }

   public static abstract class MatchBlockWithDefault<ResultType> implements MatchBlock<ResultType> {
      @Override
      public ResultType _case(Some x) { return _default(x); }

      @Override
      public ResultType _case(None x) { return _default(x); }

      protected abstract ResultType _default(OptionalInt x);
   }

   public static interface SwitchBlock {
      void _case(Some x);
      void _case(None x);
   }

   public static abstract class SwitchBlockWithDefault implements SwitchBlock {
      @Override
      public void _case(Some x) { _default(x); }

      @Override
      public void _case(None x) { _default(x); }

      protected abstract void _default(OptionalInt x);
   }

   public static final class Some extends OptionalInt {
      public int value;

      public Some(int value) {
         this.value = value;
      }

      @Override
      public <ResultType> ResultType match(MatchBlock<ResultType> matchBlock) { return matchBlock._case(this); }

      @Override
      public void _switch(SwitchBlock switchBlock) { switchBlock._case(this); }

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
         return "None";
      }

   }

   public abstract <ResultType> ResultType match(MatchBlock<ResultType> matchBlock);

   public abstract void _switch(SwitchBlock switchBlock);

}