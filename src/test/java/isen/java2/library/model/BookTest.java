package isen.java2.library.model;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Collections;
import java.util.HashSet;

import org.junit.jupiter.api.Test;

public class BookTest {
		
	@Test
	public void shouldGetAverageRating() {
		//GIVEN
		Book shining = new Book("Shining", new HashSet<Genre>(Collections.singleton(Genre.HORROR)),"Stephen King");
		//WHEN
		shining.addRating("James", 2);
		shining.addRating( "John", 3);
		//THEN
		assertThat(shining.getAverageRating()).isEqualTo(2.5);
	}

}
