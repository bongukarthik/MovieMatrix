package com.MovieMatrix.services;

import com.MovieMatrix.models.Review;
import com.MovieMatrix.models.User;
import com.MovieMatrix.models.Movie;
import com.MovieMatrix.repositories.ReviewRepository;
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
