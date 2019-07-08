package com.strumski.library.services;

import com.strumski.library.repositories.BookRepository;
import com.strumski.library.entities.Book;
import com.strumski.library.tools.CircuitBreaker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BookService {

    @Autowired
    private BookRepository dao;

    @Autowired @Qualifier("CacheCircuitBreaker")
    public CircuitBreaker circuitBreaker;

    @Cacheable(value = "booksByPage", condition = "#root.target.circuitBreaker.isClosed() == true")
    public List<Book> getBooks(Pageable pageable) {
        return dao.findAll(pageable).getContent();
    }

    @Cacheable(value = "booksAll", condition = "#root.target.circuitBreaker.isClosed() == true")
    public List<Book> getAll() {
        return dao.findAll();
    }

    @Cacheable(value = "booksCount", condition = "#root.target.circuitBreaker.isClosed() == true")
    public long getCount() {
        return dao.count();
    }
}
