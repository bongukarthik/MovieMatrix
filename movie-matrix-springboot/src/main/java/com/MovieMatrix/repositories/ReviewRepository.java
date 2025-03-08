package com.movieMatrix.repositories;


import org.springframework.data.jpa.repository.JpaRepository;
import com.movieMatrix.models.Review;
import com.movieMatrix.models.User;
import com.movieMatrix.models.Movie;
import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {

  List<Review> findByUser(User user); // Get all reviews by a user

  List<Review> findByMovie(Movie movie); // Get all reviews for a movie

  boolean existsByUserAndMovie(User user, Movie movie);
}
