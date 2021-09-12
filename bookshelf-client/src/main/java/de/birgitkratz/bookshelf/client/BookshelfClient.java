package de.birgitkratz.bookshelf.client;

import java.util.List;

import de.birgitkratz.bookshelf.ApiClient;
import de.birgitkratz.bookshelf.ApiException;
import de.birgitkratz.bookshelf.api.DefaultApi;
import de.birgitkratz.bookshelf.model.Book;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BookshelfClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(BookshelfClient.class);

    private DefaultApi api;

    public static void main(String[] args) {
        final var bookshelfClient = new BookshelfClient();

        bookshelfClient.initApi();

        try {
            bookshelfClient.fetchAllBooks();

            final var httpUndRest = new Book()
                    .isbn("9783898647328")
                    .title("HTTP und Rest")
                    .authors(List.of("Stefan Tilkov"));

            final var refactoring = new Book()
                    .isbn("9780134757599")
                    .title("Refactoring")
                    .authors(List.of("martin Fowler", "Kent Beck"));

            bookshelfClient.addBook(httpUndRest);
            bookshelfClient.addBook(refactoring);

            bookshelfClient.fetchAllBooks();

            bookshelfClient.fetchByIsbn("1234567890123");
            bookshelfClient.fetchByIsbn("9780134757599");

        } catch (ApiException e) {
            LOGGER.error("An ApiException occurred.", e);
        }
    }

    private void initApi() {
        ApiClient apiClient = new ApiClient();
        apiClient.setBasePath("http://localhost:8080");
        api = new DefaultApi(apiClient);
    }

    List<Book> fetchAllBooks() throws ApiException {
        LOGGER.info("Trying to fetch books from bookshelf server");
        final var books = api.fetchAllBooks();
        LOGGER.info("Received {} books from server", books != null ? books.size() : 0);
        return books;
    }

    Book addBook(Book book) throws ApiException {
        LOGGER.info("Trying to add books with isbn {} to bookshelf", book.getIsbn());
        return api.addBook(book);
    }

    Book fetchByIsbn(String isbn) throws ApiException {
        LOGGER.info("Trying to fetch book for isbn {} from bookshelf server", isbn);
        final var bookApiResponse = api.fetchByIsbnWithHttpInfo(isbn);
        if (bookApiResponse.getStatusCode() == 200) {
            var bookByIsbn = bookApiResponse.getData();
            LOGGER.info("Received book with title '{}' from bookshelf server", bookByIsbn.getTitle());
            return bookByIsbn;
        }
        if (bookApiResponse.getStatusCode() == 204) {
            LOGGER.info("No book for isbn {} in booshelf", isbn);
        }
        return null;
    }
}
