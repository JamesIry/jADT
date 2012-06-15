package com.pogofish.jadt.ast;


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
    Constructor(List<FieldOption> options, String name, List<Arg> args)
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
FieldOption =
    Final

*/
public final class FieldOption {

   private static final FieldOption _Final = new FieldOption();
   public static final  FieldOption _Final() { return _Final; }


      public FieldOption() {
      }

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
         return "FieldOption";
      }

}