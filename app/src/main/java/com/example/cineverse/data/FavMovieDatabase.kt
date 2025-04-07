package com.example.cineverse.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [FavMovie::class], version = 1, exportSchema = false)
abstract class FavMovieDatabase : RoomDatabase() {
    abstract fun favMovieDao(): FavMovieDao

    companion object {
        @Volatile
        private var INSTANCE: FavMovieDatabase? = null
        fun getInstance(context: Context): FavMovieDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    FavMovieDatabase::class.java,
                    "fav_movie_database"
                ).build()

                INSTANCE = instance
                instance
            }
        }
    }
}