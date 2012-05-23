package pogofish.jadt.checker;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static pogofish.jadt.util.Util.list;

import java.util.Set;

import org.junit.Test;

import pogofish.jadt.ast.*;
import pogofish.jadt.util.Util;

public class CheckerTest {
    
    @Test
    public void testDuplicateDataType() {
        final Checker checker = new StandardChecker();
        final DataType dataType = new DataType("Foo", list(new Constructor("Foo", Util.<Arg>list())));
        final Doc doc = new Doc("CheckerTest", "", Util.<String>list(), list(dataType, dataType));
        final Set<SemanticException> errors = checker.check(doc);
        assertEquals(1, errors.size());
        assertTrue(errors.contains(new DuplicateDataTypeException(dataType)));
    }
    
    @Test
    public void testDuplicateConstructor() {
        final Checker checker = new StandardChecker();
        final Constructor constructor = new Constructor("Bar", Util.<Arg>list());
        final DataType dataType = new DataType("Foo", list(constructor, constructor));
        final Doc doc = new Doc("CheckerTest", "", Util.<String>list(), list(dataType));
        final Set<SemanticException> errors = checker.check(doc);
        assertEquals(1, errors.size());
        assertTrue(errors.contains(new DuplicateConstructorException(dataType, constructor)));
    }
    
    @Test
    public void testConstructorDataTypeConflict() {
        final Checker checker = new StandardChecker();
        final Constructor constructor1 = new Constructor("Bar", Util.<Arg>list());
        final Constructor constructor2 = new Constructor("Foo", Util.<Arg>list());
        final DataType dataType = new DataType("Foo", list(constructor1, constructor2));
        final Doc doc = new Doc("CheckerTest", "", Util.<String>list(), list(dataType));
        final Set<SemanticException> errors = checker.check(doc);
        assertEquals(1, errors.size());
        assertTrue(errors.contains(new ConstructorDataTypeConflictException(dataType, constructor2)));
    }    
}
