package com.hilguener.marvelsuperheroes.ui.activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.paging.PagingData
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.hilguener.marvelsuperheroes.R
import com.hilguener.marvelsuperheroes.databinding.ActivityCharactersBinding
import com.hilguener.marvelsuperheroes.datasource.callback.BaseView
import com.hilguener.marvelsuperheroes.model.series.Serie
import com.hilguener.marvelsuperheroes.presenter.AllCharactersPresenter
import com.hilguener.marvelsuperheroes.ui.adapter.CharacterAdapter
import com.hilguener.superheroesapp.model.character.Character

class CharactersActivity : AppCompatActivity(), BaseView {
    private lateinit var adapter: CharacterAdapter
    private lateinit var progressBar: ProgressBar
    private lateinit var presenter: AllCharactersPresenter
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var binding: ActivityCharactersBinding
    private var character: List<Character>? = listOf()
    private var handler = Handler()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCharactersBinding.inflate(layoutInflater)
        setContentView(binding.root)

        presenter = AllCharactersPresenter(this)
        progressBar = binding.pb

        setupAdapter()
        setupRecyclerView()
        setupSharedPreferences()

        val cachedCharactersJson = sharedPreferences.getString("cached_characters", null)
        if (cachedCharactersJson != null) {
            val cachedCharacters = convertJsonToCategoryList(cachedCharactersJson)
            showCharacters(cachedCharacters)
        } else {
            loadFromApi()

        }
        searchCharacter()

    }

    private fun setupSharedPreferences() {
        sharedPreferences = getSharedPreferences(
            "com.example.superheroesapp.PREFERENCES",
            Context.MODE_PRIVATE
        )
    }

    private fun setupRecyclerView() {
        val recyclerView: RecyclerView = findViewById(R.id.rvMain)
        recyclerView.layoutManager = GridLayoutManager(this, 3)
    }

    private fun setupAdapter() {
        adapter = CharacterAdapter {
            val intent = Intent(this, CharacterActivity::class.java).apply {
                putExtra("characterId", it.id)
            }
            startActivity(intent)
        }
        binding.rvMain.adapter = adapter
    }

    override fun showCharacters(characters: List<Character>) {
        character = characters
        adapter.submitData(lifecycle, PagingData.from(characters))

        val jsonCharacters = convertCategoryListToJson(characters)
        val editor = sharedPreferences.edit()
        editor.putString("cached_characters", jsonCharacters)
        editor.apply()
    }

    private fun convertJsonToCategoryList(json: String): List<Character> {
        val gson = Gson()
        val listType = object : TypeToken<List<Character>>() {}.type
        return gson.fromJson(json, listType)
    }

    private fun convertCategoryListToJson(list: List<Character>): String {
        val gson = Gson()
        return gson.toJson(list)
    }

    private fun loadFromApi() {
        presenter.loadCharacters()
    }

    override fun showProgress() {
        progressBar.visibility = View.VISIBLE
    }

    override fun hideProgressBar() {
        handler.postDelayed({
            binding.pb.visibility = View.GONE
        }, 2000)
    }

    override fun onFailure(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun showSeries(series: List<Serie>) {
    }

    fun searchCharacter() {
        val searchView: androidx.appcompat.widget.SearchView = findViewById(R.id.searchView)

        searchView.queryHint = "search characters"

        searchView.setOnQueryTextListener(object :
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {

                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                presenter.searchCharacters(newText.orEmpty())
                if (newText.isNullOrBlank()) {
                    loadFromApi()
                }
                return true
            }
        })
    }
}

