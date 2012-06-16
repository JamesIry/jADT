package com.pogofish.jadt.samples.visitor;

import static org.junit.Assert.assertEquals;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.junit.Test;

/**
 * Make sure ColorExamples does what it says it does
 */
public class ColorExamplesTest {
    @Test
    public void test() {
        check(new Red(), "red");
        check(new Green(), "green");
        check(new Blue(), "blue");
    }

    private void check(Color color, String string) {
        final ColorExamples usage = new ColorExamples();
        final StringWriter writer = new StringWriter();
        final PrintWriter printWriter = new PrintWriter(writer);
        usage.printString(color, printWriter);
        assertEquals(string, writer.toString());
        
    }
}
