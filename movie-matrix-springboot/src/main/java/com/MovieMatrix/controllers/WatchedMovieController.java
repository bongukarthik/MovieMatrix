package com.movieMatrix.controllers;


import com.movieMatrix.models.WatchedMovie;
import com.movieMatrix.models.User;
import com.movieMatrix.services.WatchedMovieService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/watch-history")
public class WatchedMovieController {
  private final WatchedMovieService watchedMovieService;

  public WatchedMovieController(WatchedMovieService watchedMovieService) {
    this.watchedMovieService = watchedMovieService;
  }

  @GetMapping("/{userId}")
  public ResponseEntity<List<WatchedMovie>> getWatchHistory(@PathVariable Long userId) {
    User user = new User();
    user.setId(userId);
    return ResponseEntity.ok(watchedMovieService.getWatchHistory(user));
  }

  @PostMapping
  public ResponseEntity<WatchedMovie> addToWatchHistory(@RequestBody WatchedMovie watchedMovie) {
    return ResponseEntity.ok(watchedMovieService.addToWatchHistory(watchedMovie));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<?> removeFromWatchHistory(@PathVariable Long id) {
    return watchedMovieService.removeFromWatchHistory(id)
        ? ResponseEntity.ok("Removed from watch history")
        : ResponseEntity.notFound().build();
  }
}
