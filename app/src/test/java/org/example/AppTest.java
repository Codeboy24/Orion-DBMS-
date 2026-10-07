package org.example;

import org.junit.Test;
import static org.junit.Assert.*;

public class AppTest {
    @Test
    public void appHasMainClass() {
        Orion classUnderTest = new Orion();
        assertNotNull("App instance should not be null", classUnderTest);
    }
}