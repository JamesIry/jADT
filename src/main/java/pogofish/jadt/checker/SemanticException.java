package pogofish.jadt.checker;

public abstract class SemanticException extends RuntimeException {

    public SemanticException(String msg) {
        super(msg);
    }

}
