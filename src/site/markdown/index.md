About JADT
==========
JADT turns [algebraic datatype](what_adt.html) description [files](syntax.html) into [Java source files](how_adt.html). The resulting Java is nearly as easy to use as an Enum, but [far more flexible](why_adt.html).

JADT uses very liberal [licensing](license.html).

[Download Now](download.html).

Sample
======

Here's a sample JADT file that describes the abstract syntax tree for a fragment of a fictional language

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
Java can be generated from JADT files using [Apache Ant](ant/index.html) or from the [shell](core/index.html).

    
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
