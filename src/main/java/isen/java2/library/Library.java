/**
 * 
 */
package isen.java2.library;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

import isen.java2.library.exceptions.ItemAlreadyBorrowedException;
import isen.java2.library.exceptions.ItemAlreadyReturnedException;
import isen.java2.library.exceptions.ItemNotFoundException;
import isen.java2.library.model.Book;
import isen.java2.library.model.CulturalItem;
import isen.java2.library.model.Genre;
import isen.java2.library.model.Movie;

/**
 * This is our main library class, which will hold all the logic of
 * CulturalItems handling
 * 
 * @author Philippe Duval
 */
public class Library {

	// Private field for Movies handling. 
	private List<Movie> listOfMovies;

	// Private field for books handling.
	private List<Book> listOfBooks;

	/**
	 * Simple parameterized constructor
	 * 
	 * @param listOfMovies
	 *                    the list of Movies available in the library
	 * @param listOfBooks
	 *                    the list of books available in the library
	 */
	public Library(List<Movie> listOfMovies, List<Book> listOfBooks) {
		this.listOfMovies = listOfMovies;
		this.listOfBooks = listOfBooks;
	}

	/**
	 * Adds a Movie to the library
	 * 
	 * @param Movie
	 */
	public void add(Movie movie) {
		this.listOfMovies.add(movie);
	}

	/**
	 * Adds a book to the library
	 * 
	 * @param book
	 */
	public void add(Book book) {
		this.listOfBooks.add(book);
	}

	/**
	 * Allows a given user to borrow a Movie
	 * 
	 * @param MovieTitle
	 * @param borrower
	 * @return
	 * @throws ItemNotFoundException
	 *                                      if the title of the Movie does not match
	 *                                      an existing item in
	 *                                      the library
	 * @throws ItemAlreadyBorrowedException
	 *                                      if the Movie is already borrowed
	 */
	public Movie borrowMovie(String movieTitle, String borrower)
			throws ItemNotFoundException, ItemAlreadyBorrowedException {
		return borrowItem(movieTitle, borrower, this.listOfMovies);
	}

	/**
	 * 
	 * @param bookTitle
	 * @param borrower
	 * @return
	 * @throws ItemNotFoundException
	 *                                      if the title of the Movie does not match
	 *                                      an existing item in
	 *                                      the library
	 * @throws ItemAlreadyBorrowedException
	 *                                      if the Movie is already borrowed
	 */
	public Book borrowBook(String bookTitle, String borrower)
			throws ItemNotFoundException, ItemAlreadyBorrowedException {
		return borrowItem(bookTitle, borrower, this.listOfBooks);
	}

	/**
	 * As the borrowBook and borrowMovie implement practically the same logic, we
	 * factorize their code in a private method that does all the logic using
	 * generics. we will use a generic T extending CulturalItem to parameterize
	 * our class. this generic will be inferred at compilation time depending on
	 * the calling method's signature
	 * 
	 * @param itemTitle
	 * @param borrower
	 * @return
	 * @throws ItemAlreadyBorrowedException
	 * @throws ItemNotFoundException
	 */
	private <T extends CulturalItem> T borrowItem(String itemTitle, String borrower, List<T> listOfItems)
			throws ItemNotFoundException, ItemAlreadyBorrowedException {
		T item = this.searchByTitle(itemTitle, listOfItems);
		if (item.isBorrowed()) {
			throw new ItemAlreadyBorrowedException();
		} else {
			item.borrow(borrower);
			return item;
		}
	}

	/**
	 * Method listing what was borrowed by a certain user
	 * 
	 * @param borrower
	 */
	public void printBorrowedItems(String borrower) {
		this.printSortedList(this.filterByBorrower(this.listOfBooks, borrower), "Borrowed Books by " + borrower);
		this.printSortedList(this.filterByBorrower(this.listOfMovies, borrower), "Borrowed Movies by " + borrower);
	}

	/**
	 * Method allowing to rate a Movie
	 * 
	 * @param MovieTitle
	 * @param person
	 * @param rating
	 * @throws ItemNotFoundException
	 */
	public void rateMovie(String movieTitle, String person, Integer rating) throws ItemNotFoundException {
		Movie movie = this.searchByTitle(movieTitle, this.listOfMovies);
		movie.addRating(person, rating);
	}

	/**
	 * Method allowing to rate a book
	 * 
	 * @param bookTitle
	 * @param person
	 * @param rating
	 * @throws ItemNotFoundException
	 */
	public void rateBook(String bookTitle, String person, Integer rating) throws ItemNotFoundException {
		Book book = this.searchByTitle(bookTitle, this.listOfBooks);
		book.addRating(person, rating);
	}

	/**
	 * Method returning the top ten items according to all ratings in the
	 * library
	 */
	public void printTopTenItems() {
		List<CulturalItem> top = getTopTenItems();
		// print list (TRAP: you cannot use the print method, as it already
		// makes sorting for you)
		// here we use a ternary operator to extract the sublist, in order to
		// avoid a indexOutOfBoundException
		this.printList(top.size() > 10 ? top.subList(0, 10) : top, "Our Best Of");
	}

	/**
	 * Method returning the top ten items
	 * Beware that it can return less than ten articles!
	 * 
	 * @return a @List of culturalItems
	 */
	public List<CulturalItem> getTopTenItems() {
		// FIXME replace this by a lambda expression
		List<CulturalItem> top = new LinkedList<>(this.listOfBooks);
		top.addAll(this.listOfMovies);
		Collections.sort(top, new Comparator<CulturalItem>() {

			@Override
			public int compare(CulturalItem o1, CulturalItem o2) {
				return o2.getAverageRating().compareTo(o1.getAverageRating());
			}

		});
		return top.size() > 10 ? top.subList(0, 10): top;
	}

	/**
	 * Here again, we use a private method using generics to factorize our code.
	 * Note that in the case we want to handle another type of CulturalItem, all
	 * these private generic methods stay unchanged
	 * 
	 * @param title
	 * @return
	 */
	private <T extends CulturalItem> T searchByTitle(String title, List<T> items) throws ItemNotFoundException {
		// FIXME replace this by a lambda expression
		for (T item : items) {
			if (item.getTitle().equals(title)) {
				return item;
			}
		}
		throw new ItemNotFoundException();
	}

	/**
	 * Method printing the two lists of our library, sorted by alphabetical
	 * order
	 */
	public void printCatalogue() {
		this.printSortedList(this.listOfBooks, "Our Books");
		this.printSortedList(this.listOfMovies, "Our Movies");
	}

	/**
	 * Method returning a list of our library
	 */
	public List<CulturalItem> getCatalogue() {
		List<CulturalItem> items = new LinkedList<>();
		items.addAll(listOfBooks);
		items.addAll(listOfMovies);
		Collections.sort(items);
		return items;
	}

	/**
	 * Method searching items by genre and printing the result in alphabetical
	 * order
	 * 
	 * @param genre
	 */
	public void printByGenre(Genre genre) {
		this.printSortedList(this.filterByGenre(genre, this.listOfBooks), "Books of " + genre);
		this.printSortedList(this.filterByGenre(genre, this.listOfMovies), "Movies of " + genre);

	}

	/**
	 * Method searching items by genre and printing the result in alphabetical
	 * order
	 * 
	 * @param genre
	 */
	public List<CulturalItem> getByGenre(Genre genre) {
		List<CulturalItem> items = new LinkedList<>();
		items.addAll(this.filterByGenre(genre, this.listOfBooks));
		items.addAll(this.filterByGenre(genre, this.listOfMovies));
		return items;
	}


	/**
	 * And yet another private method using generics, this time to filter our
	 * collections by genre
	 * 
	 * @param genre
	 * @param items
	 * @return
	 */
	private <T extends CulturalItem> List<T> filterByGenre(Genre genre, List<T> items) {

		// FIXME replace this by a lambda expression
	
		List<T> filteredList = new LinkedList<>(items);

		filteredList.removeIf(t -> !t.getGenres().contains(genre));
		return filteredList;
	}

	/**
	 * Method searching items by genre and printing the result in alphabetical
	 * order
	 * 
	 * @param genre
	 */
	public List<CulturalItem> getByBorrower(String borrower) {
		List<CulturalItem> items = new LinkedList<>();
		items.addAll(this.filterByBorrower(this.listOfBooks, borrower));
		items.addAll(this.filterByBorrower(this.listOfMovies, borrower));
		return items;
	}

	/**
	 * Method allowing us to filter by borrower a list of items. Again, we use
	 * generics.
	 * 
	 * @param items
	 * @param borrower
	 * @param genre
	 * @return
	 */
	private <T extends CulturalItem> List<T> filterByBorrower(List<T> items, String borrower) {
		// FIXME replace this by a lambda expression
		List<T> filteredList = new LinkedList<>(items);

		filteredList.removeIf(new Predicate<CulturalItem>() {

			@Override
			public boolean test(CulturalItem t) {
				return (!t.isBorrowed()) || (t.isBorrowed() && !t.getBorrower().equals(borrower));
			}
		});
		return filteredList;
	}

	/**
	 * You bet it ! private method using generics, to print a list of items
	 * 
	 * @param items
	 * @param type
	 */
	private <T extends CulturalItem> void printSortedList(List<T> items, String type) {
		// FIXME there is something to change here if you want your application to work ;)
		Collections.sort(items);
		this.printList(items, type);
	}

	/**
	 * @param items
	 */
	private <T extends CulturalItem> void printList(List<T> items, String type) {
		System.out.println("======= " + type + " =============");
		// FIXME replace this by a lambda expression
		items.forEach(new Consumer<T>() {

			@Override
			public void accept(T it) {
				it.print();

			}
		});
	}


/**
 * Brand new methods to return an item to the library
 */

	/**
	 * Returns a book to the library.
	 *
	 * @param item the book to be returned
	 * @throws ItemNotFoundException if the book is not found in the library
	 * @throws ItemAlreadyReturnedException if the book has already been returned
	 */
    public void returnBook(Book item) throws ItemNotFoundException, ItemAlreadyReturnedException {
        this.returnItem(item, this.listOfBooks);
    }

	/**
	 * Returns a movie to the library.
	 * 
	 * @param item The movie to be returned.
	 * @throws ItemNotFoundException If the movie is not found in the library.
	 * @throws ItemAlreadyReturnedException If the movie has already been returned.
	 */
    public void returnMovie(Movie item) throws ItemNotFoundException, ItemAlreadyReturnedException {
        this.returnItem(item,this.listOfMovies);
    }

	/**
	 * Returns a borrowed cultural item to the library.
	 * 
	 * @param item         The cultural item to be returned.
	 * @param listOfItems  The list of cultural items to search from.
	 * @throws ItemNotFoundException      If the item is not found in the list.
	 * @throws ItemAlreadyReturnedException  If the item is already returned.
	 */
	private <T extends CulturalItem> void returnItem(CulturalItem item, List<T> listOfItems) throws ItemNotFoundException, ItemAlreadyReturnedException {
		T localitem = this.searchByTitle(item.getTitle(), listOfItems);
		if (!localitem.isBorrowed()) {
			throw new ItemAlreadyReturnedException();
		}
		item.returnBack();
	}
}
