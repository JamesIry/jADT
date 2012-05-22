package pogofish.jadt.checker;

import pogofish.jadt.ast.Doc;

public class MissingDataTypesException extends SemanticException {
    private final Doc doc;
    
    public MissingDataTypesException(Doc doc) {
        super("The document parsed from " + doc.srcInfo + " did not have any dataTypes.");        
        this.doc = doc;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((doc == null) ? 0 : doc.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        MissingDataTypesException other = (MissingDataTypesException)obj;
        if (doc == null) {
            if (other.doc != null) return false;
        } else if (!doc.equals(other.doc)) return false;
        return true;
    }
    
    
    
}
