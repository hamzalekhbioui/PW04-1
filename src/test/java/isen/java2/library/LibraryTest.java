package isen.java2.library;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.failBecauseExceptionWasNotThrown;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;

import org.assertj.core.api.Condition;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import isen.java2.library.exceptions.ItemAlreadyBorrowedException;
import isen.java2.library.exceptions.ItemAlreadyReturnedException;
import isen.java2.library.exceptions.ItemNotFoundException;
import isen.java2.library.model.Book;
import isen.java2.library.model.CulturalItem;
import isen.java2.library.model.Genre;
import isen.java2.library.model.Movie;

public class LibraryTest {

	Library myLibrary;

	@BeforeEach
	public void createCulturalItems() throws Exception {
		// Create some Books (using Arrays.asList and Collections.singleton convenience
		// methods to keep code compact)
		Book warOfWorlds = new Book("War of Worlds",
				new HashSet<Genre>(Arrays.asList(Genre.SCIENCE_FICTION, Genre.THRILLER)), "Aldrous Huxley");
		Book shining = new Book("Shining", new HashSet<Genre>(Collections.singleton(Genre.HORROR)), "Stephen King");

		// Create some Movies
		Movie fightClub = new Movie("Fight Club",
				new HashSet<>(Collections.singleton(Genre.THRILLER)),
				"David Fincher", new HashSet<>(Arrays.asList("Brad Pitt", "Edward Norton", "Edward Norton")));
		Movie evilDead = new Movie("Evil Dead", new HashSet<>(Collections.singleton(Genre.HORROR)),
				"Sam Raimi", new HashSet<>(Arrays.asList("Bruce Campbell", "Ellen Sandweiss")));
		Movie eventHorizon = new Movie("Event Horizon",
				new HashSet<>(Arrays.asList(Genre.HORROR, Genre.SCIENCE_FICTION)),
				"Paul W. S. Anderson", new HashSet<>(Arrays.asList("Laurence Fishburne", "Sam Neill ")));

		// Create a library, and have fun with it
		List<Movie> listOfMovies = new LinkedList<>(Arrays.asList(evilDead, fightClub, eventHorizon));
		List<Book> listOfBook = new LinkedList<>(Arrays.asList(warOfWorlds, shining));
		this.myLibrary = new Library(listOfMovies, listOfBook);
	}

	@Test
	public void shouldPrintCatalogue() throws Exception {
		// GIVEN (here we need some specific setup, to check that the objects are indeed
		// in the collection
		// We could simplify that and rely on locally created books to test if
		// CulturalItem implemented equals and hashCode)
		// You can try to simplify this by implementing equals and hashCOde on
		// CulturalItem, and the compare books with new created objects
		Book warOfWorlds = new Book("War of Worlds",
				new HashSet<Genre>(Arrays.asList(Genre.SCIENCE_FICTION, Genre.THRILLER)), "Aldrous Huxley");
		Movie fightClub = new Movie("Fight Club",
				new HashSet<>(Collections.singleton(Genre.THRILLER)),
				"David Fincher", new HashSet<>(Arrays.asList("Brad Pitt", "Edward Norton", "Edward Norton")));

		List<Movie> listOfMovies = new LinkedList<>(Arrays.asList(fightClub));
		List<Book> listOfBook = new LinkedList<>(Arrays.asList(warOfWorlds));
		// WHEN
		Library localLibrary = new Library(listOfMovies, listOfBook);
		// THEN
		assertThat(localLibrary.getCatalogue()).hasSize(2);
		assertThat(localLibrary.getCatalogue()).containsExactly(fightClub, warOfWorlds);
	}

	@Test
	public void shouldAddMovieToLibrary() throws Exception {
		// GIVEN
		Movie theGoodTheBadTheUgly = new Movie("The good, the bad and the ugly", Collections.singleton(Genre.WESTERN),
				"Sergio Leone",
				new HashSet<>(Arrays.asList("Clint Eastwood", "Lee Van Cleef")));
		// WHEN
		this.myLibrary.add(theGoodTheBadTheUgly);
		// THEN
		assertThat(myLibrary.getCatalogue()).contains(theGoodTheBadTheUgly);
	}

