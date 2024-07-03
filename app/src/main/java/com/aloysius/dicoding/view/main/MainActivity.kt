package com.aloysius.dicoding.view.main

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.aloysius.dicoding.R
import com.aloysius.dicoding.databinding.ActivityMainBinding
import com.aloysius.dicoding.di.Injection
import com.aloysius.dicoding.view.ViewModelFactory
import com.aloysius.dicoding.view.maps.MapsActivity
import com.aloysius.dicoding.view.upload.AddStoryActivity
import com.aloysius.dicoding.view.welcome.WelcomeActivity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private val viewModel by viewModels<MainViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: StoryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        binding.recyclerView.layoutManager = LinearLayoutManager(this)

        lifecycleScope.launch {
            viewModel.getSession().observe(this@MainActivity) { user ->
                if (!user.isLogin) {
                    navigateToWelcome()
                } else {
                    getData()
                }
            }
        }
        getData()
        setupView()
        setupAction()
    }

    private fun getData() {
        val adapter = StoryAdapter()
        binding.recyclerView.adapter = adapter.withLoadStateFooter(
            footer = LoadingStateAdapter {
                adapter.retry()
            }
        )
        viewModel.stories.observe(this) {
            adapter.submitData(lifecycle, it)
        }
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_logout -> {
                lifecycleScope.launch {
                    try {
                        viewModel.logout()
                        Log.d("MainActivity", "User logged out, navigating to WelcomeActivity")
                    } catch (e: Exception) {
                        showToast("Failed to logout: ${e.message}")
                    }
                }
                true
            }
            R.id.action_map -> {
                lifecycleScope.launch {
                    try {
                        val userPreference = Injection.provideUserPreference(applicationContext)
                        val token = userPreference.getToken().first() ?: ""
                        val intent = Intent(this@MainActivity, MapsActivity::class.java)
                        intent.putExtra("TOKEN", token)
                        startActivity(intent)
                    } catch (e: Exception) {
                        showToast("Failed to open MapsActivity: ${e.message}")
                    }
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun setupView() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.setDisplayShowTitleEnabled(false)
        adapter = StoryAdapter()
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = this@MainActivity.adapter
        }
        getData()
    }

    private fun setupAction() {
        binding.floating.setOnClickListener {
            lifecycleScope.launch {
                try {
                    val userPreference = Injection.provideUserPreference(applicationContext)
                    val token = userPreference.getToken().first() ?: ""
                    val intent = Intent(this@MainActivity, AddStoryActivity::class.java)
                    intent.putExtra("TOKEN", token)
                    startActivity(intent)
                } catch (e: Exception) {
                    showToast("Failed to open AddStoryActivity: ${e.message}")
                }
            }
        }
    }

    private fun navigateToWelcome() {
        val intent = Intent(this, WelcomeActivity::class.java)
        Log.d("MainActivity", "Navigating to WelcomeActivity")
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}