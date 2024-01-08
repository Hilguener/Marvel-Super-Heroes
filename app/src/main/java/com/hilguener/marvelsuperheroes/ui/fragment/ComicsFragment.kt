package com.hilguener.superheroesapp.ui.fragment

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.paging.PagingData
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.hilguener.marvelsuperheroes.R
import com.hilguener.marvelsuperheroes.databinding.FragmentComicsBinding
import com.hilguener.superheroesapp.model.comics.Comic
import com.hilguener.marvelsuperheroes.presenter.AllComicsPresenter
import com.hilguener.marvelsuperheroes.ui.activity.ComicActivity
import com.hilguener.superheroesapp.ui.adapter.ComicsAdapter

class ComicsFragment : Fragment() {

    private lateinit var rv: RecyclerView
    private lateinit var adapter: ComicsAdapter
    private lateinit var progressBar: ProgressBar
    private lateinit var presenter: AllComicsPresenter
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var binding: FragmentComicsBinding
    private var comic: List<Comic>? = listOf()
    private var handler = Handler()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentComicsBinding.inflate(inflater, container, false)
        presenter = AllComicsPresenter(this)
        progressBar = binding.pbComics
        return binding.root


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupAdapter()
        setupRecyclerView(binding.root)
        setupSharedPreferences()

        val cachedComicsJson = sharedPreferences.getString("cached_comics", null)
        if (cachedComicsJson != null) {
            val cachedComics = convertJsonToComicList(cachedComicsJson)
            showComics(cachedComics)
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
            view.findViewById(R.id.rvComics) // Usando view para encontrar o RecyclerView
        recyclerView.layoutManager = GridLayoutManager(requireContext(), 3)
    }

    private fun setupAdapter() {
        adapter = ComicsAdapter(object : ComicsAdapter.OnItemClickListener {
            override fun onItemClick(comicId: Int) {
                val intent = Intent(requireContext(), ComicActivity::class.java).apply {
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

        // Salvar o JSON na SharedPreferences
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
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }
}