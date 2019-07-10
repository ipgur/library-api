package com.strumski.library.services;


import com.strumski.library.ApiConstants;
import com.strumski.library.entities.Author;
import com.strumski.library.repositories.BookAuthorRepository;
import com.strumski.library.util.BookAuthorFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.BDDMockito.given;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {BookAuthorService.class})
@ActiveProfiles("test")
public class BookAuthorServiceTest {

    @Autowired
    private BookAuthorService bookAuthorService;

    @MockBean
    private BookAuthorRepository dao;

    @Test
    public void testGetAuthors() {
        List<Author> mockedAuthors = Arrays.asList(BookAuthorFactory.generateRandomAuthor(),
                                                    BookAuthorFactory.generateRandomAuthor());

        given(dao.findAll()).willReturn(mockedAuthors);

        List<Author> authors = bookAuthorService.getAuthors();
        assertEquals(mockedAuthors, authors);
    }

    @Test
    public void testGetAuthorsSorted() {
        List<Author> mockedAuthors = Arrays.asList(BookAuthorFactory.generateRandomAuthor(),
                BookAuthorFactory.generateRandomAuthor());

        given(dao.findAll(Sort.by(ApiConstants.SORT_BY_NAME))).willReturn(mockedAuthors);

        List<Author> authors = bookAuthorService.getAuthors(Sort.by(ApiConstants.SORT_BY_NAME));
        assertEquals(mockedAuthors, authors);
    }

}
