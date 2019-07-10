package com.strumski.library.controller;

import com.strumski.library.ApiConstants;
import com.strumski.library.services.BookAuthorService;
import com.strumski.library.util.BookAuthorFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
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
@WebMvcTest(BookAuthorController.class)
@ContextConfiguration(classes = {BookAuthorController.class})
@ActiveProfiles("test")
@EnableWebMvc
public class BookAuthorControllerTest {

    @Autowired
    private BookAuthorController bookAuthorController;

    @Autowired
    MockMvc mvc;

    @MockBean
    private BookAuthorService service;

    @Test
    public void testAuthors() throws Exception {
        given(service.getAuthors(Sort.by(ApiConstants.SORT_BY_NAME))).
                willReturn(Arrays.asList(BookAuthorFactory.generateRandomAuthor(),
                BookAuthorFactory.generateRandomAuthor()));
        mvc.perform(get("/books/authors")
                .accept(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    public void testRenderHTML() throws Exception {
        mvc.perform(get("/books/authors")
                .accept(MediaType.TEXT_HTML)
                .contentType(MediaType.TEXT_HTML))
                .andExpect(status().isOk())
                .andExpect(handler().handlerType(BookAuthorController.class))
                .andExpect(handler().methodName("renderHtml"));
    }

    @Test
    @DirtiesContext
    public void testRenderHTMLInternalError() throws Exception {
        ReflectionTestUtils.setField(bookAuthorController, "authorsHTMLPath", "non_existing");

        mvc.perform(get("/books/authors")
                .accept(MediaType.TEXT_HTML)
                .contentType(MediaType.TEXT_HTML))
                .andExpect(status().is5xxServerError());
    }

}
