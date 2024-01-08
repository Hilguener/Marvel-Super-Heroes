package com.hilguener.superheroesapp.datasource.callback

import com.hilguener.superheroesapp.model.comics.Comic

interface ComicCallback {
    fun onError(message: String)
    fun onSuccess(response: Comic)
    fun onComplete()
}