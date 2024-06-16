package libraryManagementSystem;

import java.util.ArrayList;
import java.util.List;

public class Member {
	private String id;
	private String name;
	private List<Book> borrowedBooks;
	private String contactinfo;
	public Member(String id, String name, String contactinfo) {
		super();
		this.id = id;
		this.name = name;
		this.borrowedBooks = new ArrayList<>();
		this.contactinfo = contactinfo;
	}
	public String getId() {
		return id;
	}
	
	public String getName() {
		return name;
	}
	
	public List<Book> getBorrowedBooks() {
		return borrowedBooks;
	}
	
	public String getContactinfo() {
		return contactinfo;
	}
	
	public void borrowBook(Book book) {
		borrowedBooks.add(book);
	}
	public void returnBook(Book book) {
		borrowedBooks.remove(book);
	}
	
	
	
	
}