	@Test
	public void shouldAddBookToLibrary() throws Exception {
		// GIVEN
		Book it = new Book("It", Collections.singleton(Genre.HORROR), "Stephen King");
		// WHEN
		this.myLibrary.add(it);
		// THEN
		assertThat(myLibrary.getCatalogue()).contains(it);
	}

	@Test
	public void shouldGetByGenre() throws Exception {
		// GIVEN
		Book it = new Book("It", Collections.singleton(Genre.HORROR), "Stephen King");
		// WHEN
		this.myLibrary.add(it);
		// THEN (note that there are multiple conditions. Still we test only one
		// behavior)
		assertThat(myLibrary.getByGenre(Genre.SCIENCE_FICTION)).hasSize(2);
		assertThat(myLibrary.getByGenre(Genre.SCIENCE_FICTION)).doesNotContain(it);
		assertThat(myLibrary.getByGenre(Genre.HORROR)).hasSize(4);
		assertThat(myLibrary.getByGenre(Genre.HORROR)).contains(it);

	}

	@Test
	public void shouldBorrowMovie() throws Exception {
		// GIVEN
		// WHEN
		Movie myFightClub = myLibrary.borrowMovie("Fight Club", "Philippe");
		// THEN
		assertThat(myFightClub.isBorrowed()).isTrue();
		assertThat(myFightClub.getBorrower()).isEqualTo("Philippe");
	}

	@Test
	public void shouldBorrowBook() throws Exception {
		// GIVEN
		// WHEN
		Book myShining = myLibrary.borrowBook("Shining", "Philippe");
		// THEN
		assertThat(myShining.isBorrowed()).isTrue();
		assertThat(myShining.getBorrower()).isEqualTo("Philippe");
	}

	@Test
	public void shouldNotAllowToBorrowUnknown() throws Exception {
		// GIVEN
		// WHEN
		// FIXME replace this try block using a lambda expression
		try {
			@SuppressWarnings("unused")
			Book unknownBook = myLibrary.borrowBook("Les fleurs du mal", "Philippe");
			failBecauseExceptionWasNotThrown(ItemNotFoundException.class);
		} catch (Exception e) {
			// THEN
			assertThat(e).isInstanceOf(ItemNotFoundException.class);
		}
	}

	@Test
	public void shouldNotAllowAlreadyBorrowed() throws Exception {
		// GIVEN
		@SuppressWarnings("unused")
		Book oneShining = myLibrary.borrowBook("Shining", "Philippe");
		// WHEN
		// Library
		// FIXME replace this try block using a lambda expression
		try {
			@SuppressWarnings("unused")
			Book anotherShining = myLibrary.borrowBook("Shining", "Martin");
			failBecauseExceptionWasNotThrown(ItemAlreadyBorrowedException.class);
		} catch (Exception e) {
			// THEN
			assertThat(e).isInstanceOf(ItemAlreadyBorrowedException.class);
		}
	}

	@Test
	public void shouldGetBorrowedItems() throws Exception {
		// GIVEN
		// WHEN
		Book myShining = myLibrary.borrowBook("Shining", "Philippe");
		// THEN
		assertThat(myLibrary.getByBorrower("Philippe")).contains(myShining);
		assertThat(myLibrary.getByBorrower("Anonymous")).isEmpty();
	}

	@Test
	public void shouldAddMovieRating() throws Exception {
		// GIVEN
		// WHEN
		myLibrary.rateMovie("Evil Dead", "Karl", 5);
		// THEN (a trick for when we don't have the expected "get" method...)
		// A quick stop here, shall we ?
		// First of all, we will see how to compact this code next week, using lambda
		// expressions
		// Second, you may end up writing a getter somewhere, just to get an easier access to the ratings.
		// This is what we mean by "use the test to define how your code will be used"
		// When using TDD, such an intricate code should never happen, because you will
		// implement a simpler interface from the ground up
		assertThat(myLibrary.getCatalogue())
				.filteredOn(new Condition<CulturalItem>(new Predicate<CulturalItem>() {
					@Override
					public boolean test(CulturalItem t) {
						return "Evil Dead".equals(t.getTitle());
					}
				}, "My useless Description"))
				.extracting(new Function<CulturalItem, Map<String, Integer>>() {
					@Override
					public Map<String, Integer> apply(CulturalItem input) {
						return input.getRatings();
					}
				}).contains(Map.of("Karl", 5));
	}

