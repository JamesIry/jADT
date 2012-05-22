package pogofish.jadt.checker;

import pogofish.jadt.ast.Constructor;
import pogofish.jadt.ast.DataType;

public class DuplicateConstructorException extends SemanticException {
    private final String dataTypeName;
    private final String constructorName;
    
    public DuplicateConstructorException(DataType dataType, Constructor constructor) {
        super("Data type " + dataType.name + " cannot have two constructors named " + constructor.name + ".");
        this.dataTypeName = dataType.name;
        this.constructorName = constructor.name;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((constructorName == null) ? 0 : constructorName.hashCode());
        result = prime * result + ((dataTypeName == null) ? 0 : dataTypeName.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        DuplicateConstructorException other = (DuplicateConstructorException)obj;
        if (constructorName == null) {
            if (other.constructorName != null) return false;
        } else if (!constructorName.equals(other.constructorName)) return false;
        if (dataTypeName == null) {
            if (other.dataTypeName != null) return false;
        } else if (!dataTypeName.equals(other.dataTypeName)) return false;
        return true;
    }
    
    

}
