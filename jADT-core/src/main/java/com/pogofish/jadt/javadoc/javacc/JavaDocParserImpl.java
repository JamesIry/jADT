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
     * look ahead n tokens. 0 is the current token, 1 is the next token, 2 the
     * one after that, etc
     * 
     * @param n
     * @return
     */
    private Token lookahead(int n) {
        Token current = token;
        for (int i = 0; i < n; i++) {
            if (current.next == null) {
                current.next = token_source.getNextToken();
            }
            current = current.next;
        }
        return current;
    }
    
    /**
     * Return the whitespace attached to the next token
     */
    @Override
    protected List<JDToken> nextTokenWhitespace() {
        final Token next = lookahead(1);
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
