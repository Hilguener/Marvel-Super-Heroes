package com.hilguener.marvelsuperheroes.ui.fragment

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.paging.PagingData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.hilguener.marvelsuperheroes.R
import com.hilguener.marvelsuperheroes.databinding.FragmentStoriesBinding
import com.hilguener.superheroesapp.model.stories.Story
import com.hilguener.marvelsuperheroes.presenter.AllStoriesPresenter
import com.hilguener.marvelsuperheroes.ui.activity.EventActivity
import com.hilguener.superheroesapp.ui.adapter.StoryAdapter

class StoriesFragment : Fragment() {
    private lateinit var adapter: StoryAdapter
    private lateinit var progressBar: ProgressBar
    private lateinit var presenter: AllStoriesPresenter
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var binding: FragmentStoriesBinding
    private var category: List<Story>? = listOf()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentStoriesBinding.inflate(inflater, container, false)
        presenter = AllStoriesPresenter(this)
        progressBar = binding.pbStory
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupAdapter()
        setupRecyclerView(binding.root)
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
        sharedPreferences = requireContext().getSharedPreferences(
            "com.example.superheroesapp.PREFERENCES",
            Context.MODE_PRIVATE
        )
    }

    private fun setupRecyclerView(view: View) {
        val recyclerView: RecyclerView =
            view.findViewById(R.id.rvStory) // Usando view para encontrar o RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun setupAdapter() {
        adapter = StoryAdapter {
            val intent = Intent(requireContext(), EventActivity::class.java).apply {
                putExtra("eventId", it.id)
            }
            startActivity(intent)
        }
        binding.rvStory.adapter = adapter
    }

    fun showStories(stories: List<Story>) {
        category = stories
        adapter.submitData(lifecycle, PagingData.from(stories))

        // Converter a lista de eventos em JSON
        val jsonEvents = convertCategoryListToJson(stories)

        // Salvar o JSON na SharedPreferences
        val editor = sharedPreferences.edit()
        editor.putString("cached_stories", jsonEvents)
        editor.apply()

    }


    private fun convertJsonToCategoryList(json: String): List<Story> {
        val gson = Gson()
        val listType =
            object : TypeToken<List<Story>>() {}.type
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
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }
}