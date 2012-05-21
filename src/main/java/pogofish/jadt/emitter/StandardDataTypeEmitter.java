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
package pogofish.jadt.emitter;

import java.io.IOException;

import pogofish.jadt.ast.Constructor;
import pogofish.jadt.ast.DataType;


public class StandardDataTypeEmitter implements DataTypeEmitter {
    private final ConstructorEmitter constructorEmitter;
    private final ClassBodyEmitter classBodyEmitter;
    
    public StandardDataTypeEmitter(ClassBodyEmitter classBodyEmitter, ConstructorEmitter constructorEmitter) {
        super();
        this.constructorEmitter = constructorEmitter;
        this.classBodyEmitter = classBodyEmitter;
    }

    /* (non-Javadoc)
     * @see sfdc.adt.emitter.DataTypeEmitter#emit(sfdc.adt.emitter.Target, sfdc.adt.ast.DataType, java.lang.String)
     */
    @Override
    public void emit(Target target, DataType dataType, String header) throws IOException {
        target.write(header);
        
        if (dataType.constructors.size() == 1) {
            emitSingleConstructor(target, dataType, header);
            
        } else {
            emitMultipleConstructor(target, dataType, header);
        }
    }

    private void emitSingleConstructor(Target target, DataType dataType, String header) throws IOException {
        final Constructor originalConstructor = dataType.constructors.get(0);
        final Constructor pseudoConstructor = new Constructor(dataType.name, originalConstructor.args);
        
        target.write("public final class " + dataType.name + " {\n\n");
        classBodyEmitter.constructorFactory(target, dataType.name, originalConstructor.name, pseudoConstructor);
        target.write("\n\n");
        
        classBodyEmitter.emitConstructorMethod(target, pseudoConstructor);
        target.write("\n\n");
        
        classBodyEmitter.emitHashCode(target, pseudoConstructor);
        target.write("\n\n");
        
        classBodyEmitter.emitEquals(target, pseudoConstructor);
        target.write("\n\n");
        
        classBodyEmitter.emitToString(target, pseudoConstructor);
        target.write("\n\n");
        
        target.write("}");
        
    }

    private void emitMultipleConstructor(Target target, DataType dataType, String header) throws IOException {
        target.write("public abstract class " + dataType.name + " {\n\n");
        target.write("   private " + dataType.name + "() {\n");
        target.write("   }\n");
        
        for(Constructor constructor : dataType.constructors) {
            target.write("\n");
            constructorEmitter.constructorFactory(target, dataType.name, constructor);
        }
        target.write("\n\n");
        
        target.write("   public static interface Visitor<A> {\n");
        for(Constructor constructor : dataType.constructors) {
            target.write("      A visit(" + constructor.name + " x);");
            target.write("\n");
        }
        target.write("   }\n\n");
        
        target.write("   public static abstract class VisitorWithDefault<A> implements Visitor<A> {\n");
        for(Constructor constructor : dataType.constructors) {
            target.write("      @Override\n");
            target.write("      public A visit(" + constructor.name + " x) { return getDefault(x); }\n\n");
        }
        target.write("      public abstract A getDefault(" + dataType.name + " x);\n");
        target.write("   }");
        
        for(Constructor constructor : dataType.constructors) {
            target.write("\n\n");
            constructorEmitter.constructorDeclaration(target, constructor, dataType.name);
        }
        target.write("\n\n   public abstract <A> A accept(Visitor<A> visitor);\n\n");
        
        target.write("}");
    }





}
