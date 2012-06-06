package pogofish.jadt.samples.ast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.junit.Test;

import static java.util.Arrays.asList;
import static org.junit.Assert.*;
import static pogofish.jadt.samples.ast.Arg._Arg;
import static pogofish.jadt.samples.ast.Expression.*;
import static pogofish.jadt.samples.ast.Function._Function;
import static pogofish.jadt.samples.ast.Statement.*;
import static pogofish.jadt.samples.ast.Type._Int;
/**
 * Make sure the sample usage does what it says it does
 * 
 * @author jiry
 */
public class UsageTest {
	static final Usage usage = new Usage();
	@Test
	public void testSampleFunction() {
		final Function expectedFunction = _Function(_Int(), "addTwo", asList(_Arg(_Int(), "x"), _Arg(_Int(), "y")), asList(
	               _Return(_Add(_Variable("x"), _Variable("y")))
	               ));

		assertEquals(expectedFunction, usage.sampleFunction());
	}
	
	@Test
	public void testExpressionLiterals() {
		final Set<Integer> emptyIntegers = usage.expressionLiterals(_LongLiteral(432L));
		assertTrue("Set not empty, got " + emptyIntegers, emptyIntegers.isEmpty());
		
		final Set<Integer> twoIntegers = usage.expressionLiterals(_Add(_Variable("Foo"), _Add(_IntLiteral(2), _IntLiteral(5))));
		assertTrue("Wrong number of elements in set", twoIntegers.size() == 2);
		assertTrue("Set didn't have 2", twoIntegers.contains(2));
		assertTrue("Set didn't have 5", twoIntegers.contains(5));
	}
	
	@Test
	public void testHasReturn() {
		final List<Statement> noReturn = Arrays.asList(_Declaration(_Int(), "Foo", _IntLiteral(2)), _Assignment("foo", _IntLiteral(3)));
		assertFalse("Got the wrong answer from a list with no returns", usage.hasReturn(noReturn));
		final List<Statement> hasReturn = new ArrayList<Statement>(noReturn);
		hasReturn.add(_Return(_LongLiteral(3)));
		assertTrue("Got wrong answer from a list with a return", usage.hasReturn(hasReturn));
	}
}
