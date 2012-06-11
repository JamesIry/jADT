package pogofish.jadt.samples.visitor;

//START SNIPPET: class
public class MyFalse implements MyBoolean {
    @Override
    public <T> T accept(MyBooleanVisitor<T> visitor) {
        return visitor.visitMyFalse(this);
    }
}
//END SNIPPET: class