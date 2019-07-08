package com.strumski.library.services;

import com.strumski.library.entities.Author;
import com.strumski.library.repositories.BookAuthorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookAuthorService {

    @Autowired
    private BookAuthorRepository dao;

    public List<Author> getAuthors() {
        return dao.findAll();
    }

    public List<Author> getAuthors(Sort sortable) {
        return dao.findAll(sortable);
    }
}
