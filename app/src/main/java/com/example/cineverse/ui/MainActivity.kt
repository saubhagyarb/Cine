package com.example.cineverse.ui

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Observer
import coil3.load
import coil3.request.crossfade
import com.example.cineverse.R
import com.example.cineverse.api.MovieViewModel
import com.google.android.material.button.MaterialButton

class MainActivity : AppCompatActivity() {
    lateinit var getStartedBtn : MaterialButton

    lateinit var poster1 : ImageView
    lateinit var poster2 : ImageView
    lateinit var poster3 : ImageView
    lateinit var poster4 : ImageView
    lateinit var poster5 : ImageView
    lateinit var poster6 : ImageView
    lateinit var poster7 : ImageView
    lateinit var poster8 : ImageView
    lateinit var poster9 : ImageView
    lateinit var poster10 : ImageView
    lateinit var poster11 : ImageView
    lateinit var poster12 : ImageView

    lateinit var movieViewModel : MovieViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(
                WindowInsetsCompat.Type.systemBars() or WindowInsetsCompat.Type.displayCutout())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        movieViewModel = MovieViewModel()
        getStartedBtn = findViewById(R.id.getStartedBtn)

        poster1 = findViewById(R.id.poster1)
        poster2 = findViewById(R.id.poster2)
        poster3 = findViewById(R.id.poster3)
        poster4 = findViewById(R.id.poster4)
        poster5 = findViewById(R.id.poster5)
        poster6 = findViewById(R.id.poster6)
        poster7 = findViewById(R.id.poster7)
        poster8 = findViewById(R.id.poster8)
        poster9 = findViewById(R.id.poster9)
        poster10 = findViewById(R.id.poster10)
        poster11 = findViewById(R.id.poster11)
        poster12 = findViewById(R.id.poster12)

        movieViewModel.movies.observe(this, Observer { movieList ->
            val imageViews = listOf(
                poster1, poster2, poster3, poster4,
                poster5, poster6, poster7, poster8,
                poster9, poster10, poster11, poster12
            )

            movieList.take(12).forEachIndexed { index, movie ->
                val imageUrl = "https://image.tmdb.org/t/p/w500${movie.poster_path}"
                imageViews.getOrNull(index)?.load(imageUrl) {
                    crossfade(true)
                }
            }
        })

        movieViewModel.fetchMovies()




        getStartedBtn.setOnClickListener {
            startActivity(Intent(this, HomeActivity::class.java))
            finish()
        }
    }
}