/**
 * 
 */
package isen.java2.library.model;

import java.util.Set;

/**
 * @author Philippe Duval
 *
 *         Implementation of Book
 */
public class Book extends CulturalItem {

	private String author;

	/**
	 * Default constructor, nothing fancy here
	 */
	public Book() {
		super();
	}

	/**
	 * Parameterized constructor. we use the super constructor to initialize the
	 * fields from the parent class.
	 * 
	 * @param title
	 * @param genres
	 */
	public Book(String title, Set<Genre> genres, String author) {
		super(title, genres);
		this.author = author;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see isen.java2.library.model.CulturalItem#print()
	 */
	@Override
	public void print() {
		System.out.println("«" + this.title + "» written by " + this.author + ", score :" + this.getAverageRating()
				+ ", " + (borrowed ? "(B)" : "(A)") + ".");
	}

	/**
	 * @return the author
	 */
	public String getAuthor() {
		return author;
	}

	/**
	 * @param author
	 *            the author to set
	 */
	public void setAuthor(String author) {
		this.author = author;
	}

}
