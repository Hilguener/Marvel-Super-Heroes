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
import com.hilguener.marvelsuperheroes.databinding.ActivityEventsBinding
import com.hilguener.marvelsuperheroes.presenter.AllEventsPresenter
import com.hilguener.superheroesapp.model.events.Event
import com.hilguener.superheroesapp.ui.adapter.EventAdapter


class EventsActivity : AppCompatActivity() {
    private lateinit var adapter: EventAdapter
    private lateinit var progressBar: ProgressBar
    private lateinit var presenter: AllEventsPresenter
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var binding: ActivityEventsBinding // Certifique-se de criar a classe ActivityEventsBinding apropriada
    private var category: List<Event>? = listOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEventsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        presenter = AllEventsPresenter(this)
        progressBar = binding.pbEvents

        setupAdapter()
        setupRecyclerView()
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
        sharedPreferences = getSharedPreferences(
            "com.example.superheroesapp.PREFERENCES",
            Context.MODE_PRIVATE
        )
    }

    private fun setupRecyclerView() {
        val recyclerView: RecyclerView = findViewById(R.id.rvEvents)
        recyclerView.layoutManager = LinearLayoutManager(this)
    }

    private fun setupAdapter() {
        adapter = EventAdapter {
            val intent = Intent(this, EventActivity::class.java).apply {
                putExtra("eventId", it.id)
            }
            startActivity(intent)
        }
        binding.rvEvents.adapter = adapter
    }

    fun showEvents(events: List<Event>) {
        category = events
        adapter.submitData(lifecycle, PagingData.from(events))

        val jsonEvents = convertCategoryListToJson(events)
        val editor = sharedPreferences.edit()
        editor.putString("cached_events", jsonEvents)
        editor.apply()

        if (events.isNotEmpty()) {
            hideProgressBar() // Se a lista de eventos não estiver vazia, oculta o ProgressBar
        }
    }

    private fun convertJsonToCategoryList(json: String): List<Event> {
        val gson = Gson()
        val listType = object : TypeToken<List<Event>>() {}.type
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
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}

