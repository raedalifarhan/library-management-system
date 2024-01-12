package com.rf.librarymanagementsystem.services;

import com.rf.librarymanagementsystem.exceptions.ApiBadRequestException;
import com.rf.librarymanagementsystem.exceptions.ApiNotFoundException;
import com.rf.librarymanagementsystem.models.Book;
import com.rf.librarymanagementsystem.repositories.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {
    @Mock private BookRepository bookRepository;
    private BookService underTest;

    @Captor
    private ArgumentCaptor<String> titleCaptor;

    // this way to get new underTest for every test
    @BeforeEach
    void setUp() {
        underTest = new BookService(bookRepository);
    }

    @Test
    void canGetAllBooks() {
        // when
        underTest.getAllBooks();
        // then
        verify(bookRepository).findAll();
    }

    @Test
    void canAddBook() {
        // given
        String title = "book";
        Book book = Book.builder()
                .title(title)
                .ISBN("test")
                .author("test")
                .publicationYear(2020)
                .build();
        // when
        underTest.addBook(book);
        // then
        ArgumentCaptor<Book> bookArgumentCaptor = ArgumentCaptor.forClass(Book.class);
        verify(bookRepository)
                .save(bookArgumentCaptor.capture());

        Book capturedBook = bookArgumentCaptor.getValue();

        assertThat(capturedBook).isEqualTo(book);

    }

    @Test
    void willThrowWhenTitleIsTaken() {
        // Given
        String title = "book";
        Book book = Book.builder()
                .title(title)
                .ISBN("test")
                .author("test")
                .publicationYear(2020)
                .build();

        given(bookRepository.existsByTitle(anyString()))
                .willReturn(true);
        // when
        // then
        assertThatThrownBy(() -> underTest.addBook(book))
                .isInstanceOf(ApiBadRequestException.class)
                .hasMessageContaining("Book with title '" + title + "' already exists");

        verify(bookRepository, never()).save(any());
    }

    @Test
    void canGetBookById() {
        // given
        Long id = 1L;
        Book book = new Book();
        given(bookRepository.existsById(id)).willReturn(true);
        given(bookRepository.findById(id)).willReturn(Optional.of(book));

        // when
        Book result = underTest.getBookById(id);

        // then
        assertThat(result).isEqualTo(book);
        verify(bookRepository).findById(id);
    }

    @Test
    void willThrowWhenBookNotFoundById() {
        // given
        Long id = 1L;
        given(bookRepository.existsById(id)).willReturn(false);

        // when/then
        assertThatThrownBy(() -> underTest.getBookById(id))
                .isInstanceOf(ApiNotFoundException.class)
                .hasMessageContaining("Book not found with id: " + id);

        verify(bookRepository, never()).findById(anyLong());
    }

    @Test
    void canUpdateBook() {
        // given
        Long id = 1L;
        String newTitle = "newTitle";
        Book existingBook = new Book();
        existingBook.setTitle("oldTitle");
        Book updatedBook = new Book();
        updatedBook.setTitle(newTitle);

        given(bookRepository.existsById(id)).willReturn(true);
        given(bookRepository.existsByTitle(newTitle)).willReturn(false);
        given(bookRepository.findById(id)).willReturn(Optional.of(existingBook));
        given(bookRepository.save(any(Book.class))).willReturn(updatedBook);

        // when
        Book result = underTest.updateBook(id, updatedBook);

        // then
        assertThat(result.getTitle()).isEqualTo(newTitle);
        verify(bookRepository).save(any(Book.class));
    }

    @Test
    void willThrowWhenBookToUpdateNotFoundById() {
        // given
        Long id = 1L;
        Book updatedBook = new Book();

        given(bookRepository.existsById(id)).willReturn(false);

        // when/then
        assertThatThrownBy(() -> underTest.updateBook(id, updatedBook))
                .isInstanceOf(ApiNotFoundException.class)
                .hasMessageContaining("Book not found with id: " + id);

        verify(bookRepository, never()).save(any(Book.class));
    }

    @Test
    void willThrowWhenTitleIsTakenDuringUpdate() {
        // given
        Long id = 1L;
        String newTitle = "newTitle";
        Book existingBook = new Book();
        existingBook.setTitle("oldTitle");
        Book updatedBook = new Book();
        updatedBook.setTitle(newTitle);

        given(bookRepository.existsById(id)).willReturn(true);
        given(bookRepository.existsByTitle(newTitle)).willReturn(true);

        // when/then
        assertThatThrownBy(() -> underTest.updateBook(id, updatedBook))
                .isInstanceOf(ApiBadRequestException.class)
                .hasMessageContaining("Book with title '" + newTitle + "' already exists");

        verify(bookRepository, never()).save(any(Book.class));
    }

    @Test
    void canDeleteBook() {
        // given
        Long id = 1L;
        given(bookRepository.existsById(id)).willReturn(true);

        // when
        underTest.deleteBook(id);

        // then
        verify(bookRepository).deleteById(id);
    }
}