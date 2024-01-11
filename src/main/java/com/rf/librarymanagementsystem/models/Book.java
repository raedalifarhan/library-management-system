package com.rf.librarymanagementsystem.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "books")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String title;

    @Column(nullable = false, length = 50)
    private String author;

    @Column(nullable = false)
    private int publicationYear;

    @Column(nullable = false)
    private String ISBN;

    private Boolean isAvailable = true;

    // todo: CreateDate, CreatedBy, LastUpdateDate, UpdatedBy
}
