package com.hilguener.marvelsuperheroes.datasource.callback

import com.hilguener.marvelsuperheroes.model.series.Serie
import com.hilguener.superheroesapp.model.stories.Story

interface SeriesCallback {
    fun onComplete()
    fun onSuccess(response: List<Serie>)
    fun onError(message: String)
}