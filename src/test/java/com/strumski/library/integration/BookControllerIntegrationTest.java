package com.strumski.library.integration;

import com.strumski.library.entities.Author;
import com.strumski.library.entities.Book;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestPropertySource(locations="classpath:application.integrationtest.properties")
@Transactional
public class BookControllerIntegrationTest {

    @Autowired
    private TestRestTemplate client;

    @Test
    public void testGetAllBooks() {
        ResponseEntity<List<Book>> restRequest = client.exchange(
                "http://localhost:8081/books",
                HttpMethod.GET,
                null, new ParameterizedTypeReference<List<Book>>() {});

        List<Book> books = restRequest.getBody();
        System.out.println(books);
        assertEquals(4, books.size());
    }

    @Test
    public void testGetPageOfBooks() {
        ResponseEntity<List<Book>> restRequest = client.exchange(
                "http://localhost:8081/books/0",
                HttpMethod.GET,
                null, new ParameterizedTypeReference<List<Book>>() {});

        List<Book> books = restRequest.getBody();
        System.out.println(books);
        assertEquals(4, books.size());
    }

    @Test
    public void testGetPageBooksNotFound() {
        ResponseEntity<List<Book>> restRequest = client.exchange(
                "http://localhost:8081/books/10",
                HttpMethod.GET,
                null, new ParameterizedTypeReference<List<Book>>() {});

        List<Book> books = restRequest.getBody();
        System.out.println(books);
        assertTrue(books.isEmpty());
    }

    @Test
    public void testGetPageBooksInvalidPage() {
        assertTrue(client.exchange(
                "http://localhost:8081/books/-10",
                HttpMethod.GET, null, Object.class)
                .getStatusCode().is4xxClientError());
    }

    @Test
    public void testGetAllAuthors() {
        ResponseEntity<List<Author>> restRequest = client.exchange(
                "http://localhost:8081/books/authors",
                HttpMethod.GET,
                null, new ParameterizedTypeReference<List<Author>>() {});

        List<Author> authors = restRequest.getBody();
        System.out.println(authors);
        assertEquals(3, authors.size());
    }

}
