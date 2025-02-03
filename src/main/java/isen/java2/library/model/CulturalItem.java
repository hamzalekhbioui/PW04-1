/**
 * 
 */
package isen.java2.library.model;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author Philippe Duval
 *
 *         Abstract class representing a Cultural Item, implementing Comparable
 *         in order to be able to sort lists of CulturalItems
 */
public abstract class CulturalItem implements Comparable<CulturalItem> {

	protected String title;
	protected Set<Genre> genres;
	protected boolean borrowed;
	protected String borrower = ""; // we initialize the value of borrower to
									// empty in order to be consistent in the
									// getBorrower method, to avoid returning
									// either empty if the item is available
									// and has been already borrowed once, or
									// null if it has never been borrowed
	protected Map<String, Integer> ratings;

	/**
	 * Default constructor. Just in case.
	 */
	public CulturalItem() {
		// Here we instantiate our map with a concrete implementation
		this.ratings = new HashMap<>();
	}

	/**
	 * Parameterized constructor
	 * 
	 * @param title
	 * @param genres
	 */
	public CulturalItem(String title, Set<Genre> genres) {
		this.title = title;
		this.genres = genres;
		this.ratings = new HashMap<>();
	}

	/**
	 * @return the ratings
	 */
	public Map<String, Integer> getRatings() {
		return ratings;
	}

	/**
	 * Allows a person to rate this item
	 * 
	 * @param person
	 * @param rating
	 */
	public void addRating(String person, Integer rating) {
		this.ratings.put(person, rating);
	}

	/**
	 * @return the average rating of this item
	 */
	public Double getAverageRating() {
		// FIXME: again, a perfect place for a lambda expression
		if (this.ratings.isEmpty()) {
			return 0.0;
		}
		int avg = 0;
		for (Integer rating : this.ratings.values()) {
			avg += rating;
		}
		return avg / (double) this.ratings.values().size();
	}

	/**
	 * @param borrower
	 */
	public void borrow(String borrower) {
		this.borrower = borrower;
		this.borrowed = true;
	}

	public void returnBack() {
		this.borrower = "";
		this.borrowed = false;
	}

	/**
	 * Prints the description of the Cultural Item
	 */
	public abstract void print();

	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param title
	 *            the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @return the genres
	 */
	public Set<Genre> getGenres() {
		return genres;
	}

	/**
	 * @param genres
	 *            the genres to set
	 */
	public void setGenres(Set<Genre> genres) {
		this.genres = genres;
	}

	/**
	 * @return wether the Cultural Item is borrowed or not
	 */
	public boolean isBorrowed() {
		return this.borrowed;
	}

	/**
	 * @return the borrower, an empty string if the cultural item is not
	 *         borrowed
	 */
	public String getBorrower() {
		return this.borrower;
	}

	@Override
	public int compareTo(CulturalItem o) {
		// this is where the sorting by Title happens
		// note that the default behavior of CompareTo for type String is
		// sorting by alphabetical order
		return this.title.compareTo(o.getTitle());
	}

}
