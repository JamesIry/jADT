package pogofish.jadt;

import java.io.*;

import pogofish.jadt.ast.Doc;
import pogofish.jadt.emitter.*;
import pogofish.jadt.parser.Parser;
import pogofish.jadt.parser.StandardParser;


public class JADT {
    private final Parser parser;
    private final DocEmitter emitter;
    
    public JADT(Parser parser, DocEmitter emitter) {
        super();
        this.parser = parser;
        this.emitter = emitter;
    }

    public static void main(String[] args) throws Exception {
        if (args.length != 2) {
            System.err.println("usage: java sfdc.adt.ADT [source file] [output directory]");
            System.exit(1);
        }
        final String srcFileName = args[0];
        final String destDirName = args[1];
        
        final JADT adt = new JADT(new StandardParser(), new StandardDocEmitter(new FileTargetFactory(destDirName), new StandardDataTypeEmitter()));        
        adt.parseAndEmit(srcFileName);
    }
    
    public void parseAndEmit(String srcFileName) throws IOException {
        final File srcFile = new File(srcFileName);
        final Reader reader = new BufferedReader(new InputStreamReader(new FileInputStream(srcFile), "UTF-8"));
        try {
            parseAndEmit(srcFile.getAbsolutePath(), reader);            
        } finally {
            reader.close();
        }
    }
    
    public void parseAndEmit(String srcInfo, Reader src) throws IOException {
        final Doc doc = parser.parse(srcInfo, src);
        emitter.emit(doc);                
    }
}
