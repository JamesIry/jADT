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
   
    Function = 
         FunctionDef(Type returnType, 
                     String name, 
                     List<Arg> args, 
                     List<Statement> statements)

    Arg = ArgDef(Type type, String name)

    Statement =
        Declaration(Type type, String name, Expression expression)
      | Assignment(String name, Expression expression)

    Expression =
        Add(Expression left, Expression right)
      | Variable(String name)
      | Literal(int value)

Usage
=====
To generate  use

    java -cp path_to_JADT pogofish.JADT full_path_of_input_file base_directory_for_output
    
To use the generated Java, you'll need some imports

    import static pogofish.jadt.sampleast.Arg.*;
    import static pogofish.jadt.sampleast.Expression.*;
    import static pogofish.jadt.sampleast.Function.*;
    import static pogofish.jadt.sampleast.Statement.*;
    import static pogofish.jadt.sampleast.Type.*;

    import java.util.*;

    import pogofish.jadt.sampleast.*;
    import pogofish.jadt.sampleast.Expression.*;
    import pogofish.jadt.sampleast.Statement.*;    

Here's an example of creating a complete function using generated factory methods

     public Function sampleFunction() {   
         return _FunctionDef(_Int, "addTwo", list(_ArgDef(_Int, "x"), _ArgDef(_Int, "y")), list(
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
    DATATYPE : DATATYPENAME "=" CONSTRUCTORS
    DATATYPENAME : IDENTIFIER
    CONSTRUCTORS : CONSTRUCTOR ("|" CONSTRUCTORS)?
    CONSTRUCTOR : CONSTRUCTORNAME ("(" ARGS ")")?
    ARGS : ARG ("," ARGS)?
    ARG : TYPE ARGNAME
    TYPE : TYPENAME ("<" TYPELIST ">")?
    TYPELIST : TYPE ("," TYPELIST )?
    TYPENAME : IDENTIFIER
    ARGNAME : IDENTIFIER
    PACKAGENAME : valid Java package name
    IDENTIFIER : valid Java identifier
    
Lexical conventions    
* JADT is case sensitive
* Whitespace isn't significant other than for separating tokens.
* End of line characters are just treated as whitespace.  
* There's no need for semicolons or other end markers.
* Just as in Java, "import" and "package" are keywords and cannot be used as identifiers.
* The punctuation "=" "," "<" ">" "(" ")" serves as both tokens and token separators.  E.g. the data type definition "Foo=Bar|Baz" is parsed the same as "Foo = Bar | Baz"

Known Limitations
=================

* Currently there's no way to create a parameterized (generic) ADT.  So no "Option A = Some(A value) | None". Should be easy to fix, I just haven't gotten around to it.
* ADTs with only a single constructor are done stupidly.  Who needs a whole sealed hierarchy/visitor system for what can basically be handled as a glorified data only class? Just needs two different emitters for constructors.
* Constructors cannot have the same name as the data type.  That restriction could be lifted for single constructor ADTs once those are handled specially, but it's not clear how to lift that restriction for multi-constructor ADTs.
* The aforementioned limitation isn't enforced by JADT - the java compiler just pukes all over the generated code. This is VERY easy to fix
* JADT does not currently enforce a limitation on use of Java keywords other than "package" or "import." If you write "ClassType = class | interface | enum" then JADT will happily produce code that will make a Java compiler grumpy and caustic.

_Copyright 2012 James Iry_
