package com.strumski.library.controller;

import com.strumski.library.services.BookAuthorService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(BookAuthorController.class)
@ContextConfiguration(classes = {BookAuthorController.class})
@EnableWebMvc
public class BookAuthorControllerTest {

    @Autowired
    private BookAuthorController bookAuthorController;

    @Autowired
    MockMvc mvc;

    @MockBean
    private BookAuthorService service;

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
