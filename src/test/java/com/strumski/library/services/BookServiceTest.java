package com.strumski.library.services;

import com.strumski.library.repositories.BookRepository;
import com.strumski.library.util.DataSourceTestConfig;
import com.strumski.library.configuration.caching.CacheConfig;
import com.strumski.library.configuration.caching.CustomCacheErrorHandler;
import com.strumski.library.configuration.persistence.PersistenceConfiguration;
import com.strumski.library.entities.Book;
import com.strumski.library.tools.CircuitBreaker;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.ConcurrentMap;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
@ContextConfiguration(classes = {BookServiceTest.Config.class, PersistenceConfiguration.class, DataSourceTestConfig.class,
        BookService.class, CacheConfig.class, CustomCacheErrorHandler.class})
@TestPropertySource(locations="classpath:application.properties")
@ActiveProfiles("test")
public class BookServiceTest {

    @Configuration
    static class Config {
        @Bean(name = "CacheCircuitBreaker")
        public CircuitBreaker providesSimpleCacheShortCircuit() {
            return Mockito.mock(CircuitBreaker.class);
        }

        @Bean
        public CacheManager cacheManager() {
            System.out.println("Creating cache manager....");
            return new ConcurrentMapCacheManager("booksCount", "booksAll", "booksCount", "booksSpec");
        }
    }

    @Autowired
    private BookService bookService;

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private CircuitBreaker circuitBreaker;

    @Test
    public void getAll() {
        Mockito.when(circuitBreaker.isClosed()).thenReturn(true);
        List<Book> books = bookService.getAll();
        assertEquals(4, books.size());
        CacheManager cacheManager = (CacheManager) applicationContext.getBean("cacheManager");
        Cache cache = cacheManager.getCache("booksAll");
        assertNotNull(cache);
        ConcurrentMap<Object, Object> nativeCache = (ConcurrentMap<Object, Object>) cache.getNativeCache();
        assertEquals(1, nativeCache.size());
    }

    @Test
    public void getAllCircuitOpen() {
        Mockito.when(circuitBreaker.isClosed()).thenReturn(false);
        List<Book> books = bookService.getAll();
        assertEquals(4, books.size());
        CacheManager cacheManager = (CacheManager) applicationContext.getBean("cacheManager");
        Cache cache = cacheManager.getCache("booksAll");
        assertNotNull(cache);
        ConcurrentMap<Object, Object> nativeCache = (ConcurrentMap<Object, Object>) cache.getNativeCache();
        assertEquals(0, nativeCache.size());
    }

    @Test
    public void getCount() {
    }
}