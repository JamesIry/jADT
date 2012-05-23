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

import java.io.*;
import java.util.Set;

import pogofish.jadt.ast.Doc;
import pogofish.jadt.checker.*;
import pogofish.jadt.emitter.*;
import pogofish.jadt.parser.Parser;
import pogofish.jadt.parser.StandardParser;


public class JADT {
    private final Parser parser;
    private final DocEmitter emitter;
    private final Checker checker;
    
    public JADT(Parser parser, Checker checker, DocEmitter emitter) {
        super();
        this.parser = parser;
        this.emitter = emitter;
        this.checker = checker;
    }

    public static void main(String[] args) throws Exception {
        if (args.length != 2) {
            System.err.println("usage: java sfdc.adt.ADT [source file] [output directory]");
            System.exit(1);
        }
        final String srcFileName = args[0];
        final String destDirName = args[1];
        
        final JADT adt = new JADT(new StandardParser(), new StandardChecker(), new StandardDocEmitter(new FileTargetFactory(destDirName), new StandardDataTypeEmitter(new StandardClassBodyEmitter(), new StandardConstructorEmitter(new StandardClassBodyEmitter()))));        
        adt.parseAndEmit(srcFileName);
    }
    
    public void parseAndEmit(String srcFileName) {
        try {
            final File srcFile = new File(srcFileName);
            final Reader reader = new BufferedReader(new InputStreamReader(new FileInputStream(srcFile), "UTF-8"));
            try {
                parseAndEmit(srcFile.getAbsolutePath(), reader);            
            } finally {
                reader.close();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    
    public void parseAndEmit(String srcInfo, Reader src) {
        final Doc doc = parser.parse(srcInfo, src);
        final Set<SemanticException> errors = checker.check(doc);
        if (!errors.isEmpty()) {
            throw new SemanticExceptions(errors);
        }
        emitter.emit(doc);                
    }
}
