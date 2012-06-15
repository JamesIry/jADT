package com.pogofish.jadt.ast;


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