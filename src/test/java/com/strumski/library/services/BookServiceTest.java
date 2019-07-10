package com.strumski.library.services;

import com.strumski.library.repositories.BookRepository;
import com.strumski.library.entities.Book;
import com.strumski.library.tools.CircuitBreaker;
import com.strumski.library.util.BookAuthorFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.*;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.BDDMockito.given;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {BookServiceTest.Config.class, BookService.class})
@TestPropertySource(locations="classpath:application.properties")
@ActiveProfiles("test")
public class BookServiceTest {


    @MockBean(name = "CacheCircuitBreaker")
    private CircuitBreaker circuitBreaker;

    @MockBean
    private BookRepository dao;

    @Configuration
    @EnableCaching
    static class Config {

        @Bean
        public CacheManager cacheManager() {
            System.out.println("Creating cache manager....");
            return new ConcurrentMapCacheManager("booksCount", "booksAll", "booksByPage", "booksSpec");
        }
    }

    @Autowired
    private BookService bookService;

    @Before
    public void setUp() {
        given(circuitBreaker.isClosed()).willReturn(true);
        given(dao.findAll()).willReturn(Arrays.asList(
                BookAuthorFactory.generateRandomBook(), BookAuthorFactory.generateRandomBook(), BookAuthorFactory.generateRandomBook()
        ));
        given(dao.findAll(PageRequest.of(1, 2))).willReturn(new PageImpl<>(Arrays.asList(
                BookAuthorFactory.generateRandomBook(), BookAuthorFactory.generateRandomBook())));
        given(dao.count()).willReturn((long) 3);
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
        given(circuitBreaker.isClosed()).willReturn(false);
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
        given(circuitBreaker.isClosed()).willReturn(false);
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
        given(circuitBreaker.isClosed()).willReturn(false);
        final long count = bookService.getCount();
        assertEquals(3, count);
        // call it one more time, we should get it from the cache now
        bookService.getCount();
        Mockito.verify(dao, Mockito.times(2)).count();
    }
}