package com.example.cineverse.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import coil3.load
import coil3.request.crossfade
import com.example.cineverse.R
import com.example.cineverse.data.FavMovie
import com.example.cineverse.data.FavMovieViewModel
import com.google.android.material.button.MaterialButton

class FavAdapter(
    favMoviesLiveData: LiveData<List<FavMovie>>,
    lifecycleOwner: LifecycleOwner,
    private val favMovieViewModel: FavMovieViewModel,
    private val onMovieClick: (FavMovie) -> Unit
) : RecyclerView.Adapter<FavAdapter.FavViewHolder>() {

    private var favMovies: List<FavMovie> = emptyList()

    init {
        favMoviesLiveData.observe(lifecycleOwner) { updatedMovies ->
            favMovies = updatedMovies
        }

    }

    inner class FavViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val posterImage: ImageView = itemView.findViewById(R.id.moviePoster)
        val titleText: TextView = itemView.findViewById(R.id.movieTitle)
        val releaseDateText: TextView = itemView.findViewById(R.id.movieReleaseDate)
        val languageText: TextView = itemView.findViewById(R.id.movieLanguage)
        val ratingText: TextView = itemView.findViewById(R.id.movieRating)
        val adultText: TextView = itemView.findViewById(R.id.movieAdult)
        val popularityText: TextView = itemView.findViewById(R.id.moviePopularity)
        val videoText: TextView = itemView.findViewById(R.id.movieVideo)
        val addButton: MaterialButton = itemView.findViewById(R.id.addToFavorites)

        fun bind(movie: FavMovie) {
            titleText.text = movie.title
            releaseDateText.text = String.format("Release Date: ${movie.release_date}")
            languageText.text = String.format("Language: ${movie.original_language}")
            ratingText.text = String.format("Rating: ${movie.vote_average} (Votes: ${movie.vote_count})")
            adultText.text = String.format("Adult Content: ${if (movie.adult) "Yes" else "No"}")
            popularityText.text = String.format("Popularity: ${movie.popularity}")
            videoText.text = String.format("Has Video: ${if (movie.video) "Yes" else "No"}")

            posterImage.load("https://image.tmdb.org/t/p/w1280" + movie.poster_path) {
                crossfade(true)
            }

            addButton.apply {
                text = String.format("Remove")
                icon = ContextCompat.getDrawable(context, R.drawable.remove)
            }

            addButton.setOnClickListener {
                favMovieViewModel.deleteFavMovie(movie.title.toString())
            }

            itemView.setOnClickListener { onMovieClick(movie) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_movie, parent, false)
        return FavViewHolder(view)
    }

    override fun onBindViewHolder(holder: FavViewHolder, position: Int) {
        holder.bind(favMovies[position])
    }

    override fun getItemCount(): Int = favMovies.size
}