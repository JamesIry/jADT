package com.pogofish.jadt.samples.ast.data;


/*
This file was generated based on /Users/jiry/workspace/JADT/jADT-samples/src/main/jadt/SampleAST.jadt using jADT version 0.2.0-SNAPSHOT http://jamesiry.github.com/jADT/ . Please do not modify directly.

The source was parsed as: 

package com.pogofish.jadt.samples.ast.data

import java.util.List

Type =
    Int
  | Long
Function =
    Function(final Type returnType, final String name, List<Arg> args, final List<Statement> statements)
Arg =
    Arg(final Type type, final String name)
Statement =
    Declaration(final Type type, final String name, final Expression expression)
  | Assignment(final String name, final Expression expression)
  | Return(final Expression expression)
Expression =
    Add(final Expression left, final Expression right)
  | Variable(final String name)
  | IntLiteral(final int value)
  | LongLiteral(final long value)

*/
public abstract class Expression {

   private Expression() {
   }

   public static final  Expression _Add(Expression left, Expression right) { return new Add(left, right); }
   public static final  Expression _Variable(String name) { return new Variable(name); }
   public static final  Expression _IntLiteral(int value) { return new IntLiteral(value); }
   public static final  Expression _LongLiteral(long value) { return new LongLiteral(value); }

   public static interface MatchBlock<ResultType> {
      ResultType _case(Add x);
      ResultType _case(Variable x);
      ResultType _case(IntLiteral x);
      ResultType _case(LongLiteral x);
   }

   public static abstract class MatchBlockWithDefault<ResultType> implements MatchBlock<ResultType> {
      @Override
      public ResultType _case(Add x) { return _default(x); }

      @Override
      public ResultType _case(Variable x) { return _default(x); }

      @Override
      public ResultType _case(IntLiteral x) { return _default(x); }

      @Override
      public ResultType _case(LongLiteral x) { return _default(x); }

      protected abstract ResultType _default(Expression x);
   }

   public static interface SwitchBlock {
      void _case(Add x);
      void _case(Variable x);
      void _case(IntLiteral x);
      void _case(LongLiteral x);
   }

   public static abstract class SwitchBlockWithDefault implements SwitchBlock {
      @Override
      public void _case(Add x) { _default(x); }

      @Override
      public void _case(Variable x) { _default(x); }

      @Override
      public void _case(IntLiteral x) { _default(x); }

      @Override
      public void _case(LongLiteral x) { _default(x); }

      protected abstract void _default(Expression x);
   }

   public static final class Add extends Expression {
      public final Expression left;
      public final Expression right;

      public Add(Expression left, Expression right) {
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
          result = prime * result + ((left == null) ? 0 : left.hashCode());
          result = prime * result + ((right == null) ? 0 : right.hashCode());
          return result;
      }

      @Override
      public boolean equals(Object obj) {
         if (this == obj) return true;
         if (obj == null) return false;
         if (getClass() != obj.getClass()) return false;
         Add other = (Add)obj;
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
         return "Add(left = " + left + ", right = " + right + ")";
      }

   }

   public static final class Variable extends Expression {
      public final String name;

      public Variable(String name) {
         this.name = name;
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
          return result;
      }

      @Override
      public boolean equals(Object obj) {
         if (this == obj) return true;
         if (obj == null) return false;
         if (getClass() != obj.getClass()) return false;
         Variable other = (Variable)obj;
         if (name == null) {
            if (other.name != null) return false;
         } else if (!name.equals(other.name)) return false;
         return true;
      }

      @Override
      public String toString() {
         return "Variable(name = " + name + ")";
      }

   }

   public static final class IntLiteral extends Expression {
      public final int value;

      public IntLiteral(int value) {
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
         IntLiteral other = (IntLiteral)obj;
         if (value != other.value) return false;
         return true;
      }

      @Override
      public String toString() {
         return "IntLiteral(value = " + value + ")";
      }

   }

   public static final class LongLiteral extends Expression {
      public final long value;

      public LongLiteral(long value) {
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
          result = prime * result + (int)value;
          return result;
      }

      @Override
      public boolean equals(Object obj) {
         if (this == obj) return true;
         if (obj == null) return false;
         if (getClass() != obj.getClass()) return false;
         LongLiteral other = (LongLiteral)obj;
         if (value != other.value) return false;
         return true;
      }

      @Override
      public String toString() {
         return "LongLiteral(value = " + value + ")";
      }

   }

   public abstract <ResultType> ResultType match(MatchBlock<ResultType> matchBlock);

   public abstract void _switch(SwitchBlock switchBlock);

}