package com.hilguener.marvelsuperheroes.ui.activity

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.hilguener.marvelsuperheroes.R
import com.hilguener.marvelsuperheroes.databinding.ActivityHomeBinding
import com.hilguener.marvelsuperheroes.ui.fragment.StoriesFragment
import com.hilguener.marvelsuperheroes.ui.fragment.CharactersFragment
import com.hilguener.superheroesapp.ui.fragment.ComicsFragment
import com.hilguener.superheroesapp.ui.fragment.EventsFragment


class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    private lateinit var toolbarTitleTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val bottomNavView: BottomNavigationView = binding.bottomNavView
        bottomNavView.setOnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.navigation_characters -> {
                    // Trocar para o fragmento HomeFragment
                    val charactersFragment = CharactersFragment()
                    replaceFragment(charactersFragment)
                    updateToolbarTitle("Characters")
                    true
                }

                R.id.navigation_comics -> {
                    // Trocar para o fragmento CharactersFragment
                    val comicsFragment = ComicsFragment()
                    replaceFragment(comicsFragment)
                    updateToolbarTitle("Comics")
                    true
                }

                R.id.navigation_events -> {
                    // Trocar para o fragmento CharactersFragment
                    val comicsFragment = EventsFragment()
                    replaceFragment(comicsFragment)
                    updateToolbarTitle("Events")
                    true
                }

                R.id.navigation_stories -> {
                    // Trocar para o fragmento CharactersFragment
                    val comicsFragment = StoriesFragment()
                    replaceFragment(comicsFragment)
                    updateToolbarTitle("Stories")
                    true
                }
                // Adicione os casos para outros itens do menu conforme necessário
                else -> false
            }
        }

        // Exibir o fragmento inicial ao iniciar a atividade
        val initialFragment = CharactersFragment()
        replaceFragment(initialFragment)
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, fragment)
            .commit()
    }

    private fun updateToolbarTitle(title: String) {
        supportActionBar?.title = title // Esconder o título padrão da Toolbar
    }

}






