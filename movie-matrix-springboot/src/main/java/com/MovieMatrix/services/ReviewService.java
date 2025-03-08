package com.movieMatrix.services;

import com.movieMatrix.models.Review;
import com.movieMatrix.models.User;
import com.movieMatrix.models.Movie;
import com.movieMatrix.repositories.ReviewRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ReviewService {
  private final ReviewRepository reviewRepository;

  public ReviewService(ReviewRepository reviewRepository) {
    this.reviewRepository = reviewRepository;
  }

  public List<Review> getReviewsByUser(User user) {
    return reviewRepository.findByUser(user);
  }

  public List<Review> getReviewsByMovie(Movie movie) {
    return reviewRepository.findByMovie(movie);
  }

  public Review saveReview(Review review) {
    return reviewRepository.save(review);
  }

  public boolean deleteReview(Long id) {
    if (reviewRepository.existsById(id)) {
      reviewRepository.deleteById(id);
      return true;
    }
    return false;
  }
}
