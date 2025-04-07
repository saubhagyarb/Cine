package com.example.cineverse.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface FavMovieDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertFavMovie(favMovie: FavMovie)

    @Query("SELECT * FROM favorite_movies")
    fun getAllFavMovies(): LiveData<List<FavMovie>>

    @Query("DELETE FROM favorite_movies WHERE title = :title")
    fun deleteFavMovie(title: String)
}