package com.pogofish.jadt.checker;

/**
 * SemanticException indicating that a single constructor field included the same modifier twice
 *
 * @author jiry
 */
public class DuplicateModifierException extends SemanticException {

    /**
     * 
     */
    private static final long serialVersionUID = -3016838411336471200L;

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + ((constructor == null) ? 0 : constructor.hashCode());
        result = prime * result
                + ((dataType == null) ? 0 : dataType.hashCode());
        result = prime * result + ((arg == null) ? 0 : arg.hashCode());
        result = prime * result
                + ((modifier == null) ? 0 : modifier.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        DuplicateModifierException other = (DuplicateModifierException) obj;
        if (constructor == null) {
            if (other.constructor != null)
                return false;
        } else if (!constructor.equals(other.constructor))
            return false;
        if (dataType == null) {
            if (other.dataType != null)
                return false;
        } else if (!dataType.equals(other.dataType))
            return false;
        if (arg == null) {
            if (other.arg != null)
                return false;
        } else if (!arg.equals(other.arg))
            return false;
        if (modifier == null) {
            if (other.modifier != null)
                return false;
        } else if (!modifier.equals(other.modifier))
            return false;
        return true;
    }

    private final String modifier;
    private String dataType;
    private String constructor;
    private String arg;

    public DuplicateModifierException(String dataType, String constructor, String arg, String modifier) {
        super("Field " + arg  + " has modifier '" + modifier + "' more than once.");
        this.dataType = dataType;
        this.constructor = constructor;
        this.arg = arg;
        this.modifier = modifier;
        
    }
    
    
    
}
