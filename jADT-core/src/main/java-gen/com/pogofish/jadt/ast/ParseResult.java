package com.pogofish.jadt.ast;

import java.util.Set;

/*
This file was generated based on /Users/jiry/workspace/JADT/jADT-core/src/main/jadt/jadt.jadt using jADT version 0.2.0-SNAPSHOT http://jamesiry.github.com/jADT/ . Please do not modify directly.

The source was parsed as: 

package com.pogofish.jadt.ast

import java.util.List
import java.util.Set

ParseResult =
    Success(Doc doc)
  | Errors(Set<SyntaxError> errors)
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
public abstract class ParseResult {

   private ParseResult() {
   }

   public static final  ParseResult _Success(Doc doc) { return new Success(doc); }
   public static final  ParseResult _Errors(Set<SyntaxError> errors) { return new Errors(errors); }

   public static interface MatchBlock<ResultType> {
      ResultType _case(Success x);
      ResultType _case(Errors x);
   }

   public static abstract class MatchBlockWithDefault<ResultType> implements MatchBlock<ResultType> {
      @Override
      public ResultType _case(Success x) { return _default(x); }

      @Override
      public ResultType _case(Errors x) { return _default(x); }

      protected abstract ResultType _default(ParseResult x);
   }

   public static interface SwitchBlock {
      void _case(Success x);
      void _case(Errors x);
   }

   public static abstract class SwitchBlockWithDefault implements SwitchBlock {
      @Override
      public void _case(Success x) { _default(x); }

      @Override
      public void _case(Errors x) { _default(x); }

      protected abstract void _default(ParseResult x);
   }

   public static final class Success extends ParseResult {
      public Doc doc;

      public Success(Doc doc) {
         this.doc = doc;
      }

      @Override
      public <ResultType> ResultType match(MatchBlock<ResultType> matchBlock) { return matchBlock._case(this); }

      @Override
      public void _switch(SwitchBlock switchBlock) { switchBlock._case(this); }

      @Override
      public int hashCode() {
          final int prime = 31;
          int result = 1;
          result = prime * result + ((doc == null) ? 0 : doc.hashCode());
          return result;
      }

      @Override
      public boolean equals(Object obj) {
         if (this == obj) return true;
         if (obj == null) return false;
         if (getClass() != obj.getClass()) return false;
         Success other = (Success)obj;
         if (doc == null) {
            if (other.doc != null) return false;
         } else if (!doc.equals(other.doc)) return false;
         return true;
      }

      @Override
      public String toString() {
         return "Success(doc = " + doc + ")";
      }

   }

   public static final class Errors extends ParseResult {
      public Set<SyntaxError> errors;

      public Errors(Set<SyntaxError> errors) {
         this.errors = errors;
      }

      @Override
      public <ResultType> ResultType match(MatchBlock<ResultType> matchBlock) { return matchBlock._case(this); }

      @Override
      public void _switch(SwitchBlock switchBlock) { switchBlock._case(this); }

      @Override
      public int hashCode() {
          final int prime = 31;
          int result = 1;
          result = prime * result + ((errors == null) ? 0 : errors.hashCode());
          return result;
      }

      @Override
      public boolean equals(Object obj) {
         if (this == obj) return true;
         if (obj == null) return false;
         if (getClass() != obj.getClass()) return false;
         Errors other = (Errors)obj;
         if (errors == null) {
            if (other.errors != null) return false;
         } else if (!errors.equals(other.errors)) return false;
         return true;
      }

      @Override
      public String toString() {
         return "Errors(errors = " + errors + ")";
      }

   }

   public abstract <ResultType> ResultType match(MatchBlock<ResultType> matchBlock);

   public abstract void _switch(SwitchBlock switchBlock);

}