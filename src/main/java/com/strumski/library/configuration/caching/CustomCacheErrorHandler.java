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
package com.strumski.library.configuration.caching;

import com.strumski.library.tools.CircuitBreaker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.Cache;
import org.springframework.cache.interceptor.CacheErrorHandler;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
public class CustomCacheErrorHandler implements CacheErrorHandler {

    @Autowired @Qualifier("CacheCircuitBreaker")
    private CircuitBreaker circuitBreaker;

    @Override
    public void handleCacheGetError(RuntimeException e, Cache cache, Object o) {
        circuitBreaker.incrementErrors();
    }

    @Override
    public void handleCachePutError(RuntimeException e, Cache cache, Object o, Object o1) {
        circuitBreaker.incrementErrors();
    }

    @Override
    public void handleCacheEvictError(RuntimeException e, Cache cache, Object o) {
        circuitBreaker.incrementErrors();
    }

    @Override
    public void handleCacheClearError(RuntimeException e, Cache cache) {
        circuitBreaker.incrementErrors();
    }
}
