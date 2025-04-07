package com.example.cineverse.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cineverse.R
import com.example.cineverse.adapters.FavAdapter
import com.example.cineverse.data.FavMovieViewModel

class FavoriteFragment : Fragment() {
    private lateinit var favRecyclerView: RecyclerView
    private lateinit var emptyStateLayout: TextView
    lateinit var fabMovieViewModel: FavMovieViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view = inflater.inflate(R.layout.fragment_favorite, container, false)
        val context = requireContext()

        favRecyclerView = view.findViewById(R.id.favFragmentRecyclerView)
        emptyStateLayout = view.findViewById(R.id.emptyStateLayout)

        favRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        fabMovieViewModel = FavMovieViewModel(requireActivity().application)

        fetchFavoriteMovies()

        return view
    }

    private fun fetchFavoriteMovies() {
        val fabMovies = fabMovieViewModel.allFavMovies

        fabMovies.observe(viewLifecycleOwner) { favMoviesList ->
            Log.d("FavoriteFragment", "Favorite movies: $favMoviesList")

            if (favMoviesList.isNullOrEmpty()) {
                emptyStateLayout.visibility = View.VISIBLE
                favRecyclerView.visibility = View.GONE
            } else {
                emptyStateLayout.visibility = View.GONE
                favRecyclerView.visibility = View.VISIBLE
            }

            favRecyclerView.adapter = FavAdapter(fabMovies, viewLifecycleOwner, fabMovieViewModel){ movie ->
                val bundle = Bundle().apply {
                    putBoolean("adult", movie.adult)
                    putString("backdrop_path", movie.backdrop_path)
                    putInt("id", movie.id)
                    putString("original_language", movie.original_language)
                    putString("original_title", movie.original_title)
                    putString("overview", movie.overview)
                    putDouble("popularity", movie.popularity)
                    putString("poster_path", movie.poster_path)
                    putString("release_date", movie.release_date)
                    putString("title", movie.title)
                    putBoolean("video", movie.video)
                    putDouble("vote_average", movie.vote_average)
                    putInt("vote_count", movie.vote_count)
                }

                val movieFragment = MovieFragment.newInstance(bundle)
                loadFragment(movieFragment)
            }
        }
    }

    private fun loadFragment(fragment: Fragment){
        parentFragmentManager.beginTransaction()
            .replace(R.id.homeFragmentContainer, fragment)
            .addToBackStack(null)
            .commit()
    }

}