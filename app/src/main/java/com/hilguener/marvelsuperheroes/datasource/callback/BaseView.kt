package com.hilguener.marvelsuperheroes.datasource.callback

import com.hilguener.marvelsuperheroes.model.series.Serie
import com.hilguener.superheroesapp.model.character.Character

interface BaseView {

    fun showSeries(series: List<Serie>)

    fun showCharacters(characters: List<Character>)

    fun showProgress()

    fun hideProgressBar()

    fun onFailure(message: String)
}