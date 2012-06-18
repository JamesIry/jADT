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
public abstract class UserError {

   private UserError() {
   }

   public static final  UserError _Semantic(SemanticError error) { return new Semantic(error); }
   public static final  UserError _Syntactic(SyntaxError error) { return new Syntactic(error); }

   public static interface MatchBlock<ResultType> {
      ResultType _case(Semantic x);
      ResultType _case(Syntactic x);
   }

   public static abstract class MatchBlockWithDefault<ResultType> implements MatchBlock<ResultType> {
      @Override
      public ResultType _case(Semantic x) { return _default(x); }

      @Override
      public ResultType _case(Syntactic x) { return _default(x); }

      protected abstract ResultType _default(UserError x);
   }

   public static interface SwitchBlock {
      void _case(Semantic x);
      void _case(Syntactic x);
   }

   public static abstract class SwitchBlockWithDefault implements SwitchBlock {
      @Override
      public void _case(Semantic x) { _default(x); }

      @Override
      public void _case(Syntactic x) { _default(x); }

      protected abstract void _default(UserError x);
   }

   public static final class Semantic extends UserError {
      public SemanticError error;

      public Semantic(SemanticError error) {
         this.error = error;
      }

      @Override
      public <ResultType> ResultType match(MatchBlock<ResultType> matchBlock) { return matchBlock._case(this); }

      @Override
      public void _switch(SwitchBlock switchBlock) { switchBlock._case(this); }

      @Override
      public int hashCode() {
          final int prime = 31;
          int result = 1;
          result = prime * result + ((error == null) ? 0 : error.hashCode());
          return result;
      }

      @Override
      public boolean equals(Object obj) {
         if (this == obj) return true;
         if (obj == null) return false;
         if (getClass() != obj.getClass()) return false;
         Semantic other = (Semantic)obj;
         if (error == null) {
            if (other.error != null) return false;
         } else if (!error.equals(other.error)) return false;
         return true;
      }

      @Override
      public String toString() {
         return "Semantic(error = " + error + ")";
      }

   }

   public static final class Syntactic extends UserError {
      public SyntaxError error;

      public Syntactic(SyntaxError error) {
         this.error = error;
      }

      @Override
      public <ResultType> ResultType match(MatchBlock<ResultType> matchBlock) { return matchBlock._case(this); }

      @Override
      public void _switch(SwitchBlock switchBlock) { switchBlock._case(this); }

      @Override
      public int hashCode() {
          final int prime = 31;
          int result = 1;
          result = prime * result + ((error == null) ? 0 : error.hashCode());
          return result;
      }

      @Override
      public boolean equals(Object obj) {
         if (this == obj) return true;
         if (obj == null) return false;
         if (getClass() != obj.getClass()) return false;
         Syntactic other = (Syntactic)obj;
         if (error == null) {
            if (other.error != null) return false;
         } else if (!error.equals(other.error)) return false;
         return true;
      }

      @Override
      public String toString() {
         return "Syntactic(error = " + error + ")";
      }

   }

   public abstract <ResultType> ResultType match(MatchBlock<ResultType> matchBlock);

   public abstract void _switch(SwitchBlock switchBlock);

}