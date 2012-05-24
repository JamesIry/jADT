package pogofish.jadt.ast;


/*
This file was generated based on /home/jiry/workspace/JADT/src/main/jadt/jadt.jadt. Please do not modify directly.

The source was parsed as: 

package pogofish.jadt.ast

import java.util.List

Doc =
    Doc(String srcInfo, String pkg, List<String> imports, List<DataType> dataTypes)
DataType =
    DataType(String name, List<Constructor> constructors)
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

   public static PrimitiveType _BooleanType = new BooleanType();
   public static PrimitiveType _CharType = new CharType();
   public static PrimitiveType _DoubleType = new DoubleType();
   public static PrimitiveType _FloatType = new FloatType();
   public static PrimitiveType _IntType = new IntType();
   public static PrimitiveType _LongType = new LongType();
   public static PrimitiveType _ShortType = new ShortType();

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

      public abstract A getDefault(PrimitiveType x);
   }

   public static final class BooleanType extends PrimitiveType {

      public BooleanType() {
      }

      @Override
      public <A> A accept(Visitor<A> visitor) { return visitor.visit(this); }

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

}