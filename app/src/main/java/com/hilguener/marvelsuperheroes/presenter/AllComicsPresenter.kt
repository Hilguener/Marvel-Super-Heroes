package com.hilguener.marvelsuperheroes.presenter

import com.hilguener.marvelsuperheroes.datasource.AllComicsRemoteDataSource
import com.hilguener.superheroesapp.datasource.callback.ComicsCallback
import com.hilguener.superheroesapp.model.character.Character
import com.hilguener.superheroesapp.model.comics.Comic
import com.hilguener.superheroesapp.ui.fragment.ComicsFragment

class AllComicsPresenter(
    val view: ComicsFragment,
    val dataSource: AllComicsRemoteDataSource = AllComicsRemoteDataSource()
) : ComicsCallback {

    private var comics: List<Comic>? = null

    fun loadComics() {
        view.showProgress()
        dataSource.findAllComics(this)
    }

    override fun onError(message: String) {
        view.onFailure(message)
        view.hideProgressBar()
    }

    override fun onComicsLoaded(character: Character?, comics: List<Comic>?) {
        val comicsList = comics?.filterIsInstance<Comic>()

        comicsList?.let {
            this@AllComicsPresenter.comics = it
            view.showComics(it)
        }

        view.hideProgressBar()
    }


    override fun onComplete() {
        view.hideProgressBar()
    }
}