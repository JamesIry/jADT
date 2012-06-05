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
package pogofish.jadt.checker;


/**
 * SemanticException indicating that a data type and a constructor had the same name
 *
 * @author jiry
 */
public class ConstructorDataTypeConflictException extends SemanticException {

	private static final long serialVersionUID = -3910178888465174226L;
	private final String name;
    
    public ConstructorDataTypeConflictException(String name) {
        super("Data type " + name + " cannot have a constructor with the name " + name +".  Only single constructor data types may have constructors with the same name");
        this.name = name;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + name.hashCode();
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        ConstructorDataTypeConflictException other = (ConstructorDataTypeConflictException)obj;
        if (!name.equals(other.name)) return false;
        return true;
    }
}
