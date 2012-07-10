/*
Copyright 2012 James Iry

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
 */
/*
 * Here's one sample comment.  It's a good spot for copyright and license info
 */
package com.pogofish.jadt.samples.comments.data;

/*
 * Imports can also have comments.  Mostly useful for the case when there is
 * no package declaration.
 */
import java.util.*;

/*
This file was generated based on /Users/jiry/workspace/JADT/jADT-samples/src/main/jadt/CommentSamples.jadt using jADT version 0.3.0-SNAPSHOT http://jamesiry.github.com/jADT/ . Please do not modify directly.

The source was parsed as: 

CommentStyle1 =
    Foo(int arg1, int arg2)
  | Bar
*/
/**
 * Here's one style of using JavaDoc
 */
public abstract class CommentStyle1 {

   private CommentStyle1() {
   }

   /**
    * A constructor case
    *
    * @param arg1 some argument
    * @param arg2 some other argument
    */
   public static final  CommentStyle1 _Foo(int arg1, int arg2) { return new Foo(arg1, arg2); }
   private static final CommentStyle1 _Bar = new Bar();
   /**
    * Another constructor case
    */
   public static final  CommentStyle1 _Bar() { return _Bar; }

   public static interface MatchBlock<ResultType> {
      ResultType _case(Foo x);
      ResultType _case(Bar x);
   }

   public static abstract class MatchBlockWithDefault<ResultType> implements MatchBlock<ResultType> {
      @Override
      public ResultType _case(Foo x) { return _default(x); }

      @Override
      public ResultType _case(Bar x) { return _default(x); }

      protected abstract ResultType _default(CommentStyle1 x);
   }

   public static interface SwitchBlock {
      void _case(Foo x);
      void _case(Bar x);
   }

   public static abstract class SwitchBlockWithDefault implements SwitchBlock {
      @Override
      public void _case(Foo x) { _default(x); }

      @Override
      public void _case(Bar x) { _default(x); }

      protected abstract void _default(CommentStyle1 x);
   }

   /**
    * A constructor case
    *
    * */
   public static final class Foo extends CommentStyle1 {
      public int arg1;
      public int arg2;

      /**
       * A constructor case
       *
       * @param arg1 some argument
       * @param arg2 some other argument
       */
      public Foo(int arg1, int arg2) {
         this.arg1 = arg1;
         this.arg2 = arg2;
      }

      @Override
      public <ResultType> ResultType match(MatchBlock<ResultType> matchBlock) { return matchBlock._case(this); }

      @Override
      public void _switch(SwitchBlock switchBlock) { switchBlock._case(this); }

      @Override
      public int hashCode() {
          final int prime = 31;
          int result = 1;
          result = prime * result + arg1;
          result = prime * result + arg2;
          return result;
      }

      @Override
      public boolean equals(Object obj) {
         if (this == obj) return true;
         if (obj == null) return false;
         if (getClass() != obj.getClass()) return false;
         Foo other = (Foo)obj;
         if (arg1 != other.arg1) return false;
         if (arg2 != other.arg2) return false;
         return true;
      }

      @Override
      public String toString() {
         return "Foo(arg1 = " + arg1 + ", arg2 = " + arg2 + ")";
      }

   }

   /**
    * Another constructor case
    */
   public static final class Bar extends CommentStyle1 {

      /**
       * Another constructor case
       */
      public Bar() {
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
         return "Bar";
      }

   }

   public abstract <ResultType> ResultType match(MatchBlock<ResultType> matchBlock);

   public abstract void _switch(SwitchBlock switchBlock);

}