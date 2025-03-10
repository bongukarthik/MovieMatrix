package com.movieMatrix.models;

import java.util.List;

import jakarta.persistence.*;
import lombok.*;
import lombok.Data;

@Entity
@Table(name = "movies")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Movie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String genre;
    private String director;
    private String releaseDate;
    private double rating;
    @ElementCollection
    private List<String> actors; // List of actors in the movie
    @OneToMany(mappedBy = "movie", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Review> reviews;
    private String posterUrl; // URL for movie poster image
}
