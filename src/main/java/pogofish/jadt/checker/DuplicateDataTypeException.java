package pogofish.jadt.checker;

import pogofish.jadt.ast.DataType;

public class DuplicateDataTypeException extends SemanticException {
    private String dataTypeName;
    
    public DuplicateDataTypeException(DataType dataType) {
        super("Cannot have two datatypes named " + dataType.name);
        this.dataTypeName = dataType.name;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((dataTypeName == null) ? 0 : dataTypeName.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        DuplicateDataTypeException other = (DuplicateDataTypeException)obj;
        if (dataTypeName == null) {
            if (other.dataTypeName != null) return false;
        } else if (!dataTypeName.equals(other.dataTypeName)) return false;
        return true;
    }

}
