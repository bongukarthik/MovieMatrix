package com.MovieMatrix.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.MovieMatrix.models.Movie;
import java.util.List;
import java.util.Optional;

public interface MovieRepository extends JpaRepository<Movie, Long> {

  Optional<Movie> findByTitle(String title);

  List<Movie> findByGenre(String genre);

  List<Movie> findByDirector(String director);

  List<Movie> findByTitleContainingIgnoreCase(String title); // Search movies by title

  boolean existsByTitle(String title);
}
