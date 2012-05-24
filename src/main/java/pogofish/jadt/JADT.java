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
package pogofish.jadt;

import java.util.Set;

import pogofish.jadt.ast.Doc;
import pogofish.jadt.checker.*;
import pogofish.jadt.emitter.*;
import pogofish.jadt.parser.Parser;
import pogofish.jadt.parser.StandardParser;
import pogofish.jadt.source.*;
import pogofish.jadt.target.*;

/**
 * Programmatic and command line driver that launches parser, then checker, then emitter, weaving everything together
 *
 * @author jiry
 */
public class JADT {
    final Parser parser;
    final DocEmitter emitter;
    final Checker checker;
    final SourceFactory sourceFactory;
    final TargetFactoryFactory factoryFactory;

    /**
     * Takes the names of a source file and output directory and does the JADT thing to them
     * 
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) {
        standardConfigDriver().parseAndEmit(args);
    }
    
    /**
     * Convenient factory method to create a complete standard configuration
     * 
     * @return Driver configured with all the Standard bits
     */
    public static JADT standardConfigDriver() {
        final SourceFactory sourceFactory = new FileSourceFactory();
        final ClassBodyEmitter classBodyEmitter = new StandardClassBodyEmitter();
        final ConstructorEmitter constructorEmitter = new StandardConstructorEmitter(classBodyEmitter);
        final DataTypeEmitter dataTypeEmitter = new StandardDataTypeEmitter(classBodyEmitter, constructorEmitter);
        final DocEmitter docEmitter = new StandardDocEmitter(dataTypeEmitter);      
        final Parser parser = new StandardParser();
        final Checker checker = new StandardChecker();
        final TargetFactoryFactory factoryFactory = new FileTargetFactoryFactory();
        
        return new JADT(sourceFactory, parser, checker, docEmitter, factoryFactory);
    }
    
    /**
     * Constructs a driver with the given components.
     * 
     * @param parser Parser to read JADT files
     * @param checker Checker to validate JADT structures
     * @param emitter Emitter to spit out Java files
     */
    public JADT(SourceFactory sourceFactory, Parser parser, Checker checker, DocEmitter emitter, TargetFactoryFactory factoryFactory) {
        super();
        this.sourceFactory = sourceFactory;
        this.parser = parser;
        this.emitter = emitter;
        this.checker = checker;
        this.factoryFactory = factoryFactory;
    }
    
    /**
     * Do the JADT thing based on an array of String args.  There must be 2 and the must be the source file and destination directory
     * 
     * @param args
     */
    public void parseAndEmit(String[] args) {
        if (args.length != 2) {
            throw new IllegalArgumentException("usage: java sfdc.adt.JADT [source file] [output directory]");
        }
        
        final String srcFileName = args[0];
        final String destDirName = args[1];

        parseAndEmit(srcFileName, destDirName);        
    }

    /**
     * Do the JADT thing given the srceFileName and destination directory
     * 
     * @param srcFileName full name of the source directory
     * @param destDir full name of the desintation directory (trailing slash is optional)
     */
    public void parseAndEmit(String srcFileName, String destDir) {
        final Source source = sourceFactory.createSource(srcFileName);
        try {
            final Doc doc = parser.parse(source.getSrcInfo(), source.getReader());
            final Set<SemanticException> errors = checker.check(doc);
            if (!errors.isEmpty()) {
                throw new SemanticExceptions(errors);
            }
            final TargetFactory targetFactory = factoryFactory.createTargetFactory(destDir);
            emitter.emit(targetFactory, doc);
        } finally {
            source.close();
        }
    }

}
