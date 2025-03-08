import axios from 'axios';

const API_URL = 'http://localhost:8080/api/movies';

export const getMovies = async () => {
    return await axios.get(API_URL);
};

export const getMovieById = async (id) => {
    return await axios.get(`${API_URL}/${id}`);
};

export const addMovie = async (movie) => {
    return await axios.post(API_URL, movie);
};
