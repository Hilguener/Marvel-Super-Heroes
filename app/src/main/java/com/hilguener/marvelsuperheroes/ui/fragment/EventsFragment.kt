package com.hilguener.superheroesapp.ui.fragment

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
import com.hilguener.marvelsuperheroes.databinding.FragmentEventsBinding
import com.hilguener.superheroesapp.model.events.Event
import com.hilguener.marvelsuperheroes.presenter.AllEventsPresenter
import com.hilguener.marvelsuperheroes.ui.activity.EventActivity
import com.hilguener.superheroesapp.ui.adapter.EventAdapter


class EventsFragment : Fragment() {
    private lateinit var adapter: EventAdapter
    private lateinit var progressBar: ProgressBar
    private lateinit var presenter: AllEventsPresenter
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var binding: FragmentEventsBinding
    private var category: List<Event>? = listOf()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentEventsBinding.inflate(inflater, container, false)
        presenter = AllEventsPresenter(this)
        progressBar = binding.pbEvents
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupAdapter()
        setupRecyclerView(binding.root)
        setupSharedPreferences()

        val cachedEventsJson = sharedPreferences.getString("cached_events", null)

        if (cachedEventsJson != null) {
            val cachedEvents = convertJsonToCategoryList(cachedEventsJson)
            showEvents(cachedEvents)
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
            view.findViewById(R.id.rvEvents) // Usando view para encontrar o RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun setupAdapter() {
        adapter = EventAdapter {
            val intent = Intent(requireContext(), EventActivity::class.java).apply {
                putExtra("eventId", it.id)
            }
            startActivity(intent)
        }
        binding.rvEvents.adapter = adapter
    }

    fun showEvents(events: List<Event>) {
        category = events
        adapter.submitData(lifecycle, PagingData.from(events))

        // Converter a lista de eventos em JSON
        val jsonEvents = convertCategoryListToJson(events)

        // Salvar o JSON na SharedPreferences
        val editor = sharedPreferences.edit()
        editor.putString("cached_events", jsonEvents)
        editor.apply()

        if (events.isNotEmpty()) {
            hideProgressBar() // Se a lista de eventos não estiver vazia, oculta o ProgressBar
        }
    }


    private fun convertJsonToCategoryList(json: String): List<Event> {
        val gson = Gson()
        val listType =
            object : TypeToken<List<Event>>() {}.type
        return gson.fromJson(json, listType)
    }

    private fun convertCategoryListToJson(list: List<Event>): String {
        val gson = Gson()
        return gson.toJson(list)
    }


    private fun loadFromApi() {
        presenter.loadEvents()
    }

    fun showProgress() {
        progressBar.visibility = View.VISIBLE
    }

    fun hideProgressBar() {
        binding.pbEvents.visibility = View.GONE

    }

    fun onFailure(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }
}
