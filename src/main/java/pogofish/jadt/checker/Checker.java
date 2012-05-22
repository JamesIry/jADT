package pogofish.jadt.checker;

import java.util.Set;

import pogofish.jadt.ast.Doc;

public interface Checker {

    public Set<SemanticException> check(Doc doc);

}
