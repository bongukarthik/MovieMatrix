package com.movieMatrix.services;

import com.movieMatrix.models.Movie;
import com.movieMatrix.repositories.MovieRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class MovieService {
  private final MovieRepository movieRepository;

  public MovieService(MovieRepository movieRepository) {
    this.movieRepository = movieRepository;
  }

  public List<Movie> getAllMovies() {
    return movieRepository.findAll();
  }

  public Optional<Movie> getMovieById(Long id) {
    return movieRepository.findById(id);
  }

  public Optional<Movie> getMovieByTitle(String title) {
    return movieRepository.findByTitle(title);
  }

  public Movie addMovie(Movie movie) {
    return movieRepository.save(movie);
  }

  public Movie updateMovie(Long id, Movie movieDetails) {
    return movieRepository.findById(id).map(movie -> {
      movie.setTitle(movieDetails.getTitle());
      movie.setGenre(movieDetails.getGenre());
      movie.setDirector(movieDetails.getDirector());
      // movie.setDescription(movieDetails.getDescription());
      movie.setActors(movieDetails.getActors());
      movie.setReleaseDate(movieDetails.getReleaseDate());
      movie.setPosterUrl(movieDetails.getPosterUrl());
      return movieRepository.save(movie);
    }).orElseThrow(() -> new RuntimeException("Movie not found"));
  }

  public Movie saveMovie(Movie movie) {
    return movieRepository.save(movie);
  }

  public boolean deleteMovie(Long id) {
    if (movieRepository.existsById(id)) {
      movieRepository.deleteById(id);
      return true;
    }
    return false;
  }
}
