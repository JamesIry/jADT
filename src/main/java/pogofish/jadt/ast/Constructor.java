/*
Copyright 2012 James Iry

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/
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