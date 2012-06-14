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
package com.pogofish.jadt;

import java.util.Set;
import java.util.logging.Logger;

import com.pogofish.jadt.ast.DataType;
import com.pogofish.jadt.ast.Doc;
import com.pogofish.jadt.checker.*;
import com.pogofish.jadt.emitter.*;
import com.pogofish.jadt.parser.*;
import com.pogofish.jadt.source.*;
import com.pogofish.jadt.target.*;
import com.pogofish.jadt.util.Util;


/**
 * Programmatic and command line driver that launches parser, then checker, then emitter, weaving everything together
 *
 * @author jiry
 */
public class JADT {
	private static final Logger logger = Logger.getLogger(JADT.class.toString());
	
    public static final String TEST_CLASS_NAME = "someClass";
    private static final String TEST_STRING = "hello";
    public static final String TEST_SRC_INFO = "source";
    public static final String TEST_DIR = "test dir";    
    
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
    	logger.fine("Using standard configuration.");
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
    	logger.finest("Checking command line arguments.");
        if (args.length != 2) {
        	final String version = new Version().getVersion();
        	logger.info("JADT version " + version + ".");
        	logger.info("Not enough arguments provided to JADT");
        	logger.info("usage: java sfdc.adt.JADT [source file] [output directory]");
            throw new IllegalArgumentException("\nJADT version " + version + "\nusage: java sfdc.adt.JADT [source file] [output directory]");
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
    	final String version = new Version().getVersion();
    	logger.info("JADT version " + version + ".");
    	logger.info("Will read from srcFile " + srcFileName);
    	logger.info("Will write to destDir " + destDir);
   	
        final Source source = sourceFactory.createSource(srcFileName);
        try {
            final Doc doc = parser.parse(source);
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

    /**
     * Create a dummy configged JADT based on the provided checker and target factory
     */
    public static JADT createDummyJADT(Checker checker, TargetFactoryFactory factory) {
        final SourceFactory sourceFactory = new StringSourceFactory(TEST_STRING);
        final Doc doc = new Doc(TEST_SRC_INFO, "pkg", Util.<String> list(), Util.<DataType> list());
        final DocEmitter docEmitter = new DummyDocEmitter(doc,  TEST_CLASS_NAME);
        final Parser parser = new DummyParser(doc, TEST_SRC_INFO, TEST_STRING);
        final JADT jadt = new JADT(sourceFactory, parser, checker, docEmitter, factory);
        return jadt;
    }    

}
