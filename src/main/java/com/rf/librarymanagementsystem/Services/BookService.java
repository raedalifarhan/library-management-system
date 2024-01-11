    package com.rf.librarymanagementsystem.Services;

    import com.rf.librarymanagementsystem.exceptions.ApiBadRequestException;
    import com.rf.librarymanagementsystem.exceptions.ApiNotFoundException;
    import com.rf.librarymanagementsystem.models.Book;
    import com.rf.librarymanagementsystem.repositories.BookRepository;
    import lombok.RequiredArgsConstructor;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.stereotype.Service;

    import java.util.List;

    @Service
    @RequiredArgsConstructor
    public class BookService {

        @Autowired
        private final BookRepository bookRepository;


        public List<Book> getAllBooks() {
            return bookRepository.findAll();
        }

        public Book getBookById(Long id) {

            if (!bookRepository.existsById(id)) throw new ApiNotFoundException("Book not found with id: " + id);

            Book book = bookRepository.findById(id).orElse(null);

            if (book == null) throw new ApiBadRequestException("Book not found");

            return book;
        }

        public Book addBook(Book book) {

            String title = book.getTitle();

            if (bookRepository.existsByTitle(title)) {
                throw new ApiBadRequestException("Book with title '" + title + "' already exists");
            }

            return bookRepository.save(book);
        }

        public Book updateBook(Long id, Book book) {

            if (!bookRepository.existsById(id)) throw new ApiNotFoundException("Book not found with id: " + id);

            String title = book.getTitle();

            if (bookRepository.existsByTitle(title)) {
                throw new ApiBadRequestException("Book with title '" + title + "' already exists");
            }

            Book bookToUpdate = bookRepository.findById(id).orElse(null);

            if (bookToUpdate == null) throw new ApiBadRequestException("Book not found");;

            // Update the existing book entity
            // todo: I can use mapper to update properties.
            bookToUpdate.setTitle(book.getTitle());
            bookToUpdate.setAuthor(book.getAuthor());
            bookToUpdate.setPublicationYear(book.getPublicationYear());
            bookToUpdate.setISBN(book.getISBN());

            return bookRepository.save(bookToUpdate);
        }

        public void deleteBook(Long id) {
            if (!bookRepository.existsById(id)) throw new ApiNotFoundException("Book not found with id: " + id);
            bookRepository.deleteById(id);
        }
    }
