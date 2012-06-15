package com.pogofish.jadt.ast;

import java.util.List;

/*
This file was generated based on /Users/jiry/workspace/JADT/jADT-core/src/main/jadt/jadt.jadt using jADT version ${pom.version} http://jamesiry.github.com/jADT/ . Please do not modify directly.

The source was parsed as: 

package com.pogofish.jadt.ast

import java.util.List

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

*/
public abstract class RefType {

   private RefType() {
   }

   public static final  RefType _ClassType(String baseName, List<RefType> typeArguments) { return new ClassType(baseName, typeArguments); }
   public static final  RefType _ArrayType(Type heldType) { return new ArrayType(heldType); }

   public static interface Visitor<ResultType> {
      ResultType visit(ClassType x);
      ResultType visit(ArrayType x);
   }

   public static abstract class VisitorWithDefault<ResultType> implements Visitor<ResultType> {
      @Override
      public ResultType visit(ClassType x) { return getDefault(x); }

      @Override
      public ResultType visit(ArrayType x) { return getDefault(x); }

      protected abstract ResultType getDefault(RefType x);
   }

   public static interface VoidVisitor {
      void visit(ClassType x);
      void visit(ArrayType x);
   }

   public static abstract class VoidVisitorWithDefault implements VoidVisitor {
      @Override
      public void visit(ClassType x) { doDefault(x); }

      @Override
      public void visit(ArrayType x) { doDefault(x); }

      protected abstract void doDefault(RefType x);
   }

   public static final class ClassType extends RefType {
      public final String baseName;
      public final List<RefType> typeArguments;

      public ClassType(String baseName, List<RefType> typeArguments) {
         this.baseName = baseName;
         this.typeArguments = typeArguments;
      }

      @Override
      public <ResultType> ResultType accept(Visitor<ResultType> visitor) { return visitor.visit(this); }

      @Override
      public void accept(VoidVisitor visitor) { visitor.visit(this); }

      @Override
      public int hashCode() {
          final int prime = 31;
          int result = 1;
          result = prime * result + ((baseName == null) ? 0 : baseName.hashCode());
          result = prime * result + ((typeArguments == null) ? 0 : typeArguments.hashCode());
          return result;
      }

      @Override
      public boolean equals(Object obj) {
         if (this == obj) return true;
         if (obj == null) return false;
         if (getClass() != obj.getClass()) return false;
         ClassType other = (ClassType)obj;
         if (baseName == null) {
            if (other.baseName != null) return false;
         } else if (!baseName.equals(other.baseName)) return false;
         if (typeArguments == null) {
            if (other.typeArguments != null) return false;
         } else if (!typeArguments.equals(other.typeArguments)) return false;
         return true;
      }

      @Override
      public String toString() {
         return "ClassType(baseName = " + baseName + ", typeArguments = " + typeArguments + ")";
      }

   }

   public static final class ArrayType extends RefType {
      public final Type heldType;

      public ArrayType(Type heldType) {
         this.heldType = heldType;
      }

      @Override
      public <ResultType> ResultType accept(Visitor<ResultType> visitor) { return visitor.visit(this); }

      @Override
      public void accept(VoidVisitor visitor) { visitor.visit(this); }

      @Override
      public int hashCode() {
          final int prime = 31;
          int result = 1;
          result = prime * result + ((heldType == null) ? 0 : heldType.hashCode());
          return result;
      }

      @Override
      public boolean equals(Object obj) {
         if (this == obj) return true;
         if (obj == null) return false;
         if (getClass() != obj.getClass()) return false;
         ArrayType other = (ArrayType)obj;
         if (heldType == null) {
            if (other.heldType != null) return false;
         } else if (!heldType.equals(other.heldType)) return false;
         return true;
      }

      @Override
      public String toString() {
         return "ArrayType(heldType = " + heldType + ")";
      }

   }

   public abstract <ResultType> ResultType accept(Visitor<ResultType> visitor);

   public abstract void accept(VoidVisitor visitor);

}