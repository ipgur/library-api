package com.strumski.library.tools;

import org.junit.Test;

import static org.junit.Assert.*;

public class CircuitBreakerTest {

    @Test
    public void testCircuitBreaker() throws InterruptedException {
        CircuitBreaker circuitBreaker = new CircuitBreaker.ShortCircuitBuilder()
                .maxNumberErrors(2)
                .timeWindowSec(2)
                .build();
        circuitBreaker.incrementErrors().incrementErrors().incrementErrors();
        assertFalse(circuitBreaker.isClosed());
        Thread.sleep(3000);
        assertTrue(circuitBreaker.isClosed());
    }
}