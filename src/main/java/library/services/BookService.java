package library.services;

import library.entities.Book;
import library.repositories.BookRepository;
import library.tools.ShortCircuit;
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

    @Autowired @Qualifier("RedisShortCircuit")
    public ShortCircuit shortCircuit;

    @Cacheable(value = "booksByPage", condition = "#root.target.shortCircuit.isClosed() == true")
    public List<Book> getBooksPageable(Pageable pageable) {
        return dao.findAll(pageable).getContent();
    }

    @Cacheable(value = "booksAll", condition = "#root.target.shortCircuit.isClosed() == true")
    public List<Book> getAll() {
        return dao.findAll();
    }

    @Cacheable(value = "booksCount", condition = "#root.target.shortCircuit.isClosed() == true")
    public long getCount() {
        return dao.count();
    }
    @Cacheable(value = "booksBySpec", condition = "#root.target.shortCircuit.isClosed() == true")
    public List<Book> getAll(Specification<Book> spec) {
        return dao.findAll(spec);
    }
}
