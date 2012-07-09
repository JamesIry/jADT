package com.pogofish.jadt.javadoc.javacc;

import static com.pogofish.jadt.ast.JDToken._JDWhiteSpace;

import java.io.Reader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.pogofish.jadt.ast.JDToken;
import com.pogofish.jadt.comments.javacc.generated.BaseJavaDocParserImpl;
import com.pogofish.jadt.comments.javacc.generated.Token;

public class JavaDocParserImpl extends BaseJavaDocParserImpl {

    public JavaDocParserImpl(Reader stream) {
        super(stream);
    }
    
    /**
     * look ahead 1 token
     */
    public Token lookahead() {
        Token current = token;
        if (current.next == null) {
            current.next = token_source.getNextToken();
        }
        return current.next;
    }
    
    /**
     * Return the whitespace attached to the next token
     */
    @Override
    protected List<JDToken> nextTokenWhitespace() {
        final Token next = lookahead();
        return whiteSpace(next);
    }
    
    /**
     * Return all the comments attached to the specified token
     */
    protected List<JDToken> whiteSpace(Token token) {
        final List<JDToken> wss = new ArrayList<JDToken>();
        Token ws = token.specialToken;
        while(ws != null) {
            switch(ws.kind) {
            case WS:
                wss.add(_JDWhiteSpace(ws.image));
                break;
            default:
                // anything else is not whitespace and not our problem.
            }
            ws = ws.specialToken;
        }
        Collections.reverse(wss);
        return wss;
    }    
}
