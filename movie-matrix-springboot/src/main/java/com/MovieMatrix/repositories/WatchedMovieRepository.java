package com.movieMatrix.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.movieMatrix.models.WatchedMovie;
import com.movieMatrix.models.User;
import com.movieMatrix.models.Movie;
import java.util.List;

public interface WatchedMovieRepository extends JpaRepository<WatchedMovie, Long> {

  List<WatchedMovie> findByUser(User user); // Get watch history for a user

  List<WatchedMovie> findByMovie(Movie movie); // Get all users who watched a movie

  boolean existsByUserAndMovie(User user, Movie movie);
}
