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
            var books = bookshelfClient.fetchAllBooks();
            LOGGER.info("Received {} books from server", books.size());

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

            books = bookshelfClient.fetchAllBooks();
            LOGGER.info("Received {} books from server", books.size());
        } catch (ApiException e) {
            LOGGER.error("An ApiException occurred.", e);
        }
    }

    private void initApi() {
        ApiClient apiClient = new ApiClient();
        apiClient.setBasePath("http://localhost:8080");
        api = new DefaultApi(new ApiClient());
        api.setApiClient(apiClient);
    }

    public List<Book> fetchAllBooks() throws ApiException {
        LOGGER.info("Trying to fetch books from bookshelf server");
        return api.fetchAllBooks();
    }

    public Book addBook(Book book) throws ApiException {
        LOGGER.info("Trying to add books with isbn {} to bookshelf", book.getIsbn());
        return api.addBook(book);
    }
}
