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

import com.strumski.library.configuration.persistence.PersistenceConfiguration;
import com.strumski.library.entities.Author;
import com.strumski.library.util.DataSourceTestConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
@ContextConfiguration(classes = {PersistenceConfiguration.class, DataSourceTestConfig.class})
@TestPropertySource(locations="classpath:application.properties")
@ActiveProfiles("test")
public class AuthorRepositoryTest {

    @Autowired
    private BookAuthorRepository authorRepository;

    @Test
    public void testGetAllAuthors() {
        List<Author> authors = authorRepository.findAll();
        assertThat(authors.size(), is(3));
        assertThat(authors.get(0).getName(), is("author 1"));
    }

    @Test
    public void testGetAllAuthorsSort() {
        List<Author> authors = authorRepository.findAll(new Sort(Sort.Direction.DESC, "name"));
        assertThat(authors.get(0).getName(), is("author 3"));
    }
}
