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

import pogofish.jadt.ast.Constructor;
import pogofish.jadt.target.Target;

public class StandardConstructorEmitter implements ConstructorEmitter {
    private final ClassBodyEmitter classBodyEmitter;
    
    public StandardConstructorEmitter(ClassBodyEmitter classBodyEmitter) {
        super();
        this.classBodyEmitter = classBodyEmitter;
    }

    @Override
    public void constructorFactory(Target target, String dataTypeName, Constructor constructor) {
        classBodyEmitter.constructorFactory(target, dataTypeName, constructor.name, constructor);
    }
    
    @Override
    public void constructorDeclaration(Target target, Constructor constructor, String dataTypeName) {
        target.write("   public static final class " + constructor.name + " extends " + dataTypeName + " {\n");
        
        classBodyEmitter.emitConstructorMethod(target, constructor);
        target.write("\n\n");
        
        emitAccept(target);
        target.write("\n\n");
        
        classBodyEmitter.emitHashCode(target, constructor);
        target.write("\n\n");
        
        classBodyEmitter.emitEquals(target, constructor);
        target.write("\n\n");
        
        classBodyEmitter.emitToString(target, constructor);
        target.write("\n\n");
        
        target.write("   }");
    }
    
    private void emitAccept(Target target) {
        target.write("      @Override\n");
        target.write("      public <A> A accept(Visitor<A> visitor) { return visitor.visit(this); }\n");
        target.write("\n");
        target.write("      @Override\n");
        target.write("      public void accept(VoidVisitor visitor) { visitor.visit(this); }");
    }

    
}
