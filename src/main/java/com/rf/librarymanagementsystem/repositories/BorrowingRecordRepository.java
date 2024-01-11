package com.rf.librarymanagementsystem.repositories;

import com.rf.librarymanagementsystem.models.Book;
import com.rf.librarymanagementsystem.models.BorrowingRecord;
import com.rf.librarymanagementsystem.models.Patron;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BorrowingRecordRepository extends JpaRepository<BorrowingRecord, Long> {
    List<BorrowingRecord> findByPatronId(Long patronId);
    BorrowingRecord findByBookAndPatronAndReturnDateIsNull(Book book, Patron patron);
}
