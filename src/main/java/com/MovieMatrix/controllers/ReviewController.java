package com.MovieMatrix.controllers;

import com.MovieMatrix.models.Review;
import com.MovieMatrix.models.User;
import com.MovieMatrix.models.Movie;
import com.MovieMatrix.services.ReviewService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reviews")
public class ReviewController {
  private final ReviewService reviewService;

  public ReviewController(ReviewService reviewService) {
    this.reviewService = reviewService;
  }

  @GetMapping("/user/{userId}")
  public ResponseEntity<List<Review>> getReviewsByUser(@PathVariable Long userId) {
    User user = new User();
    user.setId(userId);
    return ResponseEntity.ok(reviewService.getReviewsByUser(user));
  }

  @GetMapping("/movie/{movieId}")
  public ResponseEntity<List<Review>> getReviewsByMovie(@PathVariable Long movieId) {
    Movie movie = new Movie();
    movie.setId(movieId);
    return ResponseEntity.ok(reviewService.getReviewsByMovie(movie));
  }

  @PostMapping
  public ResponseEntity<Review> addReview(@RequestBody Review review) {
    return ResponseEntity.ok(reviewService.saveReview(review));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<?> deleteReview(@PathVariable Long id) {
    return reviewService.deleteReview(id) ? ResponseEntity.ok("Review deleted successfully")
        : ResponseEntity.notFound().build();
  }
}
