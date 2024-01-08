package com.hilguener.superheroesapp.datasource.callback

import com.hilguener.superheroesapp.model.character.Character

interface CharactersCallBack {
    fun onError(message: String)
    fun onSuccess(characters: List<Character>) // Atualizado para retornar também a lista de quadrinhos
    fun onComplete()
}
