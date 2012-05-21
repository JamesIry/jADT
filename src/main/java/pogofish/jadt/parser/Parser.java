package pogofish.jadt.parser;

import java.io.IOException;
import java.io.Reader;

import pogofish.jadt.ast.Doc;


public interface Parser {

    public abstract Doc parse(String srcInfo, Reader reader) throws IOException;

}