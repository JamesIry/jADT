package pogofish.jadt.samples.visitor;

//START SNIPPET: class
public interface MyBoolean {
    public <T> T accept(MyBooleanVisitor<T> visitor);
}
//END SNIPPET: class