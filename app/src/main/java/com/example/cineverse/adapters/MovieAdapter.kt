package com.example.cineverse.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import coil3.load
import coil3.request.crossfade
import com.example.cineverse.R
import com.example.cineverse.data.FavMovie
import com.example.cineverse.data.FavMovieViewModel
import com.example.cineverse.data.Movie
import com.example.cineverse.api.Constant.IMAGE_BASE_URL
import com.google.android.material.button.MaterialButton

class MovieAdapter(
    private val favMovieViewModel: FavMovieViewModel,
    lifecycleOwner: LifecycleOwner,
    private val onMovieClick: (Movie) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private  val VIEW_TYPE_ITEM = 0
    private  val VIEW_TYPE_LOADER = 1

    private val favoriteMovies = mutableSetOf<String>()

    init {
        favMovieViewModel.allFavMovies.observe(lifecycleOwner) { favMovies ->
            favoriteMovies.clear()
            favoriteMovies.addAll(favMovies.mapNotNull { it.title }.toSet())
            notifyDataSetChanged()
        }
    }

    private var movieList: MutableList<Movie?> = mutableListOf()
    var isLoading = false

    inner class MovieViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val posterImage: ImageView = itemView.findViewById(R.id.moviePoster)
        val titleText: TextView = itemView.findViewById(R.id.movieTitle)
        val releaseDateText: TextView = itemView.findViewById(R.id.movieReleaseDate)
        val languageText: TextView = itemView.findViewById(R.id.movieLanguage)
        val ratingText: TextView = itemView.findViewById(R.id.movieRating)
        val adultText: TextView = itemView.findViewById(R.id.movieAdult)
        val popularityText: TextView = itemView.findViewById(R.id.moviePopularity)
        val videoText: TextView = itemView.findViewById(R.id.movieVideo)
        val addButton: MaterialButton = itemView.findViewById(R.id.addToFavorites)


        @Suppress("SetTextI18n")
        fun bind(movie: Movie) {
            titleText.text = movie.title
            releaseDateText.text = String.format("Release Date: ${movie.release_date}")
            languageText.text = "Language: ${movie.original_language}"
            ratingText.text = "Rating: ${movie.vote_average} (Votes: ${movie.vote_count})"
            adultText.text = "Adult Content: ${if (movie.adult) "Yes" else "No"}"
            popularityText.text = "Popularity: ${movie.popularity}"
            videoText.text = "Has Video: ${if (movie.video) "Yes" else "No"}"

            itemView.setOnClickListener {
                onMovieClick(movie)
            }

            posterImage.load("${IMAGE_BASE_URL}${movie.poster_path}") {
                crossfade(true)
            }

            val isFavorite = favoriteMovies.contains(movie.title)
            updateFavoriteButton(addButton, isFavorite)

            addButton.setOnClickListener {
                if (isFavorite) {
                    favMovieViewModel.deleteFavMovie(movie.title.toString())
                    favoriteMovies.remove(movie.title)

                } else {
                    favMovieViewModel.insertFavMovie(
                        FavMovie(
                            adult = movie.adult,
                            backdrop_path = movie.backdrop_path,
                            id = movie.id,
                            original_language = movie.original_language,
                            original_title = movie.original_title,
                            overview = movie.overview,
                            popularity = movie.popularity,
                            poster_path = movie.poster_path,
                            release_date = movie.release_date,
                            title = movie.title,
                            video = movie.video,
                            vote_average = movie.vote_average,
                            vote_count = movie.vote_count
                        )
                    )
                    favoriteMovies.add(movie.title.toString())
                }
            }
        }
    }

    inner class LoaderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun getItemViewType(position: Int): Int {
        return if (movieList[position] == null) VIEW_TYPE_LOADER else VIEW_TYPE_ITEM
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == VIEW_TYPE_ITEM) {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_movie, parent, false)
            MovieViewHolder(view)
        } else {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_loader, parent, false)
            LoaderViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is MovieViewHolder) {
            movieList[position]?.let { holder.bind(it) }
        }
    }

    override fun getItemCount(): Int = movieList.size

    fun addMovies(newMovies: List<Movie>) {
        val start = movieList.size
        movieList.addAll(newMovies)
        notifyItemRangeInserted(start, newMovies.size)
    }

    fun addLoading() {
        movieList.add(null)
        notifyItemInserted(movieList.size - 1)
    }

    fun removeLoading() {
        val position = movieList.indexOfLast { it == null }
        if (position != -1) {
            movieList.removeAt(position)
            notifyItemRemoved(position)
        }
        isLoading = false
    }

    private fun updateFavoriteButton(favoriteButton: MaterialButton, isFavorite: Boolean) {
        val context = favoriteButton.context

        if (isFavorite) {
            favoriteButton.setIconResource(R.drawable.remove)
            favoriteButton.text = String.format("Remove")
            favoriteButton.setBackgroundColor(ContextCompat.getColor(context,R.color.md_theme_secondaryContainer))
            favoriteButton.setTextColor(ContextCompat.getColorStateList(context, R.color.md_theme_onSecondaryContainer))
        } else {
            favoriteButton.setIconResource(R.drawable.favorite_filled)
            favoriteButton.text = String.format("Add")
            favoriteButton.setBackgroundColor(ContextCompat.getColor(context,R.color.md_theme_primaryContainer))
            favoriteButton.setTextColor(ContextCompat.getColorStateList(context, R.color.md_theme_onPrimaryContainer))
        }
    }

    fun clearMovies() {
        movieList.clear()
        notifyDataSetChanged()
    }

}
