package com.rf.librarymanagementsystem.DTOs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BorrowingRecordDTO {
    private Long id;
    private Long bookId;
    private Long patronId;
    private LocalDateTime borrowDate;
    private LocalDateTime returnDate;
}