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
package library.configuration;

import library.tools.ShortCircuit;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ShortCircuitConfiguration {

    @Bean(name="RedisShortCircuit")
    public ShortCircuit providesRedisShortCircuit(@Value("${redis.short_circuit.max_errors:6}") int maxErrors,
                                                  @Value("${redis.short_circuit.time_interval:120}") int timeInterval) {
        System.out.println("maxErrors: " + maxErrors);
        return new ShortCircuit.ShortCircuitBuilder().maxNumberErrors(maxErrors).timeWindowSec(timeInterval).build();
    }

    @Bean(name="SimpleCacheShortCircuit")
    public ShortCircuit providesSimpleCacheShortCircuit() {
        return new ShortCircuit.ShortCircuitBuilder().maxNumberErrors(6).timeWindowSec(120).build();
    }
}
