package com.strumski.library.controller;

import com.strumski.library.configuration.ApiConstants;
import com.strumski.library.services.BookService;
import com.strumski.library.tools.CircuitBreaker;
import com.strumski.library.util.BookFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.util.Arrays;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(BookController.class)
@ContextConfiguration(classes = {BookControllerTest.Config.class, BookController.class})
public class BookControllerTest {

    @Configuration
    @EnableWebMvc
    static class Config {
        @Bean(name = "CacheCircuitBreaker")
        public CircuitBreaker providesSimpleCacheShortCircuit() {
            return Mockito.mock(CircuitBreaker.class);
        }
    }

    @Autowired
    private BookController bookController;

    @Autowired
    MockMvc mvc;

    @MockBean
    private BookService service;

    @Test
    public void getListOfBooks() throws Exception {
        given(service.getAll()).willReturn(Arrays.asList(BookFactory.generateRandomBook(),
                BookFactory.generateRandomBook()));
        mvc.perform(get("/books")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    public void testRenderHTML() throws Exception {
        mvc.perform(get("/books")
                .accept(MediaType.TEXT_HTML)
                .contentType(MediaType.TEXT_HTML))
                .andExpect(status().isOk())
                .andExpect(handler().handlerType(BookController.class))
                .andExpect(handler().methodName("renderHtml"));
    }

    @Test
    @DirtiesContext
    public void testRenderHTMLInternalError() throws Exception {
        ReflectionTestUtils.setField(bookController, "booksHTMLPath", "non_existing");

        mvc.perform(get("/books")
                .accept(MediaType.TEXT_HTML)
                .contentType(MediaType.TEXT_HTML))
                .andExpect(status().is5xxServerError());
    }


    @Test
    public void getListOfBooksByNamePageable() throws Exception {
        given(service.getBooks(PageRequest.of(1, ApiConstants.PAGE_SIZE, Sort.by(ApiConstants.SORT_BY_TITLE))))
                .willReturn(Arrays.asList(BookFactory.generateRandomBook(),
                BookFactory.generateRandomBook()));
        given(service.getCount()).willReturn((long) 2);

        mvc.perform(get("/books/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(header().longValue(ApiConstants.X_TOTAL_COUNT_HEADER, 2));

    }
}
