package com.pogofish.jadt.ast;


/*
This file was generated based on /Users/jiry/workspace/JADT/JADT-core/src/main/jadt/jadt.jadt using JADT version 0.0.2-SNAPSHOT http://jamesiry.github.com/JADT/ . Please do not modify directly.

The source was parsed as: 

package com.pogofish.jadt.ast

import java.util.List

Doc =
    Doc(String srcInfo, String pkg, List<String> imports, List<DataType> dataTypes)
DataType =
    DataType(String name, List<String> typeArguments, List<Constructor> constructors)
Constructor =
    Constructor(String name, List<Arg> args)
Arg =
    Arg(Type type, String name)
Type =
    Ref(RefType type)
  | Primitive(PrimitiveType type)
RefType =
    ClassType(String baseName, List<RefType> typeArguments)
  | ArrayType(Type heldType)
PrimitiveType =
    BooleanType
  | CharType
  | DoubleType
  | FloatType
  | IntType
  | LongType
  | ShortType

*/
public abstract class Type {

   private Type() {
   }

   public static final  Type _Ref(RefType type) { return new Ref(type); }
   public static final  Type _Primitive(PrimitiveType type) { return new Primitive(type); }

   public static interface Visitor<ResultType> {
      ResultType visit(Ref x);
      ResultType visit(Primitive x);
   }

   public static abstract class VisitorWithDefault<ResultType> implements Visitor<ResultType> {
      @Override
      public ResultType visit(Ref x) { return getDefault(x); }

      @Override
      public ResultType visit(Primitive x) { return getDefault(x); }

      protected abstract ResultType getDefault(Type x);
   }

   public static interface VoidVisitor {
      void visit(Ref x);
      void visit(Primitive x);
   }

   public static abstract class VoidVisitorWithDefault implements VoidVisitor {
      @Override
      public void visit(Ref x) { doDefault(x); }

      @Override
      public void visit(Primitive x) { doDefault(x); }

      protected abstract void doDefault(Type x);
   }

   public static final class Ref extends Type {
      public final RefType type;

      public Ref(RefType type) {
         this.type = type;
      }

      @Override
      public <ResultType> ResultType accept(Visitor<ResultType> visitor) { return visitor.visit(this); }

      @Override
      public void accept(VoidVisitor visitor) { visitor.visit(this); }

      @Override
      public int hashCode() {
          final int prime = 31;
          int result = 1;
          result = prime * result + ((type == null) ? 0 : type.hashCode());
          return result;
      }

      @Override
      public boolean equals(Object obj) {
         if (this == obj) return true;
         if (obj == null) return false;
         if (getClass() != obj.getClass()) return false;
         Ref other = (Ref)obj;
         if (type == null) {
            if (other.type != null) return false;
         } else if (!type.equals(other.type)) return false;
         return true;
      }

      @Override
      public String toString() {
         return "Ref(type = " + type + ")";
      }

   }

   public static final class Primitive extends Type {
      public final PrimitiveType type;

      public Primitive(PrimitiveType type) {
         this.type = type;
      }

      @Override
      public <ResultType> ResultType accept(Visitor<ResultType> visitor) { return visitor.visit(this); }

      @Override
      public void accept(VoidVisitor visitor) { visitor.visit(this); }

      @Override
      public int hashCode() {
          final int prime = 31;
          int result = 1;
          result = prime * result + ((type == null) ? 0 : type.hashCode());
          return result;
      }

      @Override
      public boolean equals(Object obj) {
         if (this == obj) return true;
         if (obj == null) return false;
         if (getClass() != obj.getClass()) return false;
         Primitive other = (Primitive)obj;
         if (type == null) {
            if (other.type != null) return false;
         } else if (!type.equals(other.type)) return false;
         return true;
      }

      @Override
      public String toString() {
         return "Primitive(type = " + type + ")";
      }

   }

   public abstract <ResultType> ResultType accept(Visitor<ResultType> visitor);

   public abstract void accept(VoidVisitor visitor);

}