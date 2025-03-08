package com.MovieMatrix.services;

import com.MovieMatrix.models.WatchedMovie;
import com.MovieMatrix.models.User;
import com.MovieMatrix.repositories.WatchedMovieRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class WatchedMovieService {
  private final WatchedMovieRepository watchedMovieRepository;

  public WatchedMovieService(WatchedMovieRepository watchedMovieRepository) {
    this.watchedMovieRepository = watchedMovieRepository;
  }

  public List<WatchedMovie> getWatchHistory(User user) {
    return watchedMovieRepository.findByUser(user);
  }

  public WatchedMovie addToWatchHistory(WatchedMovie watchedMovie) {
    return watchedMovieRepository.save(watchedMovie);
  }

  public boolean removeFromWatchHistory(Long id) {
    if (watchedMovieRepository.existsById(id)) {
      watchedMovieRepository.deleteById(id);
      return true;
    }
    return false;
  }
}
