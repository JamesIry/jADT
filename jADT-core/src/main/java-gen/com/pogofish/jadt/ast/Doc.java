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
public final class Doc {

   public static final  Doc _Doc(String srcInfo, String pkg, List<String> imports, List<DataType> dataTypes) { return new Doc(srcInfo, pkg, imports, dataTypes); }

      public final String srcInfo;
      public final String pkg;
      public final List<String> imports;
      public final List<DataType> dataTypes;

      public Doc(String srcInfo, String pkg, List<String> imports, List<DataType> dataTypes) {
         this.srcInfo = srcInfo;
         this.pkg = pkg;
         this.imports = imports;
         this.dataTypes = dataTypes;
      }

      @Override
      public int hashCode() {
          final int prime = 31;
          int result = 1;
          result = prime * result + ((srcInfo == null) ? 0 : srcInfo.hashCode());
          result = prime * result + ((pkg == null) ? 0 : pkg.hashCode());
          result = prime * result + ((imports == null) ? 0 : imports.hashCode());
          result = prime * result + ((dataTypes == null) ? 0 : dataTypes.hashCode());
          return result;
      }

      @Override
      public boolean equals(Object obj) {
         if (this == obj) return true;
         if (obj == null) return false;
         if (getClass() != obj.getClass()) return false;
         Doc other = (Doc)obj;
         if (srcInfo == null) {
            if (other.srcInfo != null) return false;
         } else if (!srcInfo.equals(other.srcInfo)) return false;
         if (pkg == null) {
            if (other.pkg != null) return false;
         } else if (!pkg.equals(other.pkg)) return false;
         if (imports == null) {
            if (other.imports != null) return false;
         } else if (!imports.equals(other.imports)) return false;
         if (dataTypes == null) {
            if (other.dataTypes != null) return false;
         } else if (!dataTypes.equals(other.dataTypes)) return false;
         return true;
      }

      @Override
      public String toString() {
         return "Doc(srcInfo = " + srcInfo + ", pkg = " + pkg + ", imports = " + imports + ", dataTypes = " + dataTypes + ")";
      }

}