package com.example.cineverse.ui

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cineverse.R
import com.example.cineverse.adapters.MovieAdapter
import com.example.cineverse.api.MovieViewModel
import com.example.cineverse.data.FavMovieViewModel
import com.google.android.material.textfield.TextInputEditText

class SearchFragment : Fragment() {

    private lateinit var movieViewModel: MovieViewModel
    private lateinit var movieAdapter: MovieAdapter
    private lateinit var favMovieViewModel: FavMovieViewModel
    private lateinit var searchButton: ImageButton
    private lateinit var searchRecyclerView: RecyclerView
    private lateinit var searchBar: EditText

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_search, container, false)
        init(view)
        setupRecyclerView()
        setupSearchBar()
        observeViewModel()

        return view
    }

    private fun init(view : View){
        searchRecyclerView = view.findViewById(R.id.search_recycler_view)
        searchButton = view.findViewById(R.id.searchButton)
        searchBar = view.findViewById(R.id.searchInput)
        movieViewModel = ViewModelProvider(this)[MovieViewModel::class.java]
    }

    private fun setupRecyclerView() {
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

        searchRecyclerView.adapter = movieAdapter
        searchRecyclerView.layoutManager = LinearLayoutManager(context)
    }

    private fun loadFragment(fragment: Fragment){
        parentFragmentManager.beginTransaction()
            .replace(R.id.homeFragmentContainer, fragment)
            .addToBackStack(null)
            .commit()
    }

    private fun setupSearchBar() {
        searchBar.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                movieViewModel.searchMovies(s?.toString() ?: "")
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun observeViewModel() {
        movieViewModel.movies.observe(viewLifecycleOwner) { movies ->
            movieAdapter.clearMovies()
            movieAdapter.addMovies(movies)
        }
    }
}