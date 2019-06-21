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

import library.configuration.ApiConstants;
import library.configuration.persistence.PersistenceConfiguration;
import library.entities.Author;
import library.entities.Book;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
@ContextConfiguration(classes = {PersistenceConfiguration.class, BookRepositoryTest.Config.class})
@TestPropertySource(locations="classpath:application.properties")
@ActiveProfiles("test")
public class BookRepositoryTest {

    @Configuration
    static class Config {

        @Bean
        public DataSource dataSourceForTest() {
            return new EmbeddedDatabaseBuilder()
                    .generateUniqueName(true)
                    .setType(EmbeddedDatabaseType.H2)
                    .setScriptEncoding("UTF-8")
                    .ignoreFailedDrops(true)
                    .addScript("schema.sql")
                    .addScripts("data.sql")
                    .build();
        }
    }

    @Autowired
    private BookRepository bookRepository;

    @Test
    public void testGetAllBooks() {
        List<Book> books = bookRepository.findAll();
        assertThat(books.size(), is(4));
    }

    @Test
    public void testGetAllBooksWithPagination() {
        List<Book> page1 = bookRepository.findAllNoCache(PageRequest.of(0, 2,
                Sort.by(ApiConstants.SORT_BY_TITLE))).getContent();
        assertThat(page1.size(), is(2));
        List<Book> page2 = bookRepository.findAllNoCache(PageRequest.of(1, 2,
                Sort.by(ApiConstants.SORT_BY_TITLE))).getContent();
        assertThat(page2.size(), is(2));
        List<Book> page3 = bookRepository.findAllNoCache(PageRequest.of(2, 2,
                Sort.by(ApiConstants.SORT_BY_TITLE))).getContent();
        assertThat(page3.size(), is(0));
    }

}
