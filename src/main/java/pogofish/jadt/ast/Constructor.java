package pogofish.jadt.ast;

import java.util.List;

public class Constructor {
    public final String name;
    public final List<Arg> args;
    
    public Constructor(String name, List<Arg> args) {
        super();
        this.name = name;
        this.args = args;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((args == null) ? 0 : args.hashCode());
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        Constructor other = (Constructor)obj;
        if (args == null) {
            if (other.args != null) return false;
        } else if (!args.equals(other.args)) return false;
        if (name == null) {
            if (other.name != null) return false;
        } else if (!name.equals(other.name)) return false;
        return true;
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder(name);
        if (! args.isEmpty()) {
            builder.append("(");
            boolean first = true;
            for (Arg arg : args) {
                if (first) {
                    first = false;
                } else {
                    builder.append(", ");
                }
                builder.append(arg);
            }
            builder.append(")");
        }
        return builder.toString();
    }
}