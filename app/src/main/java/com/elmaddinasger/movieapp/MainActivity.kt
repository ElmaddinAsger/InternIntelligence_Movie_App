package com.elmaddinasger.movieapp

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.elmaddinasger.movieapp.databinding.ActivityMainBinding
import com.elmaddinasger.movieapp.viewModels.SharedViewModel

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val sharedViewModel = ViewModelProvider(this)[SharedViewModel::class.java]

        sharedViewModel.completedRequests.observe(this) { count ->
            if (count == 5) {
                binding.cwMainMenu.visibility = View.VISIBLE
            }
        }
    }
}