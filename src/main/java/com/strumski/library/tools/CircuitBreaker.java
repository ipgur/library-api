/*
 * Copyright 2019 igur.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.strumski.library.tools;

import org.springframework.stereotype.Component;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.atomic.AtomicInteger;

import static java.util.concurrent.TimeUnit.SECONDS;

public class CircuitBreaker {

    private AtomicInteger errors = new AtomicInteger();
    private final int timeWindowSec;
    private final int maxNumberErrors;

    private final ScheduledExecutorService scheduler =
            Executors.newScheduledThreadPool(1);

    private CircuitBreaker(int timeWindowSec, int maxNumberErrors) {
        this.timeWindowSec = timeWindowSec;
        this.maxNumberErrors = maxNumberErrors;
        final Runnable reset = () -> this.errors.set(0);
        scheduler.scheduleAtFixedRate(reset, 0, this.timeWindowSec, SECONDS);
        Runtime.getRuntime().addShutdownHook(new Thread(scheduler::shutdownNow));
    }

    public static ShortCircuitBuilder builder() {
        return new ShortCircuitBuilder();
    }

    public CircuitBreaker incrementErrors() {
        errors.incrementAndGet();
        return this;
    }

    public boolean isClosed() {
        return errors.get() < maxNumberErrors;
    }

    public static class ShortCircuitBuilder {
        private int timeWindowSec;
        private int maxNumberErrors;

        public ShortCircuitBuilder() {}

        public ShortCircuitBuilder timeWindowSec(int timeWindowSec) {
            this.timeWindowSec = timeWindowSec;
            return this;
        }

        public ShortCircuitBuilder maxNumberErrors(int maxNumberErrors) {
            this.maxNumberErrors = maxNumberErrors;
            return this;
        }

        public CircuitBreaker build() {
            return new CircuitBreaker(timeWindowSec, maxNumberErrors);
        }

        public String toString() {
            return "ShortCircuit.ShortCircuitBuilder(timeWindowSec=" + this.timeWindowSec + ", maxNumberErrors=" + this.maxNumberErrors + ")";
        }
    }
}
