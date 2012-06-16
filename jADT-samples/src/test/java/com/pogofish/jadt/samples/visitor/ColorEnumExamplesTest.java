package com.pogofish.jadt.samples.visitor;

import static org.junit.Assert.assertEquals;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.junit.Test;

/**
 * Make sure ColorEnumExamples does what it says it does
 */
public class ColorEnumExamplesTest {
    @Test
    public void test() {
        check(ColorEnum.Red, "red");
        check(ColorEnum.Green, "green");
        check(ColorEnum.Blue, "blue");
    }

    private void check(ColorEnum color, String string) {
        final ColorEnumExamples usage = new ColorEnumExamples();
        final StringWriter writer = new StringWriter();
        final PrintWriter printWriter = new PrintWriter(writer);
        usage.printString(color, printWriter);
        assertEquals(string, writer.toString());
        
    }
}
