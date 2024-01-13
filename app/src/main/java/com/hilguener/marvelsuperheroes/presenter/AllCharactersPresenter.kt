package com.hilguener.marvelsuperheroes.presenter

import com.hilguener.marvelsuperheroes.datasource.AllCharactersRemoteDataSource
import com.hilguener.marvelsuperheroes.datasource.callback.BaseView
import com.hilguener.superheroesapp.datasource.callback.CharactersCallBack
import com.hilguener.superheroesapp.model.character.Character

class AllCharactersPresenter(
    val view: BaseView,
    val dataSource: AllCharactersRemoteDataSource = AllCharactersRemoteDataSource()
) : CharactersCallBack {

    private var characters: List<Character>? = null

    fun loadCharacters() {
        view.showProgress()
        dataSource.getCharacters(this)
    }

    override fun onError(message: String) {
        view.onFailure(message)
        view.hideProgressBar()
    }

    override fun onSuccess(response: List<Character>) {
        // Extracting only the characters from the response
        val characters = response.filter { it is Character }

        this@AllCharactersPresenter.characters = characters
        view.showCharacters(characters)
        view.hideProgressBar()
    }

    override fun onComplete() {
        view.hideProgressBar()
    }
}

