package com.hilguener.marvelsuperheroes.ui.activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.paging.PagingData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.hilguener.marvelsuperheroes.R
import com.hilguener.marvelsuperheroes.databinding.ActivityStoriesBinding
import com.hilguener.marvelsuperheroes.presenter.AllStoriesPresenter
import com.hilguener.superheroesapp.model.stories.Story
import com.hilguener.superheroesapp.ui.adapter.StoryAdapter

class StoriesActivity : AppCompatActivity() {
    private lateinit var adapter: StoryAdapter
    private lateinit var progressBar: ProgressBar
    private lateinit var presenter: AllStoriesPresenter
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var binding: ActivityStoriesBinding // Certifique-se de criar a classe ActivityStoriesBinding apropriada
    private var category: List<Story>? = listOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStoriesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        presenter = AllStoriesPresenter(this)
        progressBar = binding.pbStory

        setupAdapter()
        setupRecyclerView()
        setupSharedPreferences()

        val cachedStoriesJson = sharedPreferences.getString("cached_stories", null)

        if (cachedStoriesJson != null) {
            val cachedStories = convertJsonToCategoryList(cachedStoriesJson)
            showStories(cachedStories)
        } else {
            loadFromApi()
        }
    }

    private fun setupSharedPreferences() {
        sharedPreferences = getSharedPreferences(
            "com.example.superheroesapp.PREFERENCES",
            Context.MODE_PRIVATE
        )
    }

    private fun setupRecyclerView() {
        val recyclerView: RecyclerView = findViewById(R.id.rvStory)
        recyclerView.layoutManager = LinearLayoutManager(this)
    }

    private fun setupAdapter() {
        adapter = StoryAdapter {
            val intent = Intent(this, EventActivity::class.java).apply {
                putExtra("eventId", it.id)
            }
            startActivity(intent)
        }
        binding.rvStory.adapter = adapter
    }

    fun showStories(stories: List<Story>) {
        category = stories
        adapter.submitData(lifecycle, PagingData.from(stories))

        val jsonEvents = convertCategoryListToJson(stories)

        val editor = sharedPreferences.edit()
        editor.putString("cached_stories", jsonEvents)
        editor.apply()
    }

    private fun convertJsonToCategoryList(json: String): List<Story> {
        val gson = Gson()
        val listType = object : TypeToken<List<Story>>() {}.type
        return gson.fromJson(json, listType)
    }

    private fun convertCategoryListToJson(list: List<Story>): String {
        val gson = Gson()
        return gson.toJson(list)
    }

    private fun loadFromApi() {
        presenter.loadStories()
    }

    fun showProgress() {
        progressBar.visibility = View.VISIBLE
    }

    fun hideProgressBar() {
        binding.pbStory.visibility = View.GONE
    }

    fun onFailure(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
