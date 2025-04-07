package com.example.cineverse.api

import com.example.cineverse.data.MovieResponse
import com.example.cineverse.api.Constant.API_KEY
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query


interface MovieApiService {
    @GET("/3/discover/movie?sort_by=popularity.desc&api_key=${API_KEY}")
    suspend fun getMovies(@Query("page") page: Int): Response<MovieResponse>

    @GET("/3/search/movie?api_key=${API_KEY}&query")
    suspend fun searchMovies(@Query("query") query: String): Response<MovieResponse>
}
