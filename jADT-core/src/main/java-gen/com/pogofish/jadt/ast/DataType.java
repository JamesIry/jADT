package com.pogofish.jadt.ast;

import java.util.List;

/*
This file was generated based on /Users/jiry/workspace/JADT/jADT-core/src/main/jadt/jadt.jadt using jADT version ${pom.version} http://jamesiry.github.com/jADT/ . Please do not modify directly.

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
public final class DataType {

   public static final  DataType _DataType(String name, List<String> typeArguments, List<Constructor> constructors) { return new DataType(name, typeArguments, constructors); }

      public final String name;
      public final List<String> typeArguments;
      public final List<Constructor> constructors;

      public DataType(String name, List<String> typeArguments, List<Constructor> constructors) {
         this.name = name;
         this.typeArguments = typeArguments;
         this.constructors = constructors;
      }

      @Override
      public int hashCode() {
          final int prime = 31;
          int result = 1;
          result = prime * result + ((name == null) ? 0 : name.hashCode());
          result = prime * result + ((typeArguments == null) ? 0 : typeArguments.hashCode());
          result = prime * result + ((constructors == null) ? 0 : constructors.hashCode());
          return result;
      }

      @Override
      public boolean equals(Object obj) {
         if (this == obj) return true;
         if (obj == null) return false;
         if (getClass() != obj.getClass()) return false;
         DataType other = (DataType)obj;
         if (name == null) {
            if (other.name != null) return false;
         } else if (!name.equals(other.name)) return false;
         if (typeArguments == null) {
            if (other.typeArguments != null) return false;
         } else if (!typeArguments.equals(other.typeArguments)) return false;
         if (constructors == null) {
            if (other.constructors != null) return false;
         } else if (!constructors.equals(other.constructors)) return false;
         return true;
      }

      @Override
      public String toString() {
         return "DataType(name = " + name + ", typeArguments = " + typeArguments + ", constructors = " + constructors + ")";
      }

}