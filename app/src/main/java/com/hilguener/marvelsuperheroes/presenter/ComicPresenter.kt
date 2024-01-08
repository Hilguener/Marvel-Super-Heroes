package com.hilguener.marvelsuperheroes.presenter

import com.hilguener.superheroesapp.datasource.ComicRemoteDataSource
import com.hilguener.superheroesapp.datasource.callback.ComicCallback
import com.hilguener.superheroesapp.model.comics.Comic
import com.hilguener.marvelsuperheroes.ui.activity.ComicActivity

class ComicPresenter(
    val view: ComicActivity,
    val comicData: ComicRemoteDataSource = ComicRemoteDataSource()
): ComicCallback {

    fun showComicInfo(comicId: Int) {
        view.showProgress()
        comicData.findComicsById(comicId, this)
    }
    override fun onError(message: String) {
        view.onFailure(message)
    }

    override fun onSuccess(comic: Comic) {

        view.showComicInfo(comic)
    }

    override fun onComplete() {
        view.hideProgressBar()
    }


}