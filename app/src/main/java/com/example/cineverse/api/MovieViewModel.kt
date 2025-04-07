package com.example.cineverse.api

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.LiveData
import com.example.cineverse.data.Movie

class MovieViewModel : ViewModel() {

    private val movieResponse = MutableLiveData<List<Movie>>()
    val movies: LiveData<List<Movie>> get() = movieResponse

    private val responseError = MutableLiveData<String>()
    val error: LiveData<String> get() = responseError

    fun fetchMovies(page: Int = 1) {
        viewModelScope.launch {
            try {
                val response = RetrofitObject.api.getMovies(page)
                if (response.isSuccessful) {
                    movieResponse.value = response.body()?.results ?: emptyList()
                } else {
                    responseError.value = "Error: ${response.code()}"
                }
            } catch (e: Exception) {
                responseError.value = "Exception: ${e.message}"
            }
        }
    }

    fun searchMovies(query: String) {
        viewModelScope.launch {
            try {
                val response = RetrofitObject.api.searchMovies(query)
                if (response.isSuccessful) {
                    movieResponse.value = response.body()?.results ?: emptyList()
                } else {
                    responseError.value = "Error: ${response.code()}"
                }
            } catch (e: Exception) {
                responseError.value = "Exception: ${e.message}"
            }
        }
    }
}
