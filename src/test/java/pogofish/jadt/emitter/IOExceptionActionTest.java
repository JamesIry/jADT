package pogofish.jadt.emitter;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;

public class IOExceptionActionTest {
    @Test
    public void testNoException() {
        final String result = new IOExceptionAction<String>() {

            @Override
            public String doAction() throws IOException {
                return "hello";
            }
        }.execute();

        assertEquals("hello", result);
    }

    @Test
    public void testException() {
        try {
            final String result = new IOExceptionAction<String>() {

                @Override
                public String doAction() throws IOException {
                    throw new IOException("uh oh");
                }
            }.execute();
            fail("Execution did not cause an exception got " + result);
        } catch (RuntimeException e) {
            assertTrue("Contained exception was the wrong type", e.getCause() instanceof IOException);
        }
    }

}
