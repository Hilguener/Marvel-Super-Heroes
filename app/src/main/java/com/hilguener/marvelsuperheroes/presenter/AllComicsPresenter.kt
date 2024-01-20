package com.hilguener.marvelsuperheroes.presenter

import com.hilguener.marvelsuperheroes.datasource.AllComicsRemoteDataSource
import com.hilguener.superheroesapp.datasource.callback.ComicsCallback
import com.hilguener.superheroesapp.model.character.Character
import com.hilguener.superheroesapp.model.comics.Comic
import com.hilguener.marvelsuperheroes.ui.activity.ComicsActivity


class AllComicsPresenter(
    val view: ComicsActivity,
    val dataSource: AllComicsRemoteDataSource = AllComicsRemoteDataSource()
) : ComicsCallback {

    private var comics: List<Comic>? = null

    fun loadComics() {
        view.showProgress()
        dataSource.findAllComics(this)
    }

    fun searchComics(query: String) {
        // Chamar o método de pesquisa adequado na fonte de dados remota
        view.showProgress()
        dataSource.searchComics(query, this)
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

    override fun onSuccess(comics: List<Comic>) {
        val comics = comics.filterIsInstance<Comic>()

        this@AllComicsPresenter.comics = comics
        view.showComics(comics)
        view.hideProgressBar()
    }


    override fun onComplete() {
        view.hideProgressBar()
    }
}