package com.rf.librarymanagementsystem.repositories;

import com.rf.librarymanagementsystem.models.Book;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    boolean existsByTitle(String title);
    // todo: isbn
}
