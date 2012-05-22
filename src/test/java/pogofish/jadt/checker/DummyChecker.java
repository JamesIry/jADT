package pogofish.jadt.checker;

import java.util.Collections;
import java.util.Set;

import pogofish.jadt.ast.Doc;

public class DummyChecker implements Checker {
    private Doc doc = null;

    @Override
    public Set<SemanticException> check(Doc doc) {
        this.doc = doc;
        return Collections.emptySet();
    }
    
    public Doc lastDoc() {
        return doc;
    }

}
