package com.rf.librarymanagementsystem.controllers;

import com.rf.librarymanagementsystem.DTOs.BookDTO;
import com.rf.librarymanagementsystem.Services.BookService;
import com.rf.librarymanagementsystem.models.Book;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/books")
public class BookController {

    @Autowired
    private final BookService bookService;
    @Autowired
    private final ModelMapper modelMapper;

    @GetMapping
    public ResponseEntity<List<BookDTO>> getAllBooks() {
        List<Book> books = bookService.getAllBooks();
        return ResponseEntity.ok(books.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookDTO> getBookById(@PathVariable Long id) {
        var book = bookService.getBookById(id);
        return ResponseEntity.ok(convertToDTO(book));
    }

    @PostMapping
    public ResponseEntity<BookDTO> addBook(@RequestBody BookDTO bookDTO) {
        Book book = convertToEntity(bookDTO);
        Book addedBook = bookService.addBook(book);
        return ResponseEntity.status(HttpStatus.CREATED).body(convertToDTO(addedBook));
    }

    @PutMapping("/{id}")
    public ResponseEntity<BookDTO> updateBook(@PathVariable Long id, @RequestBody BookDTO bookDTO) {
        Book book = convertToEntity(bookDTO);
        return ResponseEntity.ok(convertToDTO(bookService.updateBook(id, book)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteBook(@PathVariable Long id) {
        bookService.deleteBook(id);
        return ResponseEntity.ok("Book deleted successfully");
    }

    private BookDTO convertToDTO(Book book) {
        return modelMapper.map(book, BookDTO.class);
    }

    private Book convertToEntity(BookDTO bookDTO) {
        return modelMapper.map(bookDTO, Book.class);
    }
}
