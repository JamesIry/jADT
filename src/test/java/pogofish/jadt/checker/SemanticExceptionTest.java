package pogofish.jadt.checker;

import static org.junit.Assert.*;

import org.junit.Test;

public class SemanticExceptionTest {

    @Test
    public void testConstructorDataTypeConflictException() {        
        final ConstructorDataTypeConflictException ex1 = new ConstructorDataTypeConflictException("Foo", "Bar");
        final ConstructorDataTypeConflictException ex2 = new ConstructorDataTypeConflictException("Foo", "Bar");
        final ConstructorDataTypeConflictException ex3 = new ConstructorDataTypeConflictException("Baz", "Bar");
        final ConstructorDataTypeConflictException ex4 = new ConstructorDataTypeConflictException("Foo", "Baz");
        checkEquals(ex1, ex1);
        checkEquals(ex1, ex2);
        checkNotEquals(ex1, ex3);
        checkNotEquals(ex1, ex4);
        checkNotEquals(ex1, null);
        checkNotEquals(ex1, "hello");
    }
    
    @Test 
    public void testDuplicateConstructorException() {        
        final DuplicateConstructorException ex1 = new DuplicateConstructorException("Foo", "Bar");
        final DuplicateConstructorException ex2 = new DuplicateConstructorException("Foo", "Bar");
        final DuplicateConstructorException ex3 = new DuplicateConstructorException("Baz", "Bar");
        final DuplicateConstructorException ex4 = new DuplicateConstructorException("Foo", "Baz");
        checkEquals(ex1, ex1);
        checkEquals(ex1, ex2);
        checkNotEquals(ex1, ex3);
        checkNotEquals(ex1, ex4);
        checkNotEquals(ex1, null);
        checkNotEquals(ex1, "hello");
    }
    
    @Test 
    public void testDuplicateDataTypeException() {        
        final DuplicateDataTypeException ex1 = new DuplicateDataTypeException("Foo");
        final DuplicateDataTypeException ex2 = new DuplicateDataTypeException("Foo");
        final DuplicateDataTypeException ex3 = new DuplicateDataTypeException("Baz");
        checkEquals(ex1, ex1);
        checkEquals(ex1, ex2);
        checkNotEquals(ex1, ex3);
        checkNotEquals(ex1, null);
        checkNotEquals(ex1, "hello");
    }
    
    private void checkEquals(Object obj1, Object obj2) {
        assertEquals(obj1, obj2);
        assertTrue(obj1 + " did not have the same hashCode as " + obj2, obj1.hashCode() == obj2.hashCode());
    }
    
    private void checkNotEquals(Object obj1, Object obj2) {
        assertFalse(obj1 + " did not have the same hashCode as " + obj2, obj1.equals(obj2));
        if (obj1 != null && obj2 != null) {
            assertFalse(obj1 + " had the same hashCode as " + obj2, obj1.hashCode() == obj2.hashCode());
        }
    }
}
