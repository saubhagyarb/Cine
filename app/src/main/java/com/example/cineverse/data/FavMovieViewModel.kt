package com.example.cineverse.data

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FavMovieViewModel(application: Application) : AndroidViewModel(application)  {
    val repository = FavMovieRepository(FavMovieDatabase.getInstance(application).favMovieDao())
    val allFavMovies = repository.allFavMovies

    fun insertFavMovie(favMovie: FavMovie) = viewModelScope.launch(Dispatchers.IO) {
        repository.insertFavMovie(favMovie)
    }

    fun deleteFavMovie(title: String) = viewModelScope.launch(Dispatchers.IO) {
        repository.deleteFavMovie(title)
    }
}