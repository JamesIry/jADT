JADT generates .java source files from algebraic datatype description files. The resulting Java is sealed against extension and automatically gets a visitor interface; an abstract visitor class that computes a default on all constructor types; and methods for equals(), hashCode(), and toString().  The resulting Java has no compile or runtime dependencies besides those you specify in your JADT description file.  While generating Java JADT has no dependencies other than the standard JDK.

JADT is licensed under the [Apache License 2.0](http://www.apache.org/licenses/LICENSE-2.0).

Sample
======

Here's a sample AST for a fragment of a language

    package pogofish.jadt.sampleast
    
    import java.util.List
    
    Type =
         Int
       | Long
   
    Function = Function(Type returnType, String name, List<Arg> args, List<Statement> statements)
    
    Arg = Arg(Type type, String name)
    
    Statement =
        Declaration(Type type, String name, Expression expression)
      | Assignment(String name, Expression expression)
      | Return(Expression expression)
    
    Expression =
        Add(Expression left, Expression right)
      | Variable(String name)
      | Literal(int value)


Usage
=====
To generate from the command line use

    java -cp "path to JADT-core" pogofish.JADT "full path of input file" "base directory for output"

To generate using ant create a build.xml along these lines
   
    <?xml version="1.0"?>

    <project name="JADTTaskExample" default="compile" basedir=".">
      <taskdef name="jadt" classname="pogofish.jadt.ant.JADTAntTask"/>

      <target name="compile" depeneds="generate">
        <!-- normal compile stuff -->
      </target>
      
      <target name="generate">
        <jadt srcFile="full path of input file" destDir = "base directory for Java output"/>
      </target>
    </project>
    
And then run ant, telling it where to find JADT-core and JADT-ant
    
    ant -lib "directory that has both JADT-core and JADT-ant"

    
To use the generated Java, you'll need some imports

    import static pogofish.jadt.sampleast.Arg.*;
    import static pogofish.jadt.sampleast.Expression.*;
    import static pogofish.jadt.sampleast.Function.*;
    import static pogofish.jadt.sampleast.Statement.*;
    import static pogofish.jadt.sampleast.Type.*;

    import java.util.*;

    import pogofish.jadt.sampleast.Expression.*;
    import pogofish.jadt.sampleast.Statement.*; 

Here's an example of creating a complete function using generated factory methods

     public Function sampleFunction() {   
            return _Function(_Int, "addTwo", list(_Arg(_Int, "x"), _Arg(_Int, "y")), list(
                    _Return(_Add(_Variable("x"), _Variable("y")))
                    ));
     }

     public static <A> List<A> list(A... elements) {
         final List<A> list = new ArrayList<A>(elements.length);
         for (A element : elements) {
             list.add(element);
         }
         return list;
     }    

Here's a sample function that returns all the integer literals in an expression

     public Set<Integer> expressionLiterals(Expression expression) {
         return expression.accept(new Expression.Visitor<Set<Integer>>() {
             @Override
             public Set<Integer> visit(Add x) {
                 final Set<Integer> results = expressionLiterals(x.left);
                 results.addAll(expressionLiterals(x.right));
                 return results;
             }

             @Override
             public Set<Integer> visit(Variable x) {
                 return Collections.<Integer>emptySet();
             }

             @Override
             public Set<Integer> visit(Literal x) {
                 return Collections.singleton(x.value);
             }
         });
     }
     
And here's a sample function that returns true only if a list of statements has a return statement.  Unlike the previous example, this one uses a VisitorWithDefault so that only relevant cases need to be considered.

     public boolean hasReturn(List<Statement> statements) {
         boolean hasReturn = false;
         for (Statement statement : statements) {
             hasReturn = hasReturn || statement.accept(new Statement.VisitorWithDefault<Boolean>() {                
                 @Override
                 public Boolean visit(Return x) {
                     return true;
                 }

                 @Override
                 public Boolean getDefault(Statement x) {
                     return false;
                 }});
         }
         return hasReturn;
     }     

Syntax
======

The syntax of a JADT file is

    DOC : PACKAGE IMPORTS DATATYPES
    PACKAGE : "package" PACKAGENAME
    IMPORTS : IMPORT*
    IMPORT : "import" PACKAGENAME
    DATATYPE : DATATYPE+
    DATATYPE : DATATYPENAME TYPEARGS? "=" CONSTRUCTORS
    TYPEARGS : '<' IDENTIFIER (',' IDENTIFIER)* '>'
    DATATYPENAME : IDENTIFIER
    CONSTRUCTORS : CONSTRUCTOR ("|" CONSTRUCTORS)?
    CONSTRUCTOR : CONSTRUCTORNAME ("(" ARGS ")")?
    ARGS : ARG ("," ARGS)?
    ARG : TYPE ARGNAME
    TYPE : ARRAY | JAVAPRIMITIVE | TYPENAME ("<" TYPELIST ">")?
    ARRAY : TYPE "[]"
    JAVAPRIMITIVE : "boolean" | "char" | "double" |
                    "float" | "int" | "long" | "short"
    TYPENAME : IDENTIFIER
    TYPELIST : TYPE ("," TYPELIST )?
    ARGNAME : IDENTIFIER
    PACKAGENAME : valid Java package name
    IDENTIFIER : valid Java identifier
    
Lexical conventions    
* JADT is case sensitive.
* Java style comments, both end of line (//) and block (/* */), are allowed.
* Whitespace isn't significant other than for separating tokens.
* End of line characters are just treated as whitespace.  
* There's no need for semicolons or other end markers.
* Java keywords cannot be used as identifiers or package names.
* The punctuation "=" "," "<" ">" "(" ")" serves as both tokens and token separators.  E.g. the data type definition "Foo=Bar|Baz" is parsed the same as "Foo = Bar | Baz"

Known Limitations
=================
* Currently you can add comments to a JADT file but there's no way to create comments that flow through to the generated Java.
* Currently there's no way to create a parameterized (generic) ADT.  So no "Option A = Some(A value) | None". Should be easy to fix, I just haven't gotten around to it.
* If an ADT has multiple constructors, none may the same name as the data type.  It's not clear how to lift that restriction and still make Java happy other than by mangling names and that wouldn't be invisible to the user.  For now, use different names.  E.g. Foo = Foo | Bar should be Foo = FooDef | Bar

_Copyright 2012 James Iry_
