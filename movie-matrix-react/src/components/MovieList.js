import React, { useEffect, useState } from 'react';
import { getMovies } from '../api/movieApi';

const MovieList = () => {
    const [movies, setMovies] = useState([]);

    useEffect(() => {
        getMovies().then(response => setMovies(response.data));
    }, []);

    return (
        <div>
            <h2>Movie List</h2>
            <ul>
                {movies.map(movie => (
                    <li key={movie.id}>
                        <strong>{movie.title}</strong> - {movie.genre}
                    </li>
                ))}
            </ul>
        </div>
    );
};

export default MovieList;
