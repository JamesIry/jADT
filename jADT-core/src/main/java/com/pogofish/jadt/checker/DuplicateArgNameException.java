package com.pogofish.jadt.checker;

public class DuplicateArgNameException extends SemanticException {

    /**
     * 
     */
    private static final long serialVersionUID = 8953072744136320613L;
    private String dataType;
    private String constructor;
    private String arg;

    public DuplicateArgNameException(String dataType, String constructor, String arg) {
        super("Data type " + dataType + " constructor " + constructor + " has duplicated arg name " + arg);
        this.dataType = dataType;
        this.constructor = constructor;
        this.arg = arg;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((arg == null) ? 0 : arg.hashCode());
        result = prime * result
                + ((constructor == null) ? 0 : constructor.hashCode());
        result = prime * result
                + ((dataType == null) ? 0 : dataType.hashCode());
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
        DuplicateArgNameException other = (DuplicateArgNameException) obj;
        if (arg == null) {
            if (other.arg != null)
                return false;
        } else if (!arg.equals(other.arg))
            return false;
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
        return true;
    }

    
}
