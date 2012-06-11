package pogofish.jadt.samples.visitor;

//START SNIPPET: class
public interface MyBooleanVisitor<ResultType> {
    ResultType visitMyTrue(MyTrue x);
    ResultType visitMyFalse(MyFalse x);
}
//END SNIPPET: class