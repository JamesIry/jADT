package pogofish.jadt.checker;

import pogofish.jadt.ast.Constructor;
import pogofish.jadt.ast.DataType;

public class ConstructorDataTypeConflictException extends SemanticException {
    private final DataType dataType;
    private final String constructorName;
    
    public ConstructorDataTypeConflictException(DataType dataType, Constructor constructor) {
        super("Data type " + dataType.name + " cannot have a constructor with the name " + constructor.name +".  Only single constructor data types may have constructors with the same name");
        this.dataType = dataType;
        this.constructorName = constructor.name;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((constructorName == null) ? 0 : constructorName.hashCode());
        result = prime * result + ((dataType == null) ? 0 : dataType.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        ConstructorDataTypeConflictException other = (ConstructorDataTypeConflictException)obj;
        if (constructorName == null) {
            if (other.constructorName != null) return false;
        } else if (!constructorName.equals(other.constructorName)) return false;
        if (dataType == null) {
            if (other.dataType != null) return false;
        } else if (!dataType.equals(other.dataType)) return false;
        return true;
    }
}
