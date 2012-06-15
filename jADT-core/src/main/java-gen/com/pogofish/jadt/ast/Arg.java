package com.pogofish.jadt.ast;

import java.util.List;

/*
This file was generated based on /Users/jiry/workspace/JADT/jADT-core/src/main/jadt/jadt.jadt using jADT version 0.2.0-SNAPSHOT http://jamesiry.github.com/jADT/ . Please do not modify directly.

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
    Arg(List<ArgModifier> modifiers, Type type, String name)
ArgModifier =
    Final
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
public final class Arg {

   public static final  Arg _Arg(List<ArgModifier> modifiers, Type type, String name) { return new Arg(modifiers, type, name); }

      public final List<ArgModifier> modifiers;
      public final Type type;
      public final String name;

      public Arg(List<ArgModifier> modifiers, Type type, String name) {
         this.modifiers = modifiers;
         this.type = type;
         this.name = name;
      }

      @Override
      public int hashCode() {
          final int prime = 31;
          int result = 1;
          result = prime * result + ((modifiers == null) ? 0 : modifiers.hashCode());
          result = prime * result + ((type == null) ? 0 : type.hashCode());
          result = prime * result + ((name == null) ? 0 : name.hashCode());
          return result;
      }

      @Override
      public boolean equals(Object obj) {
         if (this == obj) return true;
         if (obj == null) return false;
         if (getClass() != obj.getClass()) return false;
         Arg other = (Arg)obj;
         if (modifiers == null) {
            if (other.modifiers != null) return false;
         } else if (!modifiers.equals(other.modifiers)) return false;
         if (type == null) {
            if (other.type != null) return false;
         } else if (!type.equals(other.type)) return false;
         if (name == null) {
            if (other.name != null) return false;
         } else if (!name.equals(other.name)) return false;
         return true;
      }

      @Override
      public String toString() {
         return "Arg(modifiers = " + modifiers + ", type = " + type + ", name = " + name + ")";
      }

}