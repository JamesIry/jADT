package com.pogofish.jadt.ast;


/*
This file was generated based on /Users/jiry/workspace/JADT/jADT-core/src/main/jadt/jadt.jadt using jADT version 0.2.0-SNAPSHOT http://jamesiry.github.com/jADT/ . Please do not modify directly.

The source was parsed as: 

package com.pogofish.jadt.ast

import java.util.List

ParseResult =
    ParseResult(Doc doc, List<SyntaxError> errors)
Doc =
    Doc(final String srcInfo, final String pkg, final List<String> imports, final List<DataType> dataTypes)
DataType =
    DataType(final String name, final List<String> typeArguments, final List<Constructor> constructors)
Constructor =
    Constructor(final String name, final List<Arg> args)
Arg =
    Arg(final List<ArgModifier> modifiers, final Type type, final String name)
ArgModifier =
    Final
Type =
    Ref(final RefType type)
  | Primitive(final PrimitiveType type)
RefType =
    ClassType(final String baseName, final List<RefType> typeArguments)
  | ArrayType(final Type heldType)
PrimitiveType =
    BooleanType
  | CharType
  | DoubleType
  | FloatType
  | IntType
  | LongType
  | ShortType
UserError =
    Semantic(SemanticError error)
  | Syntactic(SyntaxError error)
SyntaxError =
    UnexpectedToken(String expected, String found, int line)
SemanticError =
    DuplicateDataType(String dataTypeName)
  | ConstructorDataTypeConflict(String dataTypeName)
  | DuplicateConstructor(String dataTypeName, String constructorName)
  | DuplicateArgName(String dataTypeName, String constructorName, String argName)
  | DuplicateModifier(String dataTypeName, String constructorName, String argName, String modifier)

*/
public abstract class PrimitiveType {

   private PrimitiveType() {
   }

   private static final PrimitiveType _BooleanType = new BooleanType();
   public static final  PrimitiveType _BooleanType() { return _BooleanType; }
   private static final PrimitiveType _CharType = new CharType();
   public static final  PrimitiveType _CharType() { return _CharType; }
   private static final PrimitiveType _DoubleType = new DoubleType();
   public static final  PrimitiveType _DoubleType() { return _DoubleType; }
   private static final PrimitiveType _FloatType = new FloatType();
   public static final  PrimitiveType _FloatType() { return _FloatType; }
   private static final PrimitiveType _IntType = new IntType();
   public static final  PrimitiveType _IntType() { return _IntType; }
   private static final PrimitiveType _LongType = new LongType();
   public static final  PrimitiveType _LongType() { return _LongType; }
   private static final PrimitiveType _ShortType = new ShortType();
   public static final  PrimitiveType _ShortType() { return _ShortType; }

   public static interface MatchBlock<ResultType> {
      ResultType _case(BooleanType x);
      ResultType _case(CharType x);
      ResultType _case(DoubleType x);
      ResultType _case(FloatType x);
      ResultType _case(IntType x);
      ResultType _case(LongType x);
      ResultType _case(ShortType x);
   }

   public static abstract class MatchBlockWithDefault<ResultType> implements MatchBlock<ResultType> {
      @Override
      public ResultType _case(BooleanType x) { return _default(x); }

      @Override
      public ResultType _case(CharType x) { return _default(x); }

      @Override
      public ResultType _case(DoubleType x) { return _default(x); }

      @Override
      public ResultType _case(FloatType x) { return _default(x); }

      @Override
      public ResultType _case(IntType x) { return _default(x); }

      @Override
      public ResultType _case(LongType x) { return _default(x); }

      @Override
      public ResultType _case(ShortType x) { return _default(x); }

      protected abstract ResultType _default(PrimitiveType x);
   }

   public static interface SwitchBlock {
      void _case(BooleanType x);
      void _case(CharType x);
      void _case(DoubleType x);
      void _case(FloatType x);
      void _case(IntType x);
      void _case(LongType x);
      void _case(ShortType x);
   }

   public static abstract class SwitchBlockWithDefault implements SwitchBlock {
      @Override
      public void _case(BooleanType x) { _default(x); }

      @Override
      public void _case(CharType x) { _default(x); }

      @Override
      public void _case(DoubleType x) { _default(x); }

      @Override
      public void _case(FloatType x) { _default(x); }

      @Override
      public void _case(IntType x) { _default(x); }

      @Override
      public void _case(LongType x) { _default(x); }

      @Override
      public void _case(ShortType x) { _default(x); }

      protected abstract void _default(PrimitiveType x);
   }

   public static final class BooleanType extends PrimitiveType {

      public BooleanType() {
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
         return "BooleanType";
      }

   }

   public static final class CharType extends PrimitiveType {

      public CharType() {
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
         return "CharType";
      }

   }

   public static final class DoubleType extends PrimitiveType {

      public DoubleType() {
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
         return "DoubleType";
      }

   }

   public static final class FloatType extends PrimitiveType {

      public FloatType() {
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
         return "FloatType";
      }

   }

   public static final class IntType extends PrimitiveType {

      public IntType() {
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
         return "IntType";
      }

   }

   public static final class LongType extends PrimitiveType {

      public LongType() {
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
         return "LongType";
      }

   }

   public static final class ShortType extends PrimitiveType {

      public ShortType() {
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
         return "ShortType";
      }

   }

   public abstract <ResultType> ResultType match(MatchBlock<ResultType> matchBlock);

   public abstract void _switch(SwitchBlock switchBlock);

}