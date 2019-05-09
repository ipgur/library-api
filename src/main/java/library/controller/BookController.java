/*
 * Copyright 2019 igur.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package library.controller;

import library.configuration.ApiConfiguration;
import library.database.BookRepository;
import library.exceptions.InternalServerError;
import library.model.Book;
import library.tools.FileTools;
import net.kaczmarzyk.spring.data.jpa.domain.LikeIgnoreCase;
import net.kaczmarzyk.spring.data.jpa.web.annotation.Spec;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * Book Controller for the rest endpoints
 */
@RestController
public class BookController {

    @Autowired
    private transient BookRepository bookRepository;

    /**
     * Provide list of books.
     *
     * @return provide list of book.
     */
    @GetMapping(value = "/books")
    public List<Book> getListOfBooks() {
        return bookRepository.findAll();
    }

    /**
     * Provide list of books filtered by name containing search string.
     *
     * @param spec the search spec
     * @return provide list of books.
     */
    @GetMapping(value = "/books",
            params = {ApiConfiguration.SORT_BY_TITLE})
    public List<Book> getListOfBooksBySpec(@Spec(path = ApiConfiguration.SORT_BY_TITLE, spec = LikeIgnoreCase.class)
                                               final Specification<Book> spec) {
        return bookRepository.findAll(spec);
    }

    /**
     * Provide list of books as HTML.
     *
     * @return provide list of books rendered into HTML.
     */
    @GetMapping(value = "/books", produces = MediaType.TEXT_HTML_VALUE)
    public String renderHtml() {
        String content = null;
        try {
            content = FileTools.readResourceFile(ApiConfiguration.BOOK_HTML);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (content != null) {

            return content;
        }

        throw new InternalServerError("Failed to provide HTML for book");
    }

    /**
     * Provide list of book filtered by name containing search string.
     *
     * @param response to be returned
     * @param page the page to be queried
     * @return provide list of books.
     */
    @GetMapping(value = "/books/{page}")
    public List<Book> getListOfBooksByNamePageable(HttpServletResponse response, @PathVariable Integer page) {
        response.addHeader(ApiConfiguration.X_TOTAL_COUNT_HEADER, String.valueOf(bookRepository.count()));
        return bookRepository.findAll(PageRequest.of(page, ApiConfiguration.PAGE_SIZE, Sort.by(ApiConfiguration.SORT_BY_TITLE))).getContent();
    }

}
