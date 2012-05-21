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

public class Doc {
    public final String srcInfo;
    public final String pkg;
    public final List<String> imports;
    public final List<DataType> dataTypes;
    
    public Doc(String srcInfo, String pkg, List<String> imports, List<DataType> dataTypes) {
        super();
        this.srcInfo = srcInfo;
        this.pkg = pkg;
        this.imports = imports;
        this.dataTypes = dataTypes;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((dataTypes == null) ? 0 : dataTypes.hashCode());
        result = prime * result + ((pkg == null) ? 0 : pkg.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        Doc other = (Doc)obj;
        if (dataTypes == null) {
            if (other.dataTypes != null) return false;
        } else if (!dataTypes.equals(other.dataTypes)) return false;
        if (pkg == null) {
            if (other.pkg != null) return false;
        } else if (!pkg.equals(other.pkg)) return false;
        return true;
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder(pkg.isEmpty() ? "" : ("package " + pkg + "\n\n"));
        if (!imports.isEmpty()) {
            for (String imp : imports) {
                builder.append("import " + imp + "\n");
            }
            builder.append("\n");
        }
        for (DataType dataType : dataTypes) {
            builder.append(dataType);
            builder.append("\n");
        }
        return builder.toString();  
    }    
}
