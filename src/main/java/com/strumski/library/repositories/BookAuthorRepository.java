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
package com.strumski.library.repositories;

import com.strumski.library.entities.Author;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * CRUD Access to author data.
 */
@Service
public interface BookAuthorRepository extends JpaRepository<Author, String>,
        JpaSpecificationExecutor<Author> {

    /**
     * @return list of  all authors
     */
    @Override
    @Cacheable("allAuthors")
    List<Author> findAll();

    /**
     * @return list of  all authors matching the search criteria
     */
    @Override
        @Cacheable("authorsBySpec")
    List<Author> findAll(final Specification<Author> spec);

    /**
     * @return list of  all authors
     */
    @Override
    @Cacheable("authorsBySort")
    List<Author> findAll(Sort sort);
}

