package com.example.cineverse.data

import androidx.lifecycle.LiveData

class FavMovieRepository(private val favMovieDao: FavMovieDao) {

    val allFavMovies: LiveData<List<FavMovie>> = favMovieDao.getAllFavMovies()

    fun insertFavMovie(favMovie: FavMovie) {
        favMovieDao.insertFavMovie(favMovie)
    }

    fun deleteFavMovie(title: String) {
        favMovieDao.deleteFavMovie(title)
    }
}