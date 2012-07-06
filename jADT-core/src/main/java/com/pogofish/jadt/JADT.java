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

import static com.pogofish.jadt.ast.ASTConstants.NO_COMMENTS;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import com.pogofish.jadt.ast.DataType;
import com.pogofish.jadt.ast.Doc;
import com.pogofish.jadt.ast.Imprt;
import com.pogofish.jadt.ast.ParseResult;
import com.pogofish.jadt.ast.Pkg;
import com.pogofish.jadt.checker.Checker;
import com.pogofish.jadt.checker.DummyChecker;
import com.pogofish.jadt.checker.StandardChecker;
import com.pogofish.jadt.emitter.ClassBodyEmitter;
import com.pogofish.jadt.emitter.ConstructorEmitter;
import com.pogofish.jadt.emitter.DataTypeEmitter;
import com.pogofish.jadt.emitter.DocEmitter;
import com.pogofish.jadt.emitter.DummyDocEmitter;
import com.pogofish.jadt.emitter.StandardClassBodyEmitter;
import com.pogofish.jadt.emitter.StandardConstructorEmitter;
import com.pogofish.jadt.emitter.StandardDataTypeEmitter;
import com.pogofish.jadt.emitter.StandardDocEmitter;
import com.pogofish.jadt.errors.SemanticError;
import com.pogofish.jadt.errors.SyntaxError;
import com.pogofish.jadt.errors.UserError;
import com.pogofish.jadt.parser.DummyParser;
import com.pogofish.jadt.parser.Parser;
import com.pogofish.jadt.parser.StandardParser;
import com.pogofish.jadt.parser.javacc.JavaCCParserImplFactory;
import com.pogofish.jadt.sink.FileSinkFactoryFactory;
import com.pogofish.jadt.sink.SinkFactoryFactory;
import com.pogofish.jadt.source.FileSourceFactory;
import com.pogofish.jadt.source.Source;
import com.pogofish.jadt.source.SourceFactory;
import com.pogofish.jadt.source.StringSourceFactory;
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
    final SinkFactoryFactory factoryFactory;

    /**
     * Takes the names of a source file and output directory and does the jADT thing to them
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
        final Parser parser = new StandardParser(new JavaCCParserImplFactory());
        final Checker checker = new StandardChecker();
        final SinkFactoryFactory factoryFactory = new FileSinkFactoryFactory();
        
        return new JADT(sourceFactory, parser, checker, docEmitter, factoryFactory);
    }
    
    /**
     * Constructs a driver with the given components.
     * 
     * @param parser Parser to read jADT files
     * @param checker Checker to validate jADT structures
     * @param emitter Emitter to spit out java files
     */
    public JADT(SourceFactory sourceFactory, Parser parser, Checker checker, DocEmitter emitter, SinkFactoryFactory factoryFactory) {
        super();
        this.sourceFactory = sourceFactory;
        this.parser = parser;
        this.emitter = emitter;
        this.checker = checker;
        this.factoryFactory = factoryFactory;
    }
    
    /**
     * Do the jADT thing based on an array of String args.  There must be 2 and the must be the source file and destination directory
     * 
     * @param args
     */
    public void parseAndEmit(String[] args) {
    	logger.finest("Checking command line arguments.");
        if (args.length != 2) {
        	final String version = new Version().getVersion();
        	logger.info("jADT version " + version + ".");
        	logger.info("Not enough arguments provided to jADT");
        	logger.info("usage: java sfdc.adt.JADT [source file or directory with .jadt files] [output directory]");
            throw new IllegalArgumentException("\njADT version " + version + "\nusage: java sfdc.adt.JADT [source file or directory with .jadt files] [output directory]");
        }
        
        final String srcPath = args[0];
        final String destDirName = args[1];

        parseAndEmit(srcPath, destDirName);        
    }

    /**
     * Do the jADT thing given the srceFileName and destination directory
     * 
     * @param srcPath full name of the source directory or file
     * @param destDir full name of the destination directory (trailing slash is optional)
     */
    public void parseAndEmit(String srcPath, final String destDir) {    	
    	final String version = new Version().getVersion();
    	logger.info("jADT version " + version + ".");
    	logger.info("Will read from source " + srcPath);
    	logger.info("Will write to destDir " + destDir);
   	
        final List<? extends Source> sources = sourceFactory.createSources(srcPath);
        for (Source source : sources) {
            final List<UserError> errors = new ArrayList<UserError>();
                 
            final ParseResult result = parser.parse(source);
            for (SyntaxError error : result.errors) {
                errors.add(UserError._Syntactic(error));
            }               
            final List<SemanticError> semanticErrors = checker.check(result.doc);
            for (SemanticError error : semanticErrors) {
                errors.add(UserError._Semantic(error));
            }            
            if (!errors.isEmpty()) {
                throw new JADTUserErrorsException(errors);
            }
            emitter.emit(factoryFactory.createSinkFactory(destDir), result.doc);
        }
    }

    /**
     * Create a dummy configged jADT based on the provided syntaxErrors, semanticErrors, testSrcInfo, and sink factory
     * Useful for testing
     */
    public static JADT createDummyJADT(List<SyntaxError> syntaxErrors, List<SemanticError> semanticErrors, String testSrcInfo, SinkFactoryFactory factory) {
        final SourceFactory sourceFactory = new StringSourceFactory(TEST_STRING);
        final Doc doc = new Doc(TEST_SRC_INFO, Pkg._Pkg(NO_COMMENTS, "pkg"), Util.<Imprt> list(), Util.<DataType> list());
        final ParseResult parseResult = new ParseResult(doc, syntaxErrors);
        final DocEmitter docEmitter = new DummyDocEmitter(doc,  TEST_CLASS_NAME);
        final Parser parser = new DummyParser(parseResult, testSrcInfo, TEST_STRING);
        final Checker checker = new DummyChecker(semanticErrors);
        final JADT jadt = new JADT(sourceFactory, parser, checker, docEmitter, factory);
        return jadt;
    }    

}
