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
public abstract class BinaryTree<T> {

   private BinaryTree() {
   }

   public static final <T> BinaryTree<T> _Node(T value, BinaryTree<T> left, BinaryTree<T> right) { return new Node<T>(value, left, right); }
   @SuppressWarnings("rawtypes")
   private static final BinaryTree _EmptyTree = new EmptyTree();
   @SuppressWarnings("unchecked")
   public static final <T> BinaryTree<T> _EmptyTree() { return _EmptyTree; }

   public static interface MatchBlock<T, ResultType> {
      ResultType _case(Node<T> x);
      ResultType _case(EmptyTree<T> x);
   }

   public static abstract class MatchBlockWithDefault<T, ResultType> implements MatchBlock<T, ResultType> {
      @Override
      public ResultType _case(Node<T> x) { return _default(x); }

      @Override
      public ResultType _case(EmptyTree<T> x) { return _default(x); }

      protected abstract ResultType _default(BinaryTree<T> x);
   }

   public static interface SwitchBlock<T> {
      void _case(Node<T> x);
      void _case(EmptyTree<T> x);
   }

   public static abstract class SwitchBlockWithDefault<T> implements SwitchBlock<T> {
      @Override
      public void _case(Node<T> x) { _default(x); }

      @Override
      public void _case(EmptyTree<T> x) { _default(x); }

      protected abstract void _default(BinaryTree<T> x);
   }

   public static final class Node<T> extends BinaryTree<T> {
      public T value;
      public BinaryTree<T> left;
      public BinaryTree<T> right;

      public Node(T value, BinaryTree<T> left, BinaryTree<T> right) {
         this.value = value;
         this.left = left;
         this.right = right;
      }

      @Override
      public <ResultType> ResultType match(MatchBlock<T, ResultType> matchBlock) { return matchBlock._case(this); }

      @Override
      public void _switch(SwitchBlock<T> switchBlock) { switchBlock._case(this); }

      @Override
      public int hashCode() {
          final int prime = 31;
          int result = 1;
          result = prime * result + ((value == null) ? 0 : value.hashCode());
          result = prime * result + ((left == null) ? 0 : left.hashCode());
          result = prime * result + ((right == null) ? 0 : right.hashCode());
          return result;
      }

      @Override
      public boolean equals(Object obj) {
         if (this == obj) return true;
         if (obj == null) return false;
         if (getClass() != obj.getClass()) return false;
         @SuppressWarnings("rawtypes")         Node other = (Node)obj;
         if (value == null) {
            if (other.value != null) return false;
         } else if (!value.equals(other.value)) return false;
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

   public static final class EmptyTree<T> extends BinaryTree<T> {

      public EmptyTree() {
      }

      @Override
      public <ResultType> ResultType match(MatchBlock<T, ResultType> matchBlock) { return matchBlock._case(this); }

      @Override
      public void _switch(SwitchBlock<T> switchBlock) { switchBlock._case(this); }

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

   public abstract <ResultType> ResultType match(MatchBlock<T, ResultType> matchBlock);

   public abstract void _switch(SwitchBlock<T> switchBlock);

}