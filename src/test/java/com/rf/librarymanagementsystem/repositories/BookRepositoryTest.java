package com.rf.librarymanagementsystem.repositories;

import com.rf.librarymanagementsystem.models.Book;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class BookRepositoryTest {

    @Autowired
    private BookRepository underTest;

    @AfterEach
    void tearDown() {
        underTest.deleteAll();
    }

    @Test
    void itShouldCheckIfBookTitleExists() {
        // given
        String title = "book";
        Book book = Book.builder()
                .title(title)
                .ISBN("test")
                .author("test")
                .publicationYear(2020)
                .build();

        underTest.save(book);
        // when
        boolean expected = underTest.existsByTitle(title);
        // then
        assertThat(expected).isTrue();
    }

    @Test
    void itShouldCheckIfBookTitleNotExists() {
        // given
        String title = "book";
        // when
        boolean expected = underTest.existsByTitle(title);
        // then
        assertThat(expected).isFalse();
    }
}