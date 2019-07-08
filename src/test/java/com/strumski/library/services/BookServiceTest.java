package com.strumski.library.services;

import com.strumski.library.repositories.BookRepository;
import com.strumski.library.entities.Book;
import com.strumski.library.tools.CircuitBreaker;
import com.strumski.library.util.BookFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.*;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {BookServiceTest.Config.class, BookService.class})
@TestPropertySource(locations="classpath:application.properties")
@ActiveProfiles("test")
public class BookServiceTest {

    @Configuration
    @EnableCaching
    static class Config {

        @Bean(name = "CacheCircuitBreaker")
        public CircuitBreaker providesSimpleCacheShortCircuit() {
            return Mockito.mock(CircuitBreaker.class);
        }

        @Bean
        @Primary
        public BookRepository provideDAO() {
            return Mockito.mock(BookRepository.class);
        }

        @Bean
        public CacheManager cacheManager() {
            System.out.println("Creating cache manager....");
            return new ConcurrentMapCacheManager("booksCount", "booksAll", "booksByPage", "booksSpec");
        }
    }

    @Autowired
    private BookService bookService;

    @Autowired
    private CircuitBreaker circuitBreaker;

    @Autowired
    private BookRepository dao;

    @Before
    public void setUp() {
        Mockito.when(circuitBreaker.isClosed()).thenReturn(true);
        Mockito.when(dao.findAll()).thenReturn(Arrays.asList(
                BookFactory.generateRandomBook(), BookFactory.generateRandomBook(), BookFactory.generateRandomBook()
        ));
        Mockito.when(dao.findAll(PageRequest.of(1, 2))).thenReturn(new PageImpl<>(Arrays.asList(
                BookFactory.generateRandomBook(), BookFactory.generateRandomBook())));
        Mockito.when(dao.count()).thenReturn((long) 3);
    }

    @Test
    @DirtiesContext
    public void getAll() {
        List<Book> books = bookService.getAll();
        assertEquals(3, books.size());
        // call it one more time, we should get it from the cache now
        bookService.getAll();
        Mockito.verify(dao, Mockito.times(1)).findAll();
    }

    @Test
    @DirtiesContext
    public void getAllCircuitOpen() {
        Mockito.when(circuitBreaker.isClosed()).thenReturn(false);
        List<Book> books = bookService.getAll();
        assertEquals(3, books.size());
        bookService.getAll();
        Mockito.verify(dao, Mockito.times(2)).findAll();
    }

    @Test
    @DirtiesContext
    public void getPage() {
        List<Book> books = bookService.getBooks(PageRequest.of(1, 2));
        assertEquals(2, books.size());
        // call it one more time, we should get it from the cache now
        bookService.getBooks(PageRequest.of(1, 2));
        Mockito.verify(dao, Mockito.times(1)).findAll(Mockito.any(PageRequest.class));
    }

    @Test
    @DirtiesContext
    public void getPageCircuitOpen() {
        Mockito.when(circuitBreaker.isClosed()).thenReturn(false);
        List<Book> books = bookService.getBooks(PageRequest.of(1, 2));
        assertEquals(2, books.size());
        // call it one more time, we should get it from the cache now
        bookService.getBooks(PageRequest.of(1, 2));
        Mockito.verify(dao, Mockito.times(2)).findAll(Mockito.any(PageRequest.class));
    }

    @Test
    @DirtiesContext
    public void booksCount() {
        final long count = bookService.getCount();
        assertEquals(3, count);
        // call it one more time, we should get it from the cache now
        bookService.getCount();
        Mockito.verify(dao, Mockito.times(1)).count();
    }

    @Test
    @DirtiesContext
    public void booksCountCircuitOpen() {
        Mockito.when(circuitBreaker.isClosed()).thenReturn(false);
        final long count = bookService.getCount();
        assertEquals(3, count);
        // call it one more time, we should get it from the cache now
        bookService.getCount();
        Mockito.verify(dao, Mockito.times(2)).count();
    }
}