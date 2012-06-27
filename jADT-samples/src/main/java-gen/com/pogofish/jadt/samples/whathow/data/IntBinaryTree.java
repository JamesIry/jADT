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
public abstract class IntBinaryTree {

   private IntBinaryTree() {
   }

   public static final  IntBinaryTree _Node(int value, IntBinaryTree left, IntBinaryTree right) { return new Node(value, left, right); }
   private static final IntBinaryTree _EmptyTree = new EmptyTree();
   public static final  IntBinaryTree _EmptyTree() { return _EmptyTree; }

   public static interface MatchBlock<ResultType> {
      ResultType _case(Node x);
      ResultType _case(EmptyTree x);
   }

   public static abstract class MatchBlockWithDefault<ResultType> implements MatchBlock<ResultType> {
      @Override
      public ResultType _case(Node x) { return _default(x); }

      @Override
      public ResultType _case(EmptyTree x) { return _default(x); }

      protected abstract ResultType _default(IntBinaryTree x);
   }

   public static interface SwitchBlock {
      void _case(Node x);
      void _case(EmptyTree x);
   }

   public static abstract class SwitchBlockWithDefault implements SwitchBlock {
      @Override
      public void _case(Node x) { _default(x); }

      @Override
      public void _case(EmptyTree x) { _default(x); }

      protected abstract void _default(IntBinaryTree x);
   }

   public static final class Node extends IntBinaryTree {
      public int value;
      public IntBinaryTree left;
      public IntBinaryTree right;

      public Node(int value, IntBinaryTree left, IntBinaryTree right) {
         this.value = value;
         this.left = left;
         this.right = right;
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
          result = prime * result + ((left == null) ? 0 : left.hashCode());
          result = prime * result + ((right == null) ? 0 : right.hashCode());
          return result;
      }

      @Override
      public boolean equals(Object obj) {
         if (this == obj) return true;
         if (obj == null) return false;
         if (getClass() != obj.getClass()) return false;
         Node other = (Node)obj;
         if (value != other.value) return false;
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
         return "Node(value = " + value + ", left = " + left + ", right = " + right + ")";
      }

   }

   public static final class EmptyTree extends IntBinaryTree {

      public EmptyTree() {
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
         return "EmptyTree";
      }

   }

   public abstract <ResultType> ResultType match(MatchBlock<ResultType> matchBlock);

   public abstract void _switch(SwitchBlock switchBlock);

}