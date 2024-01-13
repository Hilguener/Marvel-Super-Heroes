package com.hilguener.marvelsuperheroes.datasource.callback

import com.hilguener.marvelsuperheroes.model.series.Serie
import com.hilguener.superheroesapp.model.character.Character

interface BaseView {
    fun showCharacters(characters: List<Character>)
    fun onFailure(message: String)
    fun showSeries(series: List<Serie>)
    fun showProgress()

    fun hideProgressBar()
}