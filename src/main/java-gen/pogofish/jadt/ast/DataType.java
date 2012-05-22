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
public final class DataType {

   public static final DataType _DataType(String name, List<Constructor> constructors) { return new DataType(name, constructors); }

      public final String name;
      public final List<Constructor> constructors;

      public DataType(String name, List<Constructor> constructors) {
         this.name = name;
         this.constructors = constructors;
      }

      @Override
      public int hashCode() {
          final int prime = 31;
          int result = 1;
          result = prime * result + ((name == null) ? 0 : name.hashCode());
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
         if (constructors == null) {
            if (other.constructors != null) return false;
         } else if (!constructors.equals(other.constructors)) return false;
         return true;
      }

      @Override
      public String toString() {
         return "DataType(name = " + name + ", constructors = " + constructors + ")";
      }

}