package pogofish.jadt.ast;

import java.util.List;


public class DataType {
    public final String name;
    public final List<Constructor> constructors;
    
    public DataType(String name, List<Constructor> constructors) {
        super();
        this.name = name;
        this.constructors = constructors;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((constructors == null) ? 0 : constructors.hashCode());
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        DataType other = (DataType)obj;
        if (constructors == null) {
            if (other.constructors != null) return false;
        } else if (!constructors.equals(other.constructors)) return false;
        if (name == null) {
            if (other.name != null) return false;
        } else if (!name.equals(other.name)) return false;
        return true;
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder("data " + name + " =\n    ");
        boolean first = true;
        for (Constructor constructor : constructors) {
            if (first) {
                first = false;
            } else {
                builder.append("\n  | ");
            }
            builder.append(constructor);
        }
        return builder.toString();
    }
}