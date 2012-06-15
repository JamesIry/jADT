package com.pogofish.jadt.samples.whathow.data;

/*
This file was generated based on /Users/jiry/workspace/JADT/jADT-samples/src/main/jadt/WhatHowSamples.jadt using jADT version ${pom.version} http://jamesiry.github.com/jADT/ . Please do not modify directly.

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
public final class Manager {

   public static final  Manager _Manager(String name) { return new Manager(name); }

      public String name;

      public Manager(String name) {
         this.name = name;
      }

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
         Manager other = (Manager)obj;
         if (name == null) {
            if (other.name != null) return false;
         } else if (!name.equals(other.name)) return false;
         return true;
      }

      @Override
      public String toString() {
         return "Manager(name = " + name + ")";
      }

}