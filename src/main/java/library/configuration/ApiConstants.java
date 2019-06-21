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
package library.configuration;

/**
 * Configuration class containing some constant for API related flows
 */
public class ApiConstants {

    /**
     * private c'tor
     */
    private ApiConstants() {
        throw new AssertionError("no instances allowed");
    }

    /**
     * the default PAGE size used used for pagination calls
     */
    public static final int PAGE_SIZE = 30;

    /**
     * name of the header to pass the total count of entries available
     */
    public static final String X_TOTAL_COUNT_HEADER = "x-total-count";

    /**
     * name of the title column
     */
    public static final String SORT_BY_TITLE = "title";

    /**
     * name of the name column
     */
    public static final String SORT_BY_NAME = "name";

    /**
     * name of the authors.html file
     */
    public static final String AUTHORS_HTML = "authors.html";

    /**
     * name of the book.html file
     */
    public static final String BOOK_HTML = "books.html";
}