	@Test
	public void shouldListTopTen() throws Exception {
		// GIVEN
		Library localLibrary = new Library(new LinkedList<>(), new LinkedList<>());

		Book lotr = new Book("The Lord of the rings", Collections.singleton(Genre.FANTASY), "JRR Tolkien");
		localLibrary.add(lotr);
		Book got = new Book("A game of thrones", Collections.singleton(Genre.FANTASY), "Georges RR Martin");
		localLibrary.add(got);
		Book alice = new Book("Alice's adventures in wonderland", Collections.singleton(Genre.FANTASY),
				"Lewis Carroll");
		localLibrary.add(alice);

		localLibrary.rateBook("A game of thrones", "James", 5);
		localLibrary.rateBook("A game of thrones", "Roy", 4);
		localLibrary.rateBook("The Lord of the rings", "John", 5);
		localLibrary.rateBook("Alice's adventures in wonderland", "Queen of Hearts", 1);
		// WHEN
		// THEN
		assertThat(localLibrary.getTopTenItems())
				.hasSize(3)
				.containsExactly(lotr, got, alice);
	}

	@Test
	public void shouldReturnBooksBack() throws Exception {
		// Up to you to implement the method to return a CulturalItem, starting by
		// writing the test.
		// GIVEN whe have a borrowed book
		Book oneShining = myLibrary.borrowBook("Shining", "Philippe");
		// WHEN we return it back (in TDD this is when you should begin to think about
		// the method signature)
		myLibrary.returnBook(oneShining);
		// THEN (and yet again, how do we check that a book has been returned back ?)
		// One way to do it is to check that we can borrow it again, but it is dodgy
		// another
		assertThat(myLibrary.getCatalogue()).contains(oneShining);
		assertThat(oneShining.isBorrowed()).isFalse();
		assertThat(oneShining.getBorrower()).isEmpty();

	}

	@Test
	public void shouldReturnMoviesBack() throws Exception {
		// Up to you to implement the method to return a CulturalItem, starting by
		// writing the test.
		// GIVEN whe have a borrowed movie
		Movie oneFightClub = myLibrary.borrowMovie("Fight Club", "Philippe");
		// WHEN we return it back (in TDD this is when you should begin to think about
		// the method signature)
		myLibrary.returnMovie(oneFightClub);
		// THEN (and yet again, how do we check that a book has been returned back ?)
		// One way to do it is to check that we can borrow it again, but it is dodgy
		// another
		assertThat(myLibrary.getCatalogue()).contains(oneFightClub);
		assertThat(oneFightClub.isBorrowed()).isFalse();
		assertThat(oneFightClub.getBorrower()).isEmpty();

	}

	/**
	 * The following two tests are added because soon enough You'll realize that you
	 * need some more checks on your code,
	 * if you want to make sure that returning a book holds some business value
	 */

	@Test
	public void shouldNotReturnUnknownBooks() throws Exception {
		// GIVEN
		Book unknownBook = new Book("Dune", Collections.singleton(Genre.SCIENCE_FICTION), "Franck Herbert");
		// FIXME replace this try block using a lambda expression
		try {
			// WHEN
			myLibrary.returnBook(unknownBook);
			failBecauseExceptionWasNotThrown(ItemNotFoundException.class);
		} catch (Exception e) {
			// THEN
			assertThat(e).isInstanceOf(ItemNotFoundException.class);
		}
	}

	@Test
	public void shouldNotReturnAlreadyReturnedBooks() throws Exception {
		// GIVEN
		Book oneShining = myLibrary.borrowBook("Shining", "Philippe");
		myLibrary.returnBook(oneShining);
		// WHEN
		// FIXME replace this try block using a lambda expression
		try {
			myLibrary.returnBook(oneShining);
			failBecauseExceptionWasNotThrown(ItemAlreadyReturnedException.class);
			
		} catch (Exception e) {
			// THEN
			assertThat(e).isInstanceOf(ItemAlreadyReturnedException.class);
		}
	}

}
