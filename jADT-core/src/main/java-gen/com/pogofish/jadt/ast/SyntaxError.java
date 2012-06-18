package com.pogofish.jadt.ast;


/*
This file was generated based on /Users/jiry/workspace/JADT/jADT-core/src/main/jadt/jadt.jadt using jADT version 0.2.0-SNAPSHOT http://jamesiry.github.com/jADT/ . Please do not modify directly.

The source was parsed as: 

package com.pogofish.jadt.ast

import java.util.List

ParseResult =
    ParseResult(Doc doc, List<SyntaxError> errors)
Doc =
    Doc(final String srcInfo, final String pkg, final List<String> imports, final List<DataType> dataTypes)
DataType =
    DataType(final String name, final List<String> typeArguments, final List<Constructor> constructors)
Constructor =
    Constructor(final String name, final List<Arg> args)
Arg =
    Arg(final List<ArgModifier> modifiers, final Type type, final String name)
ArgModifier =
    Final
Type =
    Ref(final RefType type)
  | Primitive(final PrimitiveType type)
RefType =
    ClassType(final String baseName, final List<RefType> typeArguments)
  | ArrayType(final Type heldType)
PrimitiveType =
    BooleanType
  | CharType
  | DoubleType
  | FloatType
  | IntType
  | LongType
  | ShortType
UserError =
    Semantic(SemanticError error)
  | Syntactic(SyntaxError error)
SyntaxError =
    UnexpectedToken(String expected, String found, int line)
SemanticError =
    DuplicateDataType(String dataTypeName)
  | ConstructorDataTypeConflict(String dataTypeName)
  | DuplicateConstructor(String dataTypeName, String constructorName)
  | DuplicateArgName(String dataTypeName, String constructorName, String argName)
  | DuplicateModifier(String dataTypeName, String constructorName, String argName, String modifier)

*/
public final class SyntaxError {

   public static final  SyntaxError _UnexpectedToken(String expected, String found, int line) { return new SyntaxError(expected, found, line); }

      public String expected;
      public String found;
      public int line;

      public SyntaxError(String expected, String found, int line) {
         this.expected = expected;
         this.found = found;
         this.line = line;
      }

      @Override
      public int hashCode() {
          final int prime = 31;
          int result = 1;
          result = prime * result + ((expected == null) ? 0 : expected.hashCode());
          result = prime * result + ((found == null) ? 0 : found.hashCode());
          result = prime * result + line;
          return result;
      }

      @Override
      public boolean equals(Object obj) {
         if (this == obj) return true;
         if (obj == null) return false;
         if (getClass() != obj.getClass()) return false;
         SyntaxError other = (SyntaxError)obj;
         if (expected == null) {
            if (other.expected != null) return false;
         } else if (!expected.equals(other.expected)) return false;
         if (found == null) {
            if (other.found != null) return false;
         } else if (!found.equals(other.found)) return false;
         if (line != other.line) return false;
         return true;
      }

      @Override
      public String toString() {
         return "SyntaxError(expected = " + expected + ", found = " + found + ", line = " + line + ")";
      }

}