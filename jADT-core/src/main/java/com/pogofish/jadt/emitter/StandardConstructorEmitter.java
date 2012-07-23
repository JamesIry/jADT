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

import static com.pogofish.jadt.util.Util.set;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import com.pogofish.jadt.ast.Constructor;
import com.pogofish.jadt.comments.CommentProcessor;
import com.pogofish.jadt.printer.ASTPrinter;
import com.pogofish.jadt.sink.Sink;


public class StandardConstructorEmitter implements ConstructorEmitter {
	private static final String INDENT = "      ";
    private static final Logger logger = Logger.getLogger(StandardConstructorEmitter.class.toString());
    private final ClassBodyEmitter classBodyEmitter;
    private final CommentProcessor commentProcessor = new CommentProcessor();
    private static final Set<String> CONSTRUCTOR_CLASS_STRIP = set("@return", "@param");
    
    public StandardConstructorEmitter(ClassBodyEmitter classBodyEmitter) {
        super();
        this.classBodyEmitter = classBodyEmitter;
    }

    @Override
    public void constructorFactory(Sink sink, String dataTypeName, List<String> typeParameters, Constructor constructor) {
        classBodyEmitter.constructorFactory(sink, dataTypeName, constructor.name, typeParameters, constructor);
    }
    
    @Override
    public void constructorDeclaration(Sink sink, Constructor constructor, String dataTypeName, List<String> typeParameters) {
    	logger.finer("Generating constructor class for " + constructor.name + " in datatype " + dataTypeName);
    	sink.write(ASTPrinter.printComments("   ", commentProcessor.leftAlign(commentProcessor.stripTags(CONSTRUCTOR_CLASS_STRIP, constructor.comments))));
    	sink.write("   public static final class " + constructor.name);
        classBodyEmitter.emitParameterizedTypeName(sink, typeParameters);
        sink.write(" extends " + dataTypeName);
        classBodyEmitter.emitParameterizedTypeName(sink, typeParameters);
        sink.write(" {\n");
        
        classBodyEmitter.emitConstructorMethod(sink, INDENT, constructor);
        sink.write("\n\n");
        
        emitAccept(sink, typeParameters);
        sink.write("\n\n");
        
        classBodyEmitter.emitHashCode(sink, INDENT, constructor);
        sink.write("\n\n");
        
        classBodyEmitter.emitEquals(sink, INDENT, constructor, typeParameters);
        sink.write("\n\n");
        
        classBodyEmitter.emitToString(sink, INDENT, constructor);
        sink.write("\n\n");
        
        sink.write("   }");
    }
    
    private void emitAccept(Sink sink, List<String> typeArguments) {
        final List<String> visitorTypeArguments = new ArrayList<String>(typeArguments);
        visitorTypeArguments.add("ResultType");
        
        sink.write("      @Override\n");
        sink.write("      public <ResultType> ResultType match(MatchBlock");
        classBodyEmitter.emitParameterizedTypeName(sink, visitorTypeArguments);
        sink.write(" matchBlock) { return matchBlock._case(this); }\n");
        sink.write("\n");
        sink.write("      @Override\n");
        sink.write("      public void _switch(SwitchBlock");
        classBodyEmitter.emitParameterizedTypeName(sink, typeArguments);
        sink.write(" switchBlock) { switchBlock._case(this); }");
    }

    
}
