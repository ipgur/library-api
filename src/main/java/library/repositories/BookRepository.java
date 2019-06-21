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
package library.repositories;

import library.entities.Book;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

/**
 * CRUD Access to book data.
 */
public interface BookRepository extends JpaRepository<Book, Integer>,
        JpaSpecificationExecutor<Book> {
    /**
     * @return all of the books
     */
    @Override
    List<Book> findAll();

    /**
     * @param spec the search spec
     * @return list of books matching the searching criteria
     */
    @Override
    @Cacheable("booksBySpec")
    List<Book> findAll(final Specification<Book> spec);

    @Override
    Page<Book> findAll(org.springframework.data.domain.Pageable pageable);

    @Cacheable("booksByPage")
    default Page<Book> findAllAndCache(org.springframework.data.domain.Pageable pageable) {
        return findAll(pageable);
    }

    default Page<Book> findAllNoCache(org.springframework.data.domain.Pageable pageable) {
        return findAll(pageable);
    }

    @Override
    long count();

    @Cacheable("booksCount")
    default long countAndCache() {
        return count();
    }

    default long countNoCache() {
        return count();
    }
}
