package com.pogofish.jadt.ast;


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
public abstract class SemanticError {

   private SemanticError() {
   }

   public static final  SemanticError _DuplicateDataType(String dataTypeName) { return new DuplicateDataType(dataTypeName); }
   public static final  SemanticError _ConstructorDataTypeConflict(String dataTypeName) { return new ConstructorDataTypeConflict(dataTypeName); }
   public static final  SemanticError _DuplicateConstructor(String dataTypeName, String constructorName) { return new DuplicateConstructor(dataTypeName, constructorName); }
   public static final  SemanticError _DuplicateArgName(String dataTypeName, String constructorName, String argName) { return new DuplicateArgName(dataTypeName, constructorName, argName); }
   public static final  SemanticError _DuplicateModifier(String dataTypeName, String constructorName, String argName, String modifier) { return new DuplicateModifier(dataTypeName, constructorName, argName, modifier); }

   public static interface MatchBlock<ResultType> {
      ResultType _case(DuplicateDataType x);
      ResultType _case(ConstructorDataTypeConflict x);
      ResultType _case(DuplicateConstructor x);
      ResultType _case(DuplicateArgName x);
      ResultType _case(DuplicateModifier x);
   }

   public static abstract class MatchBlockWithDefault<ResultType> implements MatchBlock<ResultType> {
      @Override
      public ResultType _case(DuplicateDataType x) { return _default(x); }

      @Override
      public ResultType _case(ConstructorDataTypeConflict x) { return _default(x); }

      @Override
      public ResultType _case(DuplicateConstructor x) { return _default(x); }

      @Override
      public ResultType _case(DuplicateArgName x) { return _default(x); }

      @Override
      public ResultType _case(DuplicateModifier x) { return _default(x); }

      protected abstract ResultType _default(SemanticError x);
   }

   public static interface SwitchBlock {
      void _case(DuplicateDataType x);
      void _case(ConstructorDataTypeConflict x);
      void _case(DuplicateConstructor x);
      void _case(DuplicateArgName x);
      void _case(DuplicateModifier x);
   }

   public static abstract class SwitchBlockWithDefault implements SwitchBlock {
      @Override
      public void _case(DuplicateDataType x) { _default(x); }

      @Override
      public void _case(ConstructorDataTypeConflict x) { _default(x); }

      @Override
      public void _case(DuplicateConstructor x) { _default(x); }

      @Override
      public void _case(DuplicateArgName x) { _default(x); }

      @Override
      public void _case(DuplicateModifier x) { _default(x); }

      protected abstract void _default(SemanticError x);
   }

   public static final class DuplicateDataType extends SemanticError {
      public String dataTypeName;

      public DuplicateDataType(String dataTypeName) {
         this.dataTypeName = dataTypeName;
      }

      @Override
      public <ResultType> ResultType match(MatchBlock<ResultType> matchBlock) { return matchBlock._case(this); }

      @Override
      public void _switch(SwitchBlock switchBlock) { switchBlock._case(this); }

      @Override
      public int hashCode() {
          final int prime = 31;
          int result = 1;
          result = prime * result + ((dataTypeName == null) ? 0 : dataTypeName.hashCode());
          return result;
      }

      @Override
      public boolean equals(Object obj) {
         if (this == obj) return true;
         if (obj == null) return false;
         if (getClass() != obj.getClass()) return false;
         DuplicateDataType other = (DuplicateDataType)obj;
         if (dataTypeName == null) {
            if (other.dataTypeName != null) return false;
         } else if (!dataTypeName.equals(other.dataTypeName)) return false;
         return true;
      }

      @Override
      public String toString() {
         return "DuplicateDataType(dataTypeName = " + dataTypeName + ")";
      }

   }

   public static final class ConstructorDataTypeConflict extends SemanticError {
      public String dataTypeName;

      public ConstructorDataTypeConflict(String dataTypeName) {
         this.dataTypeName = dataTypeName;
      }

      @Override
      public <ResultType> ResultType match(MatchBlock<ResultType> matchBlock) { return matchBlock._case(this); }

      @Override
      public void _switch(SwitchBlock switchBlock) { switchBlock._case(this); }

      @Override
      public int hashCode() {
          final int prime = 31;
          int result = 1;
          result = prime * result + ((dataTypeName == null) ? 0 : dataTypeName.hashCode());
          return result;
      }

      @Override
      public boolean equals(Object obj) {
         if (this == obj) return true;
         if (obj == null) return false;
         if (getClass() != obj.getClass()) return false;
         ConstructorDataTypeConflict other = (ConstructorDataTypeConflict)obj;
         if (dataTypeName == null) {
            if (other.dataTypeName != null) return false;
         } else if (!dataTypeName.equals(other.dataTypeName)) return false;
         return true;
      }

      @Override
      public String toString() {
         return "ConstructorDataTypeConflict(dataTypeName = " + dataTypeName + ")";
      }

   }

   public static final class DuplicateConstructor extends SemanticError {
      public String dataTypeName;
      public String constructorName;

      public DuplicateConstructor(String dataTypeName, String constructorName) {
         this.dataTypeName = dataTypeName;
         this.constructorName = constructorName;
      }

