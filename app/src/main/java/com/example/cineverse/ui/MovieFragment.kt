package com.example.cineverse.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import coil3.load
import coil3.request.crossfade
import com.example.cineverse.R
import com.example.cineverse.data.FavMovie
import com.example.cineverse.data.FavMovieViewModel
import com.google.android.material.button.MaterialButton

class MovieFragment : Fragment() {

    companion object {
        fun newInstance(movieData: Bundle): MovieFragment {
            val fragment = MovieFragment()
            fragment.arguments = movieData
            return fragment
        }
    }

    private lateinit var favMovieViewModel: FavMovieViewModel
    private val favoriteTitles = mutableSetOf<String>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_movie, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val args = arguments ?: return
        favMovieViewModel = FavMovieViewModel(requireActivity().application)

        val movieId = args.getInt("id")
        val adult = args.getBoolean("adult")
        val backdropPath = args.getString("backdrop_path") ?: ""
        val originalLanguage = args.getString("original_language") ?: "N/A"
        val originalTitle = args.getString("original_title") ?: "N/A"
        val overview = args.getString("overview") ?: "No overview"
        val popularity = args.getDouble("popularity", 0.0)
        val posterPath = args.getString("poster_path") ?: ""
        val releaseDate = args.getString("release_date") ?: "N/A"
        val title = args.getString("title") ?: "N/A"
        val video = args.getBoolean("video")
        val voteAverage = args.getDouble("vote_average", 0.0)
        val voteCount = args.getInt("vote_count", 0)

        val posterImageView = view.findViewById<ImageView>(R.id.movie_poster)
        val titleTextView = view.findViewById<TextView>(R.id.movie_title)
        val infoTextView = view.findViewById<TextView>(R.id.movie_info)
        val genresTextView = view.findViewById<TextView>(R.id.movie_genres)
        val ratingTextView = view.findViewById<TextView>(R.id.movie_rating)
        val overviewTextView = view.findViewById<TextView>(R.id.movie_overview)
        val favButton = view.findViewById<MaterialButton>(R.id.watch_now_button)

        posterImageView.load("https://image.tmdb.org/t/p/w1280$backdropPath") {
            crossfade(true)
        }

        titleTextView.text = title
        infoTextView.text = "$releaseDate • ${originalLanguage.uppercase()}"
        genresTextView.text = "N/A"
        ratingTextView.text = "⭐ $voteAverage ($voteCount votes)"
        overviewTextView.text = overview

        // Observe favorites
        favMovieViewModel.allFavMovies.observe(viewLifecycleOwner, Observer { favMovies ->
            favoriteTitles.clear()
            favoriteTitles.addAll(favMovies.mapNotNull { it.title })
            updateFavoriteButton(favButton, favoriteTitles.contains(title))
        })

        favButton.setOnClickListener {
            if (favoriteTitles.contains(title)) {
                favMovieViewModel.deleteFavMovie(title)
            } else {
                favMovieViewModel.insertFavMovie(
                    FavMovie(
                        adult = adult,
                        backdrop_path = backdropPath,
                        id = movieId,
                        original_language = originalLanguage,
                        original_title = originalTitle,
                        overview = overview,
                        popularity = popularity,
                        poster_path = posterPath,
                        release_date = releaseDate,
                        title = title,
                        video = video,
                        vote_average = voteAverage,
                        vote_count = voteCount
                    )
                )
            }
        }
    }

    private fun updateFavoriteButton(button: MaterialButton, isFavorite: Boolean) {
        val context = button.context
        if (isFavorite) {
            button.setIconResource(R.drawable.remove)
            button.text = "Remove"
            button.setBackgroundColor(ContextCompat.getColor(context, R.color.md_theme_secondaryContainer))
            button.setTextColor(ContextCompat.getColorStateList(context, R.color.md_theme_onSecondaryContainer))
        } else {
            button.setIconResource(R.drawable.favorite_filled)
            button.text = "Add"
            button.setBackgroundColor(ContextCompat.getColor(context, R.color.md_theme_primaryContainer))
            button.setTextColor(ContextCompat.getColorStateList(context, R.color.md_theme_onPrimaryContainer))
        }
    }
}
