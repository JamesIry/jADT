package pogofish.jadt.ast;


/*
This file was generated based on /Users/jiry/workspace/JADTGeneric/JADT-core/src/main/jadt/jadt.jadt. Please do not modify directly.

The source was parsed as: 

package pogofish.jadt.ast

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
public abstract class PrimitiveType {

   private PrimitiveType() {
   }

   public static final PrimitiveType _BooleanType = new BooleanType();
   public static final PrimitiveType _CharType = new CharType();
   public static final PrimitiveType _DoubleType = new DoubleType();
   public static final PrimitiveType _FloatType = new FloatType();
   public static final PrimitiveType _IntType = new IntType();
   public static final PrimitiveType _LongType = new LongType();
   public static final PrimitiveType _ShortType = new ShortType();

   public static interface Visitor<A> {
      A visit(BooleanType x);
      A visit(CharType x);
      A visit(DoubleType x);
      A visit(FloatType x);
      A visit(IntType x);
      A visit(LongType x);
      A visit(ShortType x);
   }

   public static abstract class VisitorWithDefault<A> implements Visitor<A> {
      @Override
      public A visit(BooleanType x) { return getDefault(x); }

      @Override
      public A visit(CharType x) { return getDefault(x); }

      @Override
      public A visit(DoubleType x) { return getDefault(x); }

      @Override
      public A visit(FloatType x) { return getDefault(x); }

      @Override
      public A visit(IntType x) { return getDefault(x); }

      @Override
      public A visit(LongType x) { return getDefault(x); }

      @Override
      public A visit(ShortType x) { return getDefault(x); }

      protected abstract A getDefault(PrimitiveType x);
   }

   public static interface VoidVisitor {
      void visit(BooleanType x);
      void visit(CharType x);
      void visit(DoubleType x);
      void visit(FloatType x);
      void visit(IntType x);
      void visit(LongType x);
      void visit(ShortType x);
   }

   public static abstract class VoidVisitorWithDefault implements VoidVisitor {
      @Override
      public void visit(BooleanType x) { doDefault(x); }

      @Override
      public void visit(CharType x) { doDefault(x); }

      @Override
      public void visit(DoubleType x) { doDefault(x); }

      @Override
      public void visit(FloatType x) { doDefault(x); }

      @Override
      public void visit(IntType x) { doDefault(x); }

      @Override
      public void visit(LongType x) { doDefault(x); }

      @Override
      public void visit(ShortType x) { doDefault(x); }

      protected abstract void doDefault(PrimitiveType x);
   }

   public static final class BooleanType extends PrimitiveType {

      public BooleanType() {
      }

      @Override
      public <A> A accept(Visitor<A> visitor) { return visitor.visit(this); }

      @Override
      public void accept(VoidVisitor visitor) { visitor.visit(this); }

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
      public <A> A accept(Visitor<A> visitor) { return visitor.visit(this); }

      @Override
      public void accept(VoidVisitor visitor) { visitor.visit(this); }

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
      public <A> A accept(Visitor<A> visitor) { return visitor.visit(this); }

      @Override
      public void accept(VoidVisitor visitor) { visitor.visit(this); }

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
      public <A> A accept(Visitor<A> visitor) { return visitor.visit(this); }

      @Override
      public void accept(VoidVisitor visitor) { visitor.visit(this); }

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
      public <A> A accept(Visitor<A> visitor) { return visitor.visit(this); }

      @Override
      public void accept(VoidVisitor visitor) { visitor.visit(this); }

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
      public <A> A accept(Visitor<A> visitor) { return visitor.visit(this); }

      @Override
      public void accept(VoidVisitor visitor) { visitor.visit(this); }

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
      public <A> A accept(Visitor<A> visitor) { return visitor.visit(this); }

      @Override
      public void accept(VoidVisitor visitor) { visitor.visit(this); }

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

   public abstract <A> A accept(Visitor<A> visitor);

   public abstract void accept(VoidVisitor visitor);

}