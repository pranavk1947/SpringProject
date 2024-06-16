package libraryManagementSystem;
import java.util.List;

public class LibManagerDemo {
	public static void main(String[] args) {
        LibraryManager libraryManager = LibraryManager.getInstance();

        // Add books to the catalog
        libraryManager.addBook(new Book("ISBN1", "Book 1", "Author 1"));
        libraryManager.addBook(new Book("ISBN2", "Book 2", "Author 2"));
        libraryManager.addBook(new Book("ISBN3", "Book 3", "Author 3"));

        // Register members
        libraryManager.registerMember(new Member("M1", "John Doe", "john@example.com"));
        libraryManager.registerMember(new Member("M2", "Jane Smith", "jane@example.com"));

        // Borrow books
        libraryManager.borrowBook("M1", "ISBN1");
        libraryManager.borrowBook("M2", "ISBN2");

        // Return books
        libraryManager.returnBook("M1", "ISBN1");

        // Search books
        List<Book> searchResults = libraryManager.searchBooks("Book");
        System.out.println("Search Results:"+ searchResults.toString());
        for (Book book : searchResults) {
            System.out.println(book.getTitle() + " by " + book.getAuthor());
        }
    }
}
