package com.example.cineverse.ui

import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cineverse.R
import com.example.cineverse.adapters.MovieAdapter
import com.example.cineverse.api.MovieViewModel
import com.example.cineverse.data.FavMovieViewModel

class HomeFragment : Fragment() {

    private lateinit var homeRecyclerView: RecyclerView
    private lateinit var movieViewModel: MovieViewModel
    private lateinit var movieAdapter: MovieAdapter
    private lateinit var favMovieViewModel: FavMovieViewModel
    private lateinit var searchButton: ImageButton
    private var layoutManagerState: Parcelable? = null

    private var currentPage = 1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        val context = requireContext()

        homeRecyclerView = view.findViewById(R.id.homeRecyclerView)
        searchButton = view.findViewById(R.id.searchButton)
        movieViewModel = ViewModelProvider(requireActivity())[MovieViewModel::class.java]


        favMovieViewModel = FavMovieViewModel(requireActivity().application)

        movieAdapter = MovieAdapter(favMovieViewModel, viewLifecycleOwner) { movie ->
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

        val layoutManager = LinearLayoutManager(context)
        homeRecyclerView.layoutManager = layoutManager
        homeRecyclerView.adapter = movieAdapter

        // Restore scroll position if available
        layoutManagerState?.let {
            layoutManager.onRestoreInstanceState(it)
        }

        movieViewModel.movies.observe(viewLifecycleOwner) { movieList ->
            movieAdapter.removeLoading()
            movieAdapter.addMovies(movieList)
        }

        movieViewModel.error.observe(viewLifecycleOwner) { errorMsg ->
            movieAdapter.removeLoading()
            Toast.makeText(context, errorMsg, Toast.LENGTH_SHORT).show()
        }

        searchButton.setOnClickListener {
            loadFragment(SearchFragment())
        }

        setupScrollListener()

        if (movieViewModel.movies.value.isNullOrEmpty()) {
            movieViewModel.fetchMovies(currentPage)
        }

        return view
    }

    override fun onPause() {
        super.onPause()
        layoutManagerState = homeRecyclerView.layoutManager?.onSaveInstanceState()
    }

    private fun setupScrollListener() {
        homeRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(rv: RecyclerView, dx: Int, dy: Int) {
                if (!rv.canScrollVertically(1) && !movieAdapter.isLoading) {
                    movieAdapter.isLoading = true
                    movieAdapter.addLoading()
                    currentPage++
                    movieViewModel.fetchMovies(currentPage)
                }
            }
        })
    }

    private fun loadFragment(fragment: Fragment) {
        parentFragmentManager.beginTransaction()
            .replace(R.id.homeFragmentContainer, fragment)
            .addToBackStack(null)
            .commit()
    }
}
