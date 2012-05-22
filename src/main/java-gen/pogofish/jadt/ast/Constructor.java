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
public final class Constructor {

   public static final Constructor _Constructor(String name, List<Arg> args) { return new Constructor(name, args); }

      public final String name;
      public final List<Arg> args;

      public Constructor(String name, List<Arg> args) {
         this.name = name;
         this.args = args;
      }

      @Override
      public int hashCode() {
          final int prime = 31;
          int result = 1;
          result = prime * result + ((name == null) ? 0 : name.hashCode());
          result = prime * result + ((args == null) ? 0 : args.hashCode());
          return result;
      }

      @Override
      public boolean equals(Object obj) {
         if (this == obj) return true;
         if (obj == null) return false;
         if (getClass() != obj.getClass()) return false;
         Constructor other = (Constructor)obj;
         if (name == null) {
            if (other.name != null) return false;
         } else if (!name.equals(other.name)) return false;
         if (args == null) {
            if (other.args != null) return false;
         } else if (!args.equals(other.args)) return false;
         return true;
      }

      @Override
      public String toString() {
         return "Constructor(name = " + name + ", args = " + args + ")";
      }

}