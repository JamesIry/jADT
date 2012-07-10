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
 * This jADT file is an example AST for a toy language.
 * It is marked up with START SNIPPET and END SNIPPET boundaries 
 * to support /jADT/src/site/apt/index.apt
 */
package com.pogofish.jadt.samples.ast.data;

import java.util.List;

/*
This file was generated based on /Users/jiry/workspace/JADT/jADT-samples/src/main/jadt/SampleAST.jadt using jADT version 0.3.0-SNAPSHOT http://jamesiry.github.com/jADT/ . Please do not modify directly.

The source was parsed as: 

Statement =
    Declaration(final Type type, final String name, final Expression expression)
  | Assignment(final String name, final Expression expression)
  | Return(final Expression expression)
*/
public abstract class Statement {

   private Statement() {
   }

   public static final  Statement _Declaration(Type type, String name, Expression expression) { return new Declaration(type, name, expression); }
   public static final  Statement _Assignment(String name, Expression expression) { return new Assignment(name, expression); }
   public static final  Statement _Return(Expression expression) { return new Return(expression); }

   public static interface MatchBlock<ResultType> {
      ResultType _case(Declaration x);
      ResultType _case(Assignment x);
      ResultType _case(Return x);
   }

   public static abstract class MatchBlockWithDefault<ResultType> implements MatchBlock<ResultType> {
      @Override
      public ResultType _case(Declaration x) { return _default(x); }

      @Override
      public ResultType _case(Assignment x) { return _default(x); }

      @Override
      public ResultType _case(Return x) { return _default(x); }

      protected abstract ResultType _default(Statement x);
   }

   public static interface SwitchBlock {
      void _case(Declaration x);
      void _case(Assignment x);
      void _case(Return x);
   }

   public static abstract class SwitchBlockWithDefault implements SwitchBlock {
      @Override
      public void _case(Declaration x) { _default(x); }

      @Override
      public void _case(Assignment x) { _default(x); }

      @Override
      public void _case(Return x) { _default(x); }

      protected abstract void _default(Statement x);
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
      public <ResultType> ResultType match(MatchBlock<ResultType> matchBlock) { return matchBlock._case(this); }

      @Override
      public void _switch(SwitchBlock switchBlock) { switchBlock._case(this); }

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
      public <ResultType> ResultType match(MatchBlock<ResultType> matchBlock) { return matchBlock._case(this); }

      @Override
      public void _switch(SwitchBlock switchBlock) { switchBlock._case(this); }

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
      public <ResultType> ResultType match(MatchBlock<ResultType> matchBlock) { return matchBlock._case(this); }

      @Override
      public void _switch(SwitchBlock switchBlock) { switchBlock._case(this); }

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

   public abstract <ResultType> ResultType match(MatchBlock<ResultType> matchBlock);

   public abstract void _switch(SwitchBlock switchBlock);

}