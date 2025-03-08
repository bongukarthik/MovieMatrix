package com.MovieMatrix.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.MovieMatrix.models.WatchedMovie;
import com.MovieMatrix.models.User;
import com.MovieMatrix.models.Movie;
import java.util.List;

public interface WatchedMovieRepository extends JpaRepository<WatchedMovie, Long> {

  List<WatchedMovie> findByUser(User user); // Get watch history for a user

  List<WatchedMovie> findByMovie(Movie movie); // Get all users who watched a movie

  boolean existsByUserAndMovie(User user, Movie movie);
}
