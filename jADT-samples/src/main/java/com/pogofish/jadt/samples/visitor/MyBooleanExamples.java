package com.pogofish.jadt.samples.visitor;

public class MyBooleanExamples {
    public MyBoolean computeABoolean() {
        return new MyTrue();
    }
    
    // START SNIPPET: getAString
    public String getAString(MyBoolean x) {
        return x.accept(new MyBooleanVisitor<String>() {
            @Override
            public String visitMyTrue(MyTrue x) {
                return "yup";
            }

            @Override
            public String visitMyFalse(MyFalse x) {
                return "nope";
            }
        });
    }
    // END SNIPPET: getAString
}
