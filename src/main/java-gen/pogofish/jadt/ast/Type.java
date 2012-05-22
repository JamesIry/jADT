package pogofish.jadt.ast;

import java.util.List;

/*
This file was generated based on /home/jiry/workspace/JADT/src/main/jadt/jadt.jadt. Please do not modify directly.

The source was parsed as: 

package pogofish.jadt.ast

import java.util.List

data Doc =
    Doc(String srcInfo, String pkg, List<String> imports, List<DataType> dataTypes)
data DataType =
    DataType(String name, List<Constructor> constructors)
data Constructor =
    Constructor(String name, List<Arg> args)
data Arg =
    Arg(Type type, String name)
data Type =
    Ref(RefType type)
  | Primitive(PrimitiveType type)
data RefType =
    ClassType(String baseName, List<RefType> typeArguments)
  | ArrayType(Type heldType)
data PrimitiveType =
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

   public static final Type _Ref(RefType type) { return new Ref(type); }
   public static final Type _Primitive(PrimitiveType type) { return new Primitive(type); }

   public static interface Visitor<A> {
      A visit(Ref x);
      A visit(Primitive x);
   }

   public static abstract class VisitorWithDefault<A> implements Visitor<A> {
      @Override
      public A visit(Ref x) { return getDefault(x); }

      @Override
      public A visit(Primitive x) { return getDefault(x); }

      public abstract A getDefault(Type x);
   }

   public static final class Ref extends Type {
      public final RefType type;

      public Ref(RefType type) {
         this.type = type;
      }

      @Override
      public <A> A accept(Visitor<A> visitor) { return visitor.visit(this); }

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
      public <A> A accept(Visitor<A> visitor) { return visitor.visit(this); }

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

   public abstract <A> A accept(Visitor<A> visitor);

}