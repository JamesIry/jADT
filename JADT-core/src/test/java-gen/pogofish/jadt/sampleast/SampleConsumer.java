/*
Copyright 2012 James Iry

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/
package pogofish.jadt.sampleast;

import static pogofish.jadt.sampleast.Arg._Arg;
import static pogofish.jadt.sampleast.Expression._Add;
import static pogofish.jadt.sampleast.Expression._Variable;
import static pogofish.jadt.sampleast.Function._Function;
import static pogofish.jadt.sampleast.Statement._Return;
import static pogofish.jadt.sampleast.Type._Int;

import java.util.*;

import pogofish.jadt.sampleast.Expression.Add;
import pogofish.jadt.sampleast.Expression.Literal;
import pogofish.jadt.sampleast.Expression.Variable;
import pogofish.jadt.sampleast.Statement.Return;

public class SampleConsumer {
    public Function sampleFunction() {   
        return _Function(_Int, "addTwo", list(_Arg(_Int, "x"), _Arg(_Int, "y")), list(
                _Return(_Add(_Variable("x"), _Variable("y")))
                ));
    }
    
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
    
    public static <A> List<A> list(A... elements) {
        final List<A> list = new ArrayList<A>(elements.length);
        for (A element : elements) {
            list.add(element);
        }
        return list;
    }    
}
