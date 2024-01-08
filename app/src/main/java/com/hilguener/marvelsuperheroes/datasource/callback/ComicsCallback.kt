package com.hilguener.superheroesapp.datasource.callback

import com.hilguener.superheroesapp.model.character.Character
import com.hilguener.superheroesapp.model.comics.Comic

interface ComicsCallback {
    fun onComicsLoaded(character: Character?, comics: List<Comic>?)
    fun onError(error: String)
    fun onComplete()
}