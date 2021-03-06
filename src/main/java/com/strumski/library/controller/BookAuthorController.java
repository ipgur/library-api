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
import com.strumski.library.entities.Author;
import com.strumski.library.services.BookAuthorService;
import com.strumski.library.tools.FileTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

/**
 * Author controller for the author endpoints
 */
@RestController
public class BookAuthorController {

    @Autowired
    private transient BookAuthorService service;

    @Value("${static.html.authors:authors.html}")
    private String authorsHTMLPath;

    /**
     * Provide list of authors.
     *
     * @return provide list of authors.
     */
    @GetMapping(value = "/books/authors", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @Timed(name = "getAllAuthors")
    @Metered
    public List<Author> getListOfAuthors() {
        return service.getAuthors(Sort.by(ApiConstants.SORT_BY_NAME));
    }

    /**
     * Provide list of books authors as HTML.
     *
     * @return provide list of book authors rendered into HTML.
     */
    @GetMapping(value = "/books/authors", produces = MediaType.TEXT_HTML_VALUE)
    @Timed(name = "renderAuthors")
    @Metered
    public String renderHtml() {
        String content = null;
        try {
            content = FileTools.readResourceFile(authorsHTMLPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (content != null) {
            return content;
        }

        throw new InternalServerError("Failed to provide HTML for authors");
    }

}
