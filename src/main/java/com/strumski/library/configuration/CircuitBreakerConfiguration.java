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
package com.strumski.library.configuration;

import com.strumski.library.tools.CircuitBreaker;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
public class CircuitBreakerConfiguration {

    @Bean(name="CacheCircuitBreaker")
    public CircuitBreaker providesRedisShortCircuit(@Value("${redis.short_circuit.max_errors:6}") int maxErrors,
                                                    @Value("${redis.short_circuit.time_interval:120}") int timeInterval) {
        return new CircuitBreaker.ShortCircuitBuilder().maxNumberErrors(maxErrors).timeWindowSec(timeInterval).build();
    }
}
