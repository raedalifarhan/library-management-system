package com.rf.librarymanagementsystem.services;

import com.rf.librarymanagementsystem.exceptions.ApiNotFoundException;
import com.rf.librarymanagementsystem.models.Book;
import com.rf.librarymanagementsystem.models.BorrowingRecord;
import com.rf.librarymanagementsystem.models.Patron;
import com.rf.librarymanagementsystem.repositories.BookRepository;
import com.rf.librarymanagementsystem.repositories.BorrowingRecordRepository;
import com.rf.librarymanagementsystem.repositories.PatronRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class BorrowingRecordServiceTest {

    @Mock
    private BorrowingRecordRepository borrowingRecordRepository;

    @Mock
    private BookRepository bookRepository;

    @Mock
    private PatronRepository patronRepository;

    @InjectMocks
    private BorrowingRecordService underTest;

    private Book book;
    private Patron patron;

    @BeforeEach
    void setUp() {
        underTest = new BorrowingRecordService(borrowingRecordRepository, bookRepository, patronRepository);
        book = Book.builder().id(1L).title("Book Title").isAvailable(true).build();
        patron = Patron.builder().id(1L).name("Patron Name").build();
    }

    @Test
    void borrowBookWhenBookAndPatronExistAndBookIsAvailable() {
        // given
        Long bookId = 1L;
        Long patronId = 1L;
        given(bookRepository.existsById(bookId)).willReturn(true);
        given(patronRepository.existsById(patronId)).willReturn(true);
        given(bookRepository.findById(bookId)).willReturn(Optional.of(book));
        given(patronRepository.findById(patronId)).willReturn(Optional.of(patron));

        // when
        Boolean result = underTest.borrowBook(bookId, patronId);

        // then
        assertThat(result).isTrue();
        assertThat(book.getIsAvailable()).isFalse();
        verify(bookRepository).save(book);
        verify(borrowingRecordRepository).save(any(BorrowingRecord.class));
    }

    @Test
    void borrowBookWhenBookNotFound() {
        // given
        Long bookId = 1L;
        Long patronId = 1L;
        given(bookRepository.existsById(bookId)).willReturn(false);

        // when/then
        assertThatThrownBy(() -> underTest.borrowBook(bookId, patronId))
                .isInstanceOf(ApiNotFoundException.class)
                .hasMessageContaining("Book not found with id: " + bookId);
    }

    @Test
    void borrowBookWhenPatronNotFound() {
        // given
        Long bookId = 1L;
        Long patronId = 1L;
        given(bookRepository.existsById(bookId)).willReturn(true);
        given(patronRepository.existsById(patronId)).willReturn(false);

        // when/then
        assertThatThrownBy(() -> underTest.borrowBook(bookId, patronId))
                .isInstanceOf(ApiNotFoundException.class)
                .hasMessageContaining("Patron not found with id: " + patronId);
    }

    @Test
    void borrowBookWhenBookIsNotAvailable() {
        // given
        Long bookId = 1L;
        Long patronId = 1L;
        Book book = Book.builder().id(bookId).title("Book").isAvailable(false).build();
        Patron patron = Patron.builder().id(patronId).name("Patron").build();

        given(bookRepository.existsById(bookId)).willReturn(true);
        given(patronRepository.existsById(patronId)).willReturn(true);
        given(bookRepository.findById(bookId)).willReturn(Optional.of(book));
        given(patronRepository.findById(patronId)).willReturn(Optional.of(patron));

        // when
        Boolean result = underTest.borrowBook(bookId, patronId);

        // then
        assertThat(result).isFalse();
        verify(bookRepository, never()).save(any(Book.class));
        verify(borrowingRecordRepository, never()).save(any(BorrowingRecord.class));
    }


    @Test
    void returnBookWhenBookAndPatronExistAndBookIsNotAvailable() {
        // given
        Long bookId = 1L;
        Long patronId = 1L;
        book.setIsAvailable(false);
        BorrowingRecord borrowingRecord = BorrowingRecord.builder()
                .book(book)
                .patron(patron)
                .borrowingDate(LocalDateTime.now())
                .build();

        given(bookRepository.existsById(bookId)).willReturn(true);
        given(patronRepository.existsById(patronId)).willReturn(true);
        given(bookRepository.findById(bookId)).willReturn(Optional.of(book));
        given(patronRepository.findById(patronId)).willReturn(Optional.of(patron));
        given(borrowingRecordRepository.findByBookAndPatronAndReturnDateIsNull(book, patron)).willReturn(borrowingRecord);

        // when
        Boolean result = underTest.returnBook(bookId, patronId);

        // then
        assertThat(result).isTrue();
        assertThat(book.getIsAvailable()).isTrue();
        verify(bookRepository).save(book);
        verify(borrowingRecordRepository).save(borrowingRecord);
    }

    @Test
    void returnBookWhenBookNotFound() {
        // given
        Long bookId = 1L;
        Long patronId = 1L;
        given(bookRepository.existsById(bookId)).willReturn(false);

        // when/then
        assertThatThrownBy(() -> underTest.returnBook(bookId, patronId))
                .isInstanceOf(ApiNotFoundException.class)
                .hasMessageContaining("Book not found with id: " + bookId);
    }

    @Test
    void returnBookWhenPatronNotFound() {
        // given
        Long bookId = 1L;
        Long patronId = 1L;
        given(bookRepository.existsById(bookId)).willReturn(true);
        given(patronRepository.existsById(patronId)).willReturn(false);

        // when/then
        assertThatThrownBy(() -> underTest.returnBook(bookId, patronId))
                .isInstanceOf(ApiNotFoundException.class)
                .hasMessageContaining("Patron not found with id: " + patronId);
    }

    @Test
    void returnBookWhenBookIsAvailable() {
        // given
        Long bookId = 1L;
        Long patronId = 1L;
        book.setIsAvailable(true);

        given(bookRepository.existsById(bookId)).willReturn(true);
        given(patronRepository.existsById(patronId)).willReturn(true);
        given(bookRepository.findById(bookId)).willReturn(Optional.of(book));
        given(patronRepository.findById(patronId)).willReturn(Optional.of(Patron.builder().build())); // Create a Patron object

        // when
        Boolean result = underTest.returnBook(bookId, patronId);

        // then
        assertThat(result).isFalse();
        verify(bookRepository, never()).save(any(Book.class));
        verify(borrowingRecordRepository, never()).save(any(BorrowingRecord.class));
    }


    @Test
    void returnBookWhenBorrowingRecordNotFound() {
        // given
        Long bookId = 1L;
        Long patronId = 1L;
        book.setIsAvailable(false);
        given(bookRepository.existsById(bookId)).willReturn(true);
        given(patronRepository.existsById(patronId)).willReturn(true);
        given(bookRepository.findById(bookId)).willReturn(Optional.of(book));
        given(patronRepository.findById(patronId)).willReturn(Optional.of(patron));
        given(borrowingRecordRepository.findByBookAndPatronAndReturnDateIsNull(book, patron)).willReturn(null);

        // when
        Boolean result = underTest.returnBook(bookId, patronId);

        // then
        assertThat(result).isFalse();
        verify(bookRepository, never()).save(any(Book.class));
        verify(borrowingRecordRepository, never()).save(any(BorrowingRecord.class));
    }
}
