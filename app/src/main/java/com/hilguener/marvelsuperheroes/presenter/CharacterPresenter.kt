package com.hilguener.marvelsuperheroes.presenter

import com.hilguener.superheroesapp.datasource.CharacterRemoteDataSource
import com.hilguener.superheroesapp.datasource.callback.ComicsCallback
import com.hilguener.superheroesapp.model.character.Character
import com.hilguener.superheroesapp.model.comics.Comic
import com.hilguener.marvelsuperheroes.ui.activity.CharacterActivity

class CharacterPresenter(
    val view: CharacterActivity,
    val data: CharacterRemoteDataSource = CharacterRemoteDataSource()
) : ComicsCallback {

    fun showCharacterInfo(characterId: Int) {
        view.showProgress()
        data.findCharacterById(characterId, this)
    }

    override fun onComicsLoaded(character: Character?, comics: List<Comic>?) {
        if (comics != null) {
            view.showComics(comics)
        } else {
            // Trate a situação em que os quadrinhos não são encontrados
            view.onFailure("Quadrinhos não encontrados")
        }
        view.showCharacter(character)
    }

    override fun onSuccess(comics: List<Comic>) {

    }


    override fun onError(message: String) {
        view.onFailure(message)
    }


    override fun onComplete() {
        view.hideProgressBar()
    }
}