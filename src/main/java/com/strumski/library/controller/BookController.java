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
package com.strumski.library.controller;

import com.codahale.metrics.annotation.Metered;
import com.codahale.metrics.annotation.Timed;
import com.strumski.library.ApiConstants;
import com.strumski.library.exceptions.InternalServerError;
import com.strumski.library.entities.Book;
import com.strumski.library.services.BookService;
import com.strumski.library.tools.FileTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * Book Controller for the rest endpoints
 */
@RestController
public class BookController {

    @Autowired
    private transient BookService bookService;

    @Value("${static.html.books:books.html}")
    private String booksHTMLPath;

    /**
     * Provide list of books.
     *
     * @return provide list of book.
     */
    @GetMapping(value = "/books")
    public List<Book> getListOfBooks() {
        return bookService.getAll();
    }

    /**
     * Provide list of books as HTML.
     *
     * @return provide list of books rendered into HTML.
     */
    @GetMapping(value = {"/books", "/"}, produces = MediaType.TEXT_HTML_VALUE)
    @Timed(name = "renderBooks")
    @Metered
    public String renderHtml() {
        String content = null;
        try {
            content = FileTools.readResourceFile(booksHTMLPath);
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
    @Timed(name = "getPageBooks")
    @Metered
    @GetMapping(value = "/books/{page}")
    @Transactional(readOnly = true)
    public List<Book> getListOfBooksByNamePageable(HttpServletResponse response, @PathVariable Integer page) {
        try {
            response.addHeader(ApiConstants.X_TOTAL_COUNT_HEADER,
                    String.valueOf(bookService.getCount()));
            return bookService.getBooks(PageRequest.of(page, ApiConstants.PAGE_SIZE,
                    Sort.by(ApiConstants.SORT_BY_TITLE)));
        } catch(IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Non existing page", e);
        }

    }

}
