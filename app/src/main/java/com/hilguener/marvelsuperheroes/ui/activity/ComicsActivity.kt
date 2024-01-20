package com.hilguener.marvelsuperheroes.ui.activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.view.Menu
import android.view.View
import android.widget.ProgressBar
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.paging.PagingData
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.hilguener.marvelsuperheroes.R
import com.hilguener.marvelsuperheroes.databinding.ActivityComicsBinding
import com.hilguener.marvelsuperheroes.presenter.AllComicsPresenter
import com.hilguener.superheroesapp.model.comics.Comic
import com.hilguener.superheroesapp.ui.adapter.ComicsAdapter

class ComicsActivity : AppCompatActivity() {
    private lateinit var rv: RecyclerView
    private lateinit var adapter: ComicsAdapter
    private lateinit var progressBar: ProgressBar
    private lateinit var presenter: AllComicsPresenter
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var binding: ActivityComicsBinding // Certifique-se de criar a classe ActivityComicsBinding apropriada
    private var comic: List<Comic>? = listOf()
    private var handler = Handler()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityComicsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        presenter = AllComicsPresenter(this)
        progressBar = binding.pbComics

        setupAdapter()
        setupRecyclerView()
        setupSharedPreferences()

        val cachedComicsJson = sharedPreferences.getString("cached_comics", null)
        if (cachedComicsJson != null) {
            val cachedComics = convertJsonToComicList(cachedComicsJson)
            showComics(cachedComics)
        } else {
            loadFromApi()
        }

        searchComics()
    }

    private fun setupSharedPreferences() {
        sharedPreferences = getSharedPreferences(
            "com.example.superheroesapp.PREFERENCES",
            Context.MODE_PRIVATE
        )
    }



    private fun setupRecyclerView() {
        val recyclerView: RecyclerView = findViewById(R.id.rvComics)
        recyclerView.layoutManager = GridLayoutManager(this, 3)
    }

    private fun setupAdapter() {
        adapter = ComicsAdapter(object : ComicsAdapter.OnItemClickListener {
            override fun onItemClick(comicId: Int) {
                val intent = Intent(this@ComicsActivity, ComicActivity::class.java).apply {
                    putExtra("comicId", comicId)
                }
                startActivity(intent)
            }
        })
        binding.rvComics.adapter = adapter
    }

    fun showComics(comics: List<Comic>) {
        comic = comics
        adapter.submitData(lifecycle, PagingData.from(comics))

        val jsonComics = convertComicListToJson(comics)
        val editor = sharedPreferences.edit()
        editor.putString("cached_comics", jsonComics)
        editor.apply()
    }

    private fun convertJsonToComicList(json: String): List<Comic> {
        val gson = Gson()
        val listType = object : TypeToken<List<Comic>>() {}.type
        return gson.fromJson(json, listType)
    }

    private fun convertComicListToJson(list: List<Comic>): String {
        val gson = Gson()
        return gson.toJson(list)
    }

    private fun loadFromApi() {
        presenter.loadComics()
    }

    fun showProgress() {
        progressBar.visibility = View.VISIBLE
    }

    fun hideProgressBar() {
        handler.postDelayed({
            binding.pbComics.visibility = View.GONE
        }, 2000)
    }

    fun onFailure(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    fun searchComics() {
        val searchView: androidx.appcompat.widget.SearchView = findViewById(R.id.searchView)

        searchView.queryHint = "search comics"

        searchView.setOnQueryTextListener(object :
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                // Atualizar a consulta na fonte de dados do Paging
                presenter.searchComics(newText.orEmpty())
                // Se o texto da consulta estiver vazio, recarregue os dados iniciais no RecyclerView
                if (newText.isNullOrBlank()) {
                    loadFromApi()
                }

                return true
            }
        })
    }
}
