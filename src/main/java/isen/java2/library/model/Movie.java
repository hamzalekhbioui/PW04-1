/**
 * 
 */
package isen.java2.library.model;

import java.util.Iterator;
import java.util.Set;

/**
 * @author Philippe Duval
 *
 *         Implementation of a movie, see books for comments
 */
public class Movie extends CulturalItem {

	private String director;

	private Set<String> actors;

	/**
	 * 
	 */
	public Movie() {
		super();
	}

	/**
	 * @param director
	 * @param actors
	 */
	public Movie(String title, Set<Genre> genres, String director, Set<String> actors) {
		super(title, genres);
		this.director = director;
		this.actors = actors;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see isen.java2.library.model.CulturalItem#print()
	 */
	@Override
	public void print() {
		String actorsString = new String();
		Iterator<String> it = actors.iterator();
		actorsString += it.next();
		while (it.hasNext()) {
			actorsString += ", " + it.next();
		}
		System.out.println("«" + this.title + "» directed by " + this.director + " with " + actorsString + ", score :"
				+ this.getAverageRating() + ", " + (borrowed ? "(B)" : "(A)") + ".");
	}

	/**
	 * @return the director
	 */
	public String getDirector() {
		return director;
	}

	/**
	 * @param director
	 *            the director to set
	 */
	public void setDirector(String director) {
		this.director = director;
	}

	/**
	 * @return the actors
	 */
	public Set<String> getActors() {
		return actors;
	}

	/**
	 * @param actors
	 *            the actors to set
	 */
	public void setActors(Set<String> actors) {
		this.actors = actors;
	}

}
