package pogofish.jadt;

import java.util.Set;

import pogofish.jadt.checker.SemanticException;
import pogofish.jadt.util.Util;

public class SemanticExceptions extends RuntimeException {

    public SemanticExceptions(Set<SemanticException> errors) {
        super(Util.makeString(errors, "\n\n "));
    }

}
