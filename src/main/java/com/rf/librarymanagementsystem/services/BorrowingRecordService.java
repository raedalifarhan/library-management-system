package com.rf.librarymanagementsystem.services;

import com.rf.librarymanagementsystem.exceptions.ApiBadRequestException;
import com.rf.librarymanagementsystem.exceptions.ApiNotFoundException;
import com.rf.librarymanagementsystem.models.Book;
import com.rf.librarymanagementsystem.models.BorrowingRecord;
import com.rf.librarymanagementsystem.models.Patron;
import com.rf.librarymanagementsystem.repositories.BookRepository;
import com.rf.librarymanagementsystem.repositories.BorrowingRecordRepository;
import com.rf.librarymanagementsystem.repositories.PatronRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Service
public class BorrowingRecordService {

    @Autowired
    private final BorrowingRecordRepository borrowingRecordRepository;
    @Autowired
    private final BookRepository bookRepository;
    @Autowired
    private final PatronRepository patronRepository;

    public Boolean borrowBook(Long bookId, Long patronId) {
        if (!bookRepository.existsById(bookId))
            throw new ApiNotFoundException("Book not found with id: " + bookId);

        if (!patronRepository.existsById(patronId))
            throw new ApiNotFoundException("Patron not found with id: " + patronId);

        Book book = bookRepository.findById(bookId).orElse(null);
        if (book == null) throw new ApiBadRequestException("Book not found");

        Patron patron = patronRepository.findById(patronId).orElse(null);
        if (patron == null) throw new ApiBadRequestException("Patron not found");

        if (book.getIsAvailable()) {
            book.setIsAvailable(false);
            bookRepository.save(book);

            var borrowingRecord = BorrowingRecord.builder()
                    .book(book)
                    .patron(patron)
                    .borrowingDate(LocalDateTime.now())
                    .build();

            borrowingRecordRepository.save(borrowingRecord);
            return true;
        }
        return false;
    }

    public Boolean returnBook(Long bookId, Long patronId) {
        if (!bookRepository.existsById(bookId))
            throw new ApiNotFoundException("Book not found with id: " + bookId);

        if (!patronRepository.existsById(patronId))
            throw new ApiNotFoundException("Patron not found with id: " + patronId);

        Book book = bookRepository.findById(bookId).orElseThrow(() -> new ApiBadRequestException("Book not found"));
        Patron patron = patronRepository.findById(patronId).orElseThrow(() -> new ApiBadRequestException("Patron not found"));

        if (!book.getIsAvailable()) {
            BorrowingRecord borrowingRecord = borrowingRecordRepository.findByBookAndPatronAndReturnDateIsNull(book, patron);

            if (borrowingRecord != null) {
                book.setIsAvailable(true);
                bookRepository.save(book);

                borrowingRecord.setReturnDate(LocalDateTime.now());
                borrowingRecordRepository.save(borrowingRecord);

                return true;
            }
        }
        return false;
    }

}
