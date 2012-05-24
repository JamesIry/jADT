package pogofish.jadt.ast;

import java.util.List;

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
public abstract class RefType {

   private RefType() {
   }

   public static final RefType _ClassType(String baseName, List<RefType> typeArguments) { return new ClassType(baseName, typeArguments); }
   public static final RefType _ArrayType(Type heldType) { return new ArrayType(heldType); }

   public static interface Visitor<A> {
      A visit(ClassType x);
      A visit(ArrayType x);
   }

   public static abstract class VisitorWithDefault<A> implements Visitor<A> {
      @Override
      public A visit(ClassType x) { return getDefault(x); }

      @Override
      public A visit(ArrayType x) { return getDefault(x); }

      public abstract A getDefault(RefType x);
   }

   public static final class ClassType extends RefType {
      public final String baseName;
      public final List<RefType> typeArguments;

      public ClassType(String baseName, List<RefType> typeArguments) {
         this.baseName = baseName;
         this.typeArguments = typeArguments;
      }

      @Override
      public <A> A accept(Visitor<A> visitor) { return visitor.visit(this); }

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
      public <A> A accept(Visitor<A> visitor) { return visitor.visit(this); }

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

   public abstract <A> A accept(Visitor<A> visitor);

}