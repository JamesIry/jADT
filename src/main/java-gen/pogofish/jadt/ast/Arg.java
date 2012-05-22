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
    Arg(String type, String name)

*/
public final class Arg {

   public static final Arg _Arg(String type, String name) { return new Arg(type, name); }

      public final String type;
      public final String name;

      public Arg(String type, String name) {
         this.type = type;
         this.name = name;
      }

      @Override
      public int hashCode() {
          final int prime = 31;
          int result = 1;
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
         return "Arg(type = " + type + ", name = " + name + ")";
      }

}