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
package com.pogofish.jadt.emitter;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import com.pogofish.jadt.ast.Constructor;
import com.pogofish.jadt.target.Target;


public class StandardConstructorEmitter implements ConstructorEmitter {
	private static final Logger logger = Logger.getLogger(StandardConstructorEmitter.class.toString());
    private final ClassBodyEmitter classBodyEmitter;
    
    public StandardConstructorEmitter(ClassBodyEmitter classBodyEmitter) {
        super();
        this.classBodyEmitter = classBodyEmitter;
    }

    @Override
    public void constructorFactory(Target target, String dataTypeName, List<String> typeParameters, Constructor constructor) {
        classBodyEmitter.constructorFactory(target, dataTypeName, constructor.name, typeParameters, constructor);
    }
    
    @Override
    public void constructorDeclaration(Target target, Constructor constructor, String dataTypeName, List<String> typeParameters) {
    	logger.finer("Generating constructor class for " + constructor.name + " in datatype " + dataTypeName);
    	target.write("   public static final class " + constructor.name);
        classBodyEmitter.emitParameterizedTypeName(target, typeParameters);
        target.write(" extends " + dataTypeName);
        classBodyEmitter.emitParameterizedTypeName(target, typeParameters);
        target.write(" {\n");
        
        classBodyEmitter.emitConstructorMethod(target, constructor);
        target.write("\n\n");
        
        emitAccept(target, typeParameters);
        target.write("\n\n");
        
        classBodyEmitter.emitHashCode(target, constructor);
        target.write("\n\n");
        
        classBodyEmitter.emitEquals(target, constructor, typeParameters);
        target.write("\n\n");
        
        classBodyEmitter.emitToString(target, constructor);
        target.write("\n\n");
        
        target.write("   }");
    }
    
    private void emitAccept(Target target, List<String> typeArguments) {
        final List<String> visitorTypeArguments = new ArrayList<String>(typeArguments);
        visitorTypeArguments.add("ResultType");
        
        target.write("      @Override\n");
        target.write("      public <ResultType> ResultType accept(Visitor");
        classBodyEmitter.emitParameterizedTypeName(target, visitorTypeArguments);
        target.write(" visitor) { return visitor.visit(this); }\n");
        target.write("\n");
        target.write("      @Override\n");
        target.write("      public void accept(VoidVisitor");
        classBodyEmitter.emitParameterizedTypeName(target, typeArguments);
        target.write(" visitor) { visitor.visit(this); }");
    }

    
}
