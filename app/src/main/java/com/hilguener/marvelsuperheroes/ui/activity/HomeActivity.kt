package com.hilguener.marvelsuperheroes.ui.activity

import android.content.Intent
import android.content.res.Resources
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.paging.PagingData
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.hilguener.marvelsuperheroes.R
import com.hilguener.marvelsuperheroes.databinding.ActivityHomeScreenBinding
import com.hilguener.marvelsuperheroes.datasource.callback.BaseView
import com.hilguener.marvelsuperheroes.model.series.Serie
import com.hilguener.marvelsuperheroes.presenter.AllCharactersPresenter
import com.hilguener.marvelsuperheroes.presenter.AllSeriesPresenter
import com.hilguener.marvelsuperheroes.ui.adapter.CharacterHomeScreenAdapter
import com.hilguener.marvelsuperheroes.ui.adapter.SeriesAdapter
import com.hilguener.superheroesapp.model.character.Character

class HomeActivity : AppCompatActivity(), BaseView {
    private lateinit var binding: ActivityHomeScreenBinding
    private lateinit var pb: ProgressBar
    private lateinit var charactersAdapter: CharacterHomeScreenAdapter
    private lateinit var seriesAdapter: SeriesAdapter
    private lateinit var charactersPresenter: AllCharactersPresenter
    private lateinit var seriesPresenter: AllSeriesPresenter
    private var series: List<Serie>? = listOf()
    private var character: List<Character>? = listOf()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        pb = binding.pbHomeScreen
        charactersPresenter = AllCharactersPresenter(this)
        seriesPresenter = AllSeriesPresenter(this)


        setupAdapter()
        setupRecyclerView()
        setupSeriesRecyclerView()

        goToComics()
        goToCharacters()
        goToEvents()
        goToStories()
        goToYoutube()

        loadFromApi()
        displayUserName()
    }

    private fun setupAdapter() {
        charactersAdapter = CharacterHomeScreenAdapter {
            val intent = Intent(this, CharacterActivity::class.java).apply {
                putExtra("characterId", it.id)
            }
            startActivity(intent)
        }
        binding.rvCharactersMain.adapter = charactersAdapter
    }

    private fun setupSeriesRecyclerView() {
        val seriesRecyclerView: RecyclerView = findViewById(R.id.rv_series)
        seriesRecyclerView.layoutManager =
            GridLayoutManager(this@HomeActivity, 2, RecyclerView.HORIZONTAL, false)
        seriesAdapter = SeriesAdapter {
            // Lógica para o clique em um item da série, se necessário
        }
        seriesRecyclerView.adapter = seriesAdapter
    }


    private fun displayUserName() {
        val user = FirebaseAuth.getInstance().currentUser
        val userName = user?.displayName

        if (!userName.isNullOrBlank()) {
            binding.userName.text = "Olá, $userName!"
        } else {
            // Se o nome do usuário não estiver disponível, ajuste conforme necessário
            binding.userName.text = "Olá, Usuário!"
        }
    }

    private fun goToComics() {
        val cardComics = binding.comicsCardView
        cardComics.setOnClickListener {
            val intent = Intent(this, ComicsActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setupRecyclerView() {
        val recyclerView: RecyclerView = findViewById(R.id.rvCharactersMain)
        recyclerView.layoutManager =
            LinearLayoutManager(this@HomeActivity, RecyclerView.HORIZONTAL, false)
    }

    private fun goToCharacters() {
        val cardComics = binding.charactersCardView
        cardComics.setOnClickListener {
            val intent = Intent(this, CharactersActivity::class.java)
            startActivity(intent)
        }
    }

    private fun loadFromApi() {
        charactersPresenter.loadCharacters()
        seriesPresenter.loadSeries()
    }

    private fun goToEvents() {
        val cardComics = binding.eventsCardView
        cardComics.setOnClickListener {
            val intent = Intent(this, EventsActivity::class.java)
            startActivity(intent)
        }
    }

    private fun goToStories() {
        val cardComics = binding.storiesCardView
        cardComics.setOnClickListener {
            val intent = Intent(this, StoriesActivity::class.java)
            startActivity(intent)
        }
    }

    private fun goToYoutube() {
        val youtubeChannelUrl = "https://www.youtube.com/@marvel"

        binding.goToYoutube.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(youtubeChannelUrl))

            // Verifica se há um aplicativo do YouTube instalado
            if (intent.resolveActivity(packageManager) != null) {
                startActivity(intent)
            } else {
                // Se não houver aplicativo do YouTube, abrir no navegador
                val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(youtubeChannelUrl))
                startActivity(browserIntent)
            }
        }
    }


    override fun showCharacters(characters: List<Character>) {
        character = characters
        charactersAdapter.submitData(lifecycle, PagingData.from(characters))
    }


    override fun onFailure(message: String) {
        Log.e("Erro recycler", message)
    }

    override fun showSeries(series: List<Serie>) {
        this.series = series
        seriesAdapter.submitData(lifecycle, PagingData.from(series))
    }

    override fun showProgress() {
        pb.visibility = View.VISIBLE
    }

    override fun hideProgressBar() {
        pb.visibility = View.GONE
    }

}