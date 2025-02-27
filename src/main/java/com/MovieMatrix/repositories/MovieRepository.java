package com.MovieMatrix.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.MovieMatrix.models.Movie;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {
}