      @Override
      public <ResultType> ResultType match(MatchBlock<ResultType> matchBlock) { return matchBlock._case(this); }

      @Override
      public void _switch(SwitchBlock switchBlock) { switchBlock._case(this); }

      @Override
      public int hashCode() {
          final int prime = 31;
          int result = 1;
          result = prime * result + ((dataTypeName == null) ? 0 : dataTypeName.hashCode());
          result = prime * result + ((constructorName == null) ? 0 : constructorName.hashCode());
          return result;
      }

      @Override
      public boolean equals(Object obj) {
         if (this == obj) return true;
         if (obj == null) return false;
         if (getClass() != obj.getClass()) return false;
         DuplicateConstructor other = (DuplicateConstructor)obj;
         if (dataTypeName == null) {
            if (other.dataTypeName != null) return false;
         } else if (!dataTypeName.equals(other.dataTypeName)) return false;
         if (constructorName == null) {
            if (other.constructorName != null) return false;
         } else if (!constructorName.equals(other.constructorName)) return false;
         return true;
      }

      @Override
      public String toString() {
         return "DuplicateConstructor(dataTypeName = " + dataTypeName + ", constructorName = " + constructorName + ")";
      }

   }

   public static final class DuplicateArgName extends SemanticError {
      public String dataTypeName;
      public String constructorName;
      public String argName;

      public DuplicateArgName(String dataTypeName, String constructorName, String argName) {
         this.dataTypeName = dataTypeName;
         this.constructorName = constructorName;
         this.argName = argName;
      }

      @Override
      public <ResultType> ResultType match(MatchBlock<ResultType> matchBlock) { return matchBlock._case(this); }

      @Override
      public void _switch(SwitchBlock switchBlock) { switchBlock._case(this); }

      @Override
      public int hashCode() {
          final int prime = 31;
          int result = 1;
          result = prime * result + ((dataTypeName == null) ? 0 : dataTypeName.hashCode());
          result = prime * result + ((constructorName == null) ? 0 : constructorName.hashCode());
          result = prime * result + ((argName == null) ? 0 : argName.hashCode());
          return result;
      }

      @Override
      public boolean equals(Object obj) {
         if (this == obj) return true;
         if (obj == null) return false;
         if (getClass() != obj.getClass()) return false;
         DuplicateArgName other = (DuplicateArgName)obj;
         if (dataTypeName == null) {
            if (other.dataTypeName != null) return false;
         } else if (!dataTypeName.equals(other.dataTypeName)) return false;
         if (constructorName == null) {
            if (other.constructorName != null) return false;
         } else if (!constructorName.equals(other.constructorName)) return false;
         if (argName == null) {
            if (other.argName != null) return false;
         } else if (!argName.equals(other.argName)) return false;
         return true;
      }

      @Override
      public String toString() {
         return "DuplicateArgName(dataTypeName = " + dataTypeName + ", constructorName = " + constructorName + ", argName = " + argName + ")";
      }

   }

   public static final class DuplicateModifier extends SemanticError {
      public String dataTypeName;
      public String constructorName;
      public String argName;
      public String modifier;

      public DuplicateModifier(String dataTypeName, String constructorName, String argName, String modifier) {
         this.dataTypeName = dataTypeName;
         this.constructorName = constructorName;
         this.argName = argName;
         this.modifier = modifier;
      }

      @Override
      public <ResultType> ResultType match(MatchBlock<ResultType> matchBlock) { return matchBlock._case(this); }

      @Override
      public void _switch(SwitchBlock switchBlock) { switchBlock._case(this); }

      @Override
      public int hashCode() {
          final int prime = 31;
          int result = 1;
          result = prime * result + ((dataTypeName == null) ? 0 : dataTypeName.hashCode());
          result = prime * result + ((constructorName == null) ? 0 : constructorName.hashCode());
          result = prime * result + ((argName == null) ? 0 : argName.hashCode());
          result = prime * result + ((modifier == null) ? 0 : modifier.hashCode());
          return result;
      }

      @Override
      public boolean equals(Object obj) {
         if (this == obj) return true;
         if (obj == null) return false;
         if (getClass() != obj.getClass()) return false;
         DuplicateModifier other = (DuplicateModifier)obj;
         if (dataTypeName == null) {
            if (other.dataTypeName != null) return false;
         } else if (!dataTypeName.equals(other.dataTypeName)) return false;
         if (constructorName == null) {
            if (other.constructorName != null) return false;
         } else if (!constructorName.equals(other.constructorName)) return false;
         if (argName == null) {
            if (other.argName != null) return false;
         } else if (!argName.equals(other.argName)) return false;
         if (modifier == null) {
            if (other.modifier != null) return false;
         } else if (!modifier.equals(other.modifier)) return false;
         return true;
      }

      @Override
      public String toString() {
         return "DuplicateModifier(dataTypeName = " + dataTypeName + ", constructorName = " + constructorName + ", argName = " + argName + ", modifier = " + modifier + ")";
      }

   }

   public abstract <ResultType> ResultType match(MatchBlock<ResultType> matchBlock);

   public abstract void _switch(SwitchBlock switchBlock);

}