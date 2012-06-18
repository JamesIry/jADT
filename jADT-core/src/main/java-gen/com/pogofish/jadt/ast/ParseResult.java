package com.pogofish.jadt.ast;

import java.util.List;

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
public final class ParseResult {

   public static final  ParseResult _ParseResult(Doc doc, List<SyntaxError> errors) { return new ParseResult(doc, errors); }

      public Doc doc;
      public List<SyntaxError> errors;

      public ParseResult(Doc doc, List<SyntaxError> errors) {
         this.doc = doc;
         this.errors = errors;
      }

      @Override
      public int hashCode() {
          final int prime = 31;
          int result = 1;
          result = prime * result + ((doc == null) ? 0 : doc.hashCode());
          result = prime * result + ((errors == null) ? 0 : errors.hashCode());
          return result;
      }

      @Override
      public boolean equals(Object obj) {
         if (this == obj) return true;
         if (obj == null) return false;
         if (getClass() != obj.getClass()) return false;
         ParseResult other = (ParseResult)obj;
         if (doc == null) {
            if (other.doc != null) return false;
         } else if (!doc.equals(other.doc)) return false;
         if (errors == null) {
            if (other.errors != null) return false;
         } else if (!errors.equals(other.errors)) return false;
         return true;
      }

      @Override
      public String toString() {
         return "ParseResult(doc = " + doc + ", errors = " + errors + ")";
      }

}