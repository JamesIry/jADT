package com.pogofish.jadt.samples.visitor;

// START SNIPPET: class
public class MyTrue implements MyBoolean {
    @Override
    public <T> T accept(MyBooleanVisitor<T> visitor) {
        return visitor.visitMyTrue(this);
    }
}
// END SNIPPET: class