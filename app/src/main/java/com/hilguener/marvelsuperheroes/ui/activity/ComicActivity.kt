package com.hilguener.marvelsuperheroes.ui.activity

import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.hilguener.marvelsuperheroes.R
import com.hilguener.marvelsuperheroes.databinding.ActivityComicBinding
import com.hilguener.superheroesapp.model.comics.Comic
import com.hilguener.superheroesapp.model.comics.Date
import com.hilguener.superheroesapp.model.comics.Price
import com.hilguener.marvelsuperheroes.presenter.ComicPresenter

class ComicActivity : AppCompatActivity() {


    companion object {
        const val COMIC_ID = "comicId"
    }

    private lateinit var presenter: ComicPresenter
    private lateinit var binding: ActivityComicBinding
    private lateinit var pb: ProgressBar
    private val handler = Handler()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityComicBinding.inflate(layoutInflater)
        setContentView(binding.root)
        presenter = ComicPresenter(this)
        pb = binding.progressBarComic
        val comicId = intent.getIntExtra(COMIC_ID, -1)
        Log.d("ComicActivity", "Comic ID recebido: $comicId")
        if (comicId == -1) {
        } else {
            presenter.showComicInfo(comicId)

        }
        handler.postDelayed({
            binding.linearLayout.visibility = View.VISIBLE
        }, 1000)


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
        pb.visibility = View.GONE
    }

    fun onFailure(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    fun showComicInfo(comic: Comic?) {
        binding.apply {
            comic?.run {
                comicTitle.text = title
                comicDescription.text = description ?: "No description found"
                comicPageCount.text = pageCount.toString()
                comicCreators.text =
                    creators?.items?.joinToString(", ") { it.name } ?: "No creators listed"
                comicFormat.text = format
                comicISBN.text = isbn.takeIf { it.isNotEmpty() } ?: "No ISBN found"
                comicUpc.text = upc.takeIf { it.isNotEmpty() } ?: "No UPC found"
                comicDate.text = formatDates(dates)
                comicPrice.text = formatPrices(prices)

                val imageUrl = "${thumbnail?.path}/portrait_incredible.${thumbnail?.extension}"
                val requestOptions = RequestOptions().transform(RoundedCorners(16))

                Glide.with(this@ComicActivity)
                    .load(imageUrl)
                    .apply(requestOptions)
                    .into(binding.comicCover)
            }
        }
    }


    fun formatDates(dates: List<Date>?): String {
        return dates?.joinToString("\n") { "${it.date}" } ?: "No dates available"
    }

    fun formatPrices(prices: List<Price>?): String {
        return prices?.joinToString("\n") { "$${it.price}" } ?: "No prices available"
    }


}