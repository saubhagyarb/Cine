package com.example.cineverse.ui

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.example.cineverse.R
import com.example.cineverse.data.FavMovieViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView

class HomeActivity : AppCompatActivity() {
    private lateinit var bottomNavigationView: BottomNavigationView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_home)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.homeScreen)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.displayCutout() or WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val homeFragment = HomeFragment()
        val favoriteFragment = FavoriteFragment()
        val aboutFragment = AboutFragment()
        bottomNavigationView = findViewById(R.id.bottomNavigationView)


        setFragment(homeFragment)
        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.mHome -> {
                    setFragment(homeFragment)
                    item.setIcon(R.drawable.home_filled)
                }
                R.id.mFavorite -> {
                    setFragment(favoriteFragment)
                    item.setIcon(R.drawable.favorite_filled)
                }
                R.id.mAbout -> {
                    setFragment(aboutFragment)
                    item.setIcon(R.drawable.about_filled)
                }
            }

            // Reset other icons when a new one is selected
            bottomNavigationView.menu.findItem(R.id.mHome).apply {
                if (item.itemId != R.id.mHome) setIcon(R.drawable.home)
            }
            bottomNavigationView.menu.findItem(R.id.mFavorite).apply {
                if (item.itemId != R.id.mFavorite) setIcon(R.drawable.favorite)
            }
            bottomNavigationView.menu.findItem(R.id.mAbout).apply {
                if (item.itemId != R.id.mAbout) setIcon(R.drawable.about)
            }

            true
        }


        val favMovieViewModel = FavMovieViewModel(application)
        favMovieViewModel.allFavMovies.observe(this@HomeActivity) { favMovies ->
            bottomNavigationView.getOrCreateBadge(R.id.mFavorite).apply {
                number = favMovies.size
                isVisible = number > 0
            }
        }
    }

    private fun setFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.homeFragmentContainer, fragment)
            commit()
        }
    }
}