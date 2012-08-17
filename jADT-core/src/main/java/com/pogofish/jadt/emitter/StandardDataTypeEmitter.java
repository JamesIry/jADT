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

import com.pogofish.jadt.ast.Annotation;
import com.pogofish.jadt.ast.Constructor;
import com.pogofish.jadt.ast.DataType;
import com.pogofish.jadt.ast.Optional;
import com.pogofish.jadt.ast.Optional.None;
import com.pogofish.jadt.ast.Optional.Some;
import com.pogofish.jadt.ast.RefType;
import com.pogofish.jadt.comments.CommentProcessor;
import com.pogofish.jadt.printer.ASTPrinter;
import com.pogofish.jadt.sink.Sink;


public class StandardDataTypeEmitter implements DataTypeEmitter {
	private static final String SINGLE_CONSTRUCTOR_INDENT = "   ";

    private static final Logger logger = Logger.getLogger(StandardConstructorEmitter.class.toString());
    
    private final ConstructorEmitter constructorEmitter;
    private final ClassBodyEmitter classBodyEmitter;
    private final CommentProcessor commentProcessor = new CommentProcessor();    
    
    public StandardDataTypeEmitter(ClassBodyEmitter classBodyEmitter, ConstructorEmitter constructorEmitter) {
        super();
        this.constructorEmitter = constructorEmitter;
        this.classBodyEmitter = classBodyEmitter;
    }

    /* (non-Javadoc)
     * @see sfdc.adt.emitter.DataTypeEmitter#emit(sfdc.adt.emitter.Sink, sfdc.adt.ast.DataType, java.lang.String)
     */
    @Override
    public void emit(Sink sink, DataType dataType, String header) {
    	logger.fine("Generating data type " + dataType.name + ".");
        sink.write(header);
        sink.write(ASTPrinter.print(dataType));
        sink.write("\n*/\n");

        sink.write(ASTPrinter.printComments("", commentProcessor.leftAlign(dataType.comments)));
        for (Annotation annotation : dataType.annotations) {
            sink.write(ASTPrinter.print(annotation));
            sink.write("\n");
        }
        if (dataType.constructors.size() == 1) {
            emitSingleConstructor(sink, dataType, header);
            
        } else {
            emitMultipleConstructor(sink, dataType, header);
        }
    }
    
    private void emitBaseClassAndInterfaces(final Sink sink, DataType dataType) {
        dataType.extendedType._switch(new Optional.SwitchBlock<RefType>() {
            @Override
            public void _case(Some<RefType> x) {
                sink.write(" extends ");
                sink.write(ASTPrinter.print(x.value));
            }

            @Override
            public void _case(None<RefType> x) {
            }
        });
        if (!dataType.implementedTypes.isEmpty()) {
            sink.write(" implements ");
            boolean first = true;
            for (RefType type : dataType.implementedTypes) {
                if (first) {
                    first = false;
                } else {
                    sink.write(", ");
                }
                sink.write(ASTPrinter.print(type));
            }
        }
    }

    private void emitSingleConstructor(Sink sink, DataType dataType, String header) {
    	logger.finer("Generating single constructor for " + dataType.name + ".");
        final Constructor originalConstructor = dataType.constructors.get(0);
        final Constructor pseudoConstructor = new Constructor(originalConstructor.comments, dataType.name, originalConstructor.args);
        
        sink.write("public final class " + dataType.name);
        classBodyEmitter.emitParameterizedTypeName(sink, dataType.typeArguments);
        emitBaseClassAndInterfaces(sink, dataType);
        sink.write(" {\n\n");
        
        
        classBodyEmitter.constructorFactory(sink, dataType.name, originalConstructor.name, dataType.typeArguments, pseudoConstructor);
        sink.write("\n\n");
        
        classBodyEmitter.emitConstructorMethod(sink, SINGLE_CONSTRUCTOR_INDENT, pseudoConstructor);
        sink.write("\n\n");
        
        classBodyEmitter.emitHashCode(sink, SINGLE_CONSTRUCTOR_INDENT, pseudoConstructor);
        sink.write("\n\n");
        
        classBodyEmitter.emitEquals(sink, SINGLE_CONSTRUCTOR_INDENT, pseudoConstructor, dataType.typeArguments);
        sink.write("\n\n");
        
        classBodyEmitter.emitToString(sink, SINGLE_CONSTRUCTOR_INDENT, pseudoConstructor);
        sink.write("\n\n");
        
        sink.write("}");
        
    }

    private void emitMultipleConstructor(Sink sink, DataType dataType, String header) {
       	logger.finer("Generating multiple constructors for " + dataType.name + ".");
        sink.write("public abstract class " + dataType.name);
        classBodyEmitter.emitParameterizedTypeName(sink, dataType.typeArguments);
        emitBaseClassAndInterfaces(sink, dataType);
        sink.write(" {\n\n");
        
        sink.write("   private " + dataType.name + "() {\n");
        sink.write("   }\n");
        
        for(Constructor constructor : dataType.constructors) {
            sink.write("\n");
            constructorEmitter.constructorFactory(sink, dataType.name, dataType.typeArguments, constructor);
        }
        sink.write("\n\n");
        
        final List<String> visitorTypeArguments = new ArrayList<String>(dataType.typeArguments);
        visitorTypeArguments.add("ResultType");
        
        sink.write("   public static interface MatchBlock"); 
        classBodyEmitter.emitParameterizedTypeName(sink, visitorTypeArguments);
        sink.write(" {\n");
        for(Constructor constructor : dataType.constructors) {
            sink.write("      ResultType _case(" + constructor.name);
            classBodyEmitter.emitParameterizedTypeName(sink, dataType.typeArguments);
            sink.write(" x);\n");
        }
        sink.write("   }\n\n");
        
        sink.write("   public static abstract class MatchBlockWithDefault"); 
        classBodyEmitter.emitParameterizedTypeName(sink, visitorTypeArguments);
        sink.write(" implements MatchBlock"); 
        classBodyEmitter.emitParameterizedTypeName(sink, visitorTypeArguments);
        sink.write(" {\n");
        for(Constructor constructor : dataType.constructors) {
            sink.write("      @Override\n");
            sink.write("      public ResultType _case(" + constructor.name);
            classBodyEmitter.emitParameterizedTypeName(sink, dataType.typeArguments);
            sink.write(" x) { return _default(x); }\n\n");
        }
        sink.write("      protected abstract ResultType _default(" + dataType.name);
        classBodyEmitter.emitParameterizedTypeName(sink, dataType.typeArguments);
        sink.write(" x);\n");
        sink.write("   }");
        
        sink.write("\n\n");
        
        sink.write("   public static interface SwitchBlock");
        classBodyEmitter.emitParameterizedTypeName(sink, dataType.typeArguments);
        sink.write(" {\n");
        for(Constructor constructor : dataType.constructors) {
            sink.write("      void _case(" + constructor.name);
            classBodyEmitter.emitParameterizedTypeName(sink, dataType.typeArguments);
            sink.write(" x);\n");
        }
        sink.write("   }\n\n");
        
        sink.write("   public static abstract class SwitchBlockWithDefault");
        classBodyEmitter.emitParameterizedTypeName(sink, dataType.typeArguments);
        sink.write(" implements SwitchBlock");
        classBodyEmitter.emitParameterizedTypeName(sink, dataType.typeArguments);
        sink.write(" {\n");
        for(Constructor constructor : dataType.constructors) {
            sink.write("      @Override\n");
            sink.write("      public void _case(" + constructor.name);
            classBodyEmitter.emitParameterizedTypeName(sink, dataType.typeArguments);            
            sink.write(" x) { _default(x); }\n\n");
        }
        sink.write("      protected abstract void _default(" + dataType.name);
        classBodyEmitter.emitParameterizedTypeName(sink, dataType.typeArguments);        
        sink.write(" x);\n");
        sink.write("   }");
        
        for(Constructor constructor : dataType.constructors) {
            sink.write("\n\n");
            constructorEmitter.constructorDeclaration(sink, constructor, dataType.name, dataType.typeArguments);
        }
        sink.write("\n\n   public abstract <ResultType> ResultType match(MatchBlock");
        classBodyEmitter.emitParameterizedTypeName(sink, visitorTypeArguments);
        sink.write(" matchBlock);\n\n");
        
        sink.write("   public abstract void _switch(SwitchBlock");
        classBodyEmitter.emitParameterizedTypeName(sink, dataType.typeArguments);
        sink.write(" switchBlock);\n\n");
        
        sink.write("}");
    }



}
