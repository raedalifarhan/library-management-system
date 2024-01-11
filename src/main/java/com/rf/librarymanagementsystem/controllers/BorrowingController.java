package com.rf.librarymanagementsystem.controllers;

import com.rf.librarymanagementsystem.Services.BorrowingRecordService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/borrow")
public class BorrowingController {

    private final BorrowingRecordService borrowingRecordService;

    @PostMapping("/borrow/{bookId}/patron/{patronId}")
    public ResponseEntity<String> borrowBook(@PathVariable Long bookId, @PathVariable Long patronId) {

        Boolean borrowed = borrowingRecordService.borrowBook(bookId, patronId);
        if (borrowed) {
            return ResponseEntity.ok("Book borrowed successfully");
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/return/{bookId}/patron/{patronId}")
    public ResponseEntity<String> returnBook(@PathVariable Long bookId, @PathVariable Long patronId) {

        Boolean returned = borrowingRecordService.returnBook(bookId, patronId);

        if (returned) {
            return ResponseEntity.ok("Book returned successfully");
        }
        return ResponseEntity.notFound().build();
    }
}
