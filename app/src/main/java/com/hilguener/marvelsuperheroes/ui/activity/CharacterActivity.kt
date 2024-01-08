package com.hilguener.marvelsuperheroes.ui.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.paging.PagingData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.hilguener.marvelsuperheroes.R
import com.hilguener.marvelsuperheroes.databinding.ActivityCharacterBinding
import com.hilguener.superheroesapp.model.character.Character
import com.hilguener.superheroesapp.model.comics.Comic
import com.hilguener.marvelsuperheroes.presenter.CharacterPresenter
import com.hilguener.superheroesapp.ui.adapter.ComicsAdapter


class CharacterActivity : AppCompatActivity(), ComicsAdapter.OnItemClickListener {
    private lateinit var binding: ActivityCharacterBinding
    private lateinit var pb: ProgressBar
    private lateinit var presenter: CharacterPresenter
    private lateinit var recyclerView: RecyclerView
    private lateinit var comicsAdapter: ComicsAdapter
    private val handler = Handler()

    companion object {
        const val CHARACTER_ID = "characterId"
        const val COMIC_ID = "comicId"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCharacterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        presenter = CharacterPresenter(this)
        pb = binding.pbCharacter
        recyclerView = binding.rvCharacter // Referência para o RecyclerView

        comicsAdapter = ComicsAdapter(this)
        comicsAdapter.setOnItemClickListener(this)

        recyclerView.apply {
            layoutManager =
                LinearLayoutManager(this@CharacterActivity, RecyclerView.HORIZONTAL, false)
            adapter = comicsAdapter
            // Adjust spacing as needed
        }
        val characterId = intent.getIntExtra(CHARACTER_ID, -1)
        if (characterId == -1) {
        } else {
            presenter.showCharacterInfo(characterId)
        }

        handler.postDelayed({
            binding.comicsTxt.visibility = View.VISIBLE
        }, 2000)
    }

    private fun finishWithSlideDownAnimation() {
        finish()
        overridePendingTransition(R.anim.stay, R.anim.slide_out_down)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finishWithSlideDownAnimation()
    }

    fun showProgress() {
        pb.visibility = View.VISIBLE
    }

    fun hideProgressBar() {
        handler.postDelayed({
            binding.pbCharacter.visibility = View.GONE
        }, 2000)
    }

    fun onFailure(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun onItemClick(comicId: Int) {
        val intent = Intent(this, ComicActivity::class.java)
        intent.putExtra(COMIC_ID, comicId)

        startActivity(intent)
        overridePendingTransition(R.anim.slide_in_up, R.anim.stay)
    }

    fun showCharacter(character: Character?) {
        binding.apply {
            characterName.text = character?.name
            if (character?.description.isNullOrEmpty()) {
                characterDescription.text = "No info about this character"
            } else {
                characterDescription.text = character?.description
            }
            val imageUrl =
                "${character?.thumbnail?.path}/standard_fantastic.${character?.thumbnail?.extension}"
            val requestOptions = RequestOptions().transform(RoundedCorners(20))

            Glide.with(this@CharacterActivity)
                .load(imageUrl)
                .apply(requestOptions)
                .into(binding.characterImage)
        }
    }

    fun showComics(comics: List<Comic>) {
        comicsAdapter.submitData(lifecycle, PagingData.from(comics))
    }


}
