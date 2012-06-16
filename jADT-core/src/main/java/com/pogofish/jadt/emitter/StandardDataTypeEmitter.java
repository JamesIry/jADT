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
import com.pogofish.jadt.ast.DataType;
import com.pogofish.jadt.target.Target;



public class StandardDataTypeEmitter implements DataTypeEmitter {
	private static final Logger logger = Logger.getLogger(StandardConstructorEmitter.class.toString());
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
    public void emit(Target target, DataType dataType, String header) {
    	logger.fine("Generating data type " + dataType.name + ".");
        target.write(header);
        if (dataType.constructors.size() == 1) {
            emitSingleConstructor(target, dataType, header);
            
        } else {
            emitMultipleConstructor(target, dataType, header);
        }
    }

    private void emitSingleConstructor(Target target, DataType dataType, String header) {
    	logger.finer("Generating single constructor for " + dataType.name + ".");
        final Constructor originalConstructor = dataType.constructors.get(0);
        final Constructor pseudoConstructor = new Constructor(dataType.name, originalConstructor.args);
        
        target.write("public final class " + dataType.name);
        classBodyEmitter.emitParameterizedTypeName(target, dataType.typeArguments);
        target.write(" {\n\n");
        
        
        classBodyEmitter.constructorFactory(target, dataType.name, originalConstructor.name, dataType.typeArguments, pseudoConstructor);
        target.write("\n\n");
        
        classBodyEmitter.emitConstructorMethod(target, pseudoConstructor);
        target.write("\n\n");
        
        classBodyEmitter.emitHashCode(target, pseudoConstructor);
        target.write("\n\n");
        
        classBodyEmitter.emitEquals(target, pseudoConstructor, dataType.typeArguments);
        target.write("\n\n");
        
        classBodyEmitter.emitToString(target, pseudoConstructor);
        target.write("\n\n");
        
        target.write("}");
        
    }

    private void emitMultipleConstructor(Target target, DataType dataType, String header) {
       	logger.finer("Generating multiple constructors for " + dataType.name + ".");
        target.write("public abstract class " + dataType.name);
        classBodyEmitter.emitParameterizedTypeName(target, dataType.typeArguments);
        target.write(" {\n\n");
        
        target.write("   private " + dataType.name + "() {\n");
        target.write("   }\n");
        
        for(Constructor constructor : dataType.constructors) {
            target.write("\n");
            constructorEmitter.constructorFactory(target, dataType.name, dataType.typeArguments, constructor);
        }
        target.write("\n\n");
        
        final List<String> visitorTypeArguments = new ArrayList<String>(dataType.typeArguments);
        visitorTypeArguments.add("ResultType");
        
        target.write("   public static interface MatchBlock"); 
        classBodyEmitter.emitParameterizedTypeName(target, visitorTypeArguments);
        target.write(" {\n");
        for(Constructor constructor : dataType.constructors) {
            target.write("      ResultType _case(" + constructor.name);
            classBodyEmitter.emitParameterizedTypeName(target, dataType.typeArguments);
            target.write(" x);\n");
        }
        target.write("   }\n\n");
        
        target.write("   public static abstract class MatchBlockWithDefault"); 
        classBodyEmitter.emitParameterizedTypeName(target, visitorTypeArguments);
        target.write(" implements MatchBlock"); 
        classBodyEmitter.emitParameterizedTypeName(target, visitorTypeArguments);
        target.write(" {\n");
        for(Constructor constructor : dataType.constructors) {
            target.write("      @Override\n");
            target.write("      public ResultType _case(" + constructor.name);
            classBodyEmitter.emitParameterizedTypeName(target, dataType.typeArguments);
            target.write(" x) { return _default(x); }\n\n");
        }
        target.write("      protected abstract ResultType _default(" + dataType.name);
        classBodyEmitter.emitParameterizedTypeName(target, dataType.typeArguments);
        target.write(" x);\n");
        target.write("   }");
        
        target.write("\n\n");
        
        target.write("   public static interface SwitchBlock");
        classBodyEmitter.emitParameterizedTypeName(target, dataType.typeArguments);
        target.write(" {\n");
        for(Constructor constructor : dataType.constructors) {
            target.write("      void _case(" + constructor.name);
            classBodyEmitter.emitParameterizedTypeName(target, dataType.typeArguments);
            target.write(" x);\n");
        }
        target.write("   }\n\n");
        
        target.write("   public static abstract class SwitchBlockWithDefault");
        classBodyEmitter.emitParameterizedTypeName(target, dataType.typeArguments);
        target.write(" implements SwitchBlock");
        classBodyEmitter.emitParameterizedTypeName(target, dataType.typeArguments);
        target.write(" {\n");
        for(Constructor constructor : dataType.constructors) {
            target.write("      @Override\n");
            target.write("      public void _case(" + constructor.name);
            classBodyEmitter.emitParameterizedTypeName(target, dataType.typeArguments);            
            target.write(" x) { _default(x); }\n\n");
        }
        target.write("      protected abstract void _default(" + dataType.name);
        classBodyEmitter.emitParameterizedTypeName(target, dataType.typeArguments);        
        target.write(" x);\n");
        target.write("   }");
        
        for(Constructor constructor : dataType.constructors) {
            target.write("\n\n");
            constructorEmitter.constructorDeclaration(target, constructor, dataType.name, dataType.typeArguments);
        }
        target.write("\n\n   public abstract <ResultType> ResultType match(MatchBlock");
        classBodyEmitter.emitParameterizedTypeName(target, visitorTypeArguments);
        target.write(" matchBlock);\n\n");
        
        target.write("   public abstract void _switch(SwitchBlock");
        classBodyEmitter.emitParameterizedTypeName(target, dataType.typeArguments);
        target.write(" switchBlock);\n\n");
        
        target.write("}");
    }



}
