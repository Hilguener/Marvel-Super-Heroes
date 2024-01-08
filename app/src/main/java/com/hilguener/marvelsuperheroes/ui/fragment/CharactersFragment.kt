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
import com.hilguener.marvelsuperheroes.databinding.FragmentCharactersBinding
import com.hilguener.marvelsuperheroes.presenter.AllCharactersPresenter
import com.hilguener.superheroesapp.model.character.Character
import com.hilguener.marvelsuperheroes.ui.activity.CharacterActivity
import com.hilguener.superheroesapp.ui.adapter.CharacterAdapter

class CharactersFragment : Fragment() {
    private lateinit var adapter: CharacterAdapter
    private lateinit var progressBar: ProgressBar
    private lateinit var presenter: AllCharactersPresenter
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var binding: FragmentCharactersBinding
    private var category: List<Character>? = listOf()
    private var handler = Handler()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCharactersBinding.inflate(inflater, container, false)
        presenter = AllCharactersPresenter(this)
        progressBar = binding.pb
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupAdapter()
        setupRecyclerView(binding.root)
        setupSharedPreferences()

        val cachedCharactersJson = sharedPreferences.getString("cached_characters", null)
        if (cachedCharactersJson != null) {
            val cachedEvents = convertJsonToCategoryList(cachedCharactersJson)
            showCharacters(cachedEvents)
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
            view.findViewById(R.id.rvMain) // Usando view para encontrar o RecyclerView
        recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
    }

    private fun setupAdapter() {
        adapter = CharacterAdapter {
            val intent = Intent(requireContext(), CharacterActivity::class.java).apply {
                putExtra("characterId", it.id)
            }
            startActivity(intent)
        }
        binding.rvMain.adapter = adapter
    }


    fun showCharacters(characters: List<Character>) {
        category = characters
        adapter.submitData(lifecycle, PagingData.from(characters))

        // Converter a lista de eventos em JSON
        val jsonCharacters = convertCategoryListToJson(characters)

        // Salvar o JSON na SharedPreferences
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

    fun showProgress() {
        progressBar.visibility = View.VISIBLE
    }

    fun hideProgressBar() {
        handler.postDelayed({
            binding.pb.visibility = View.GONE
        }, 2000)
    }

    fun onFailure(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }
}
