package com.strumski.library.util;

import com.strumski.library.entities.Author;
import com.strumski.library.entities.Book;
import org.apache.commons.lang3.RandomStringUtils;

import java.util.Random;

public class BookAuthorFactory {

    public static Book generateRandomBook() {
        return Book.builder()
                .title(RandomStringUtils.randomAlphabetic(5))
                .description(RandomStringUtils.randomAlphabetic(10))
                .id(new Random().nextInt(100))
                .build();
    }

    public static Author generateRandomAuthor() {
        return Author.builder()
                .name(RandomStringUtils.randomAlphabetic(5))
                .birthplace(RandomStringUtils.randomAlphabetic(10))
                .id(new Random().nextInt(100))
                .build();
    }
}
