package de.birgitkratz.bookshelfserver;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.birgitkratz.bookshelf.api.BooksApi;
import de.birgitkratz.bookshelf.model.Book;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BookshelfController implements BooksApi {

    private static final Logger LOGGER = LoggerFactory.getLogger(BookshelfController.class);

    Map<String, Book> bookshelf = new HashMap<>();

    @Override
    public ResponseEntity<Book> addBook(final Book book) {
        LOGGER.info("Received request to add book with isbn {} to bookshelf", book.getIsbn());
        bookshelf.put(book.getIsbn(), book);
        return ResponseEntity
                .created(URI.create("/books/" + book.getIsbn()))
                .body(book);
    }

    @Override
    public ResponseEntity<List<Book>> fetchAllBooks() {
        LOGGER.info("Received request to fetch all books");
        final var books = new ArrayList<>(bookshelf.values());
        return ResponseEntity.ok(books);
    }
}
