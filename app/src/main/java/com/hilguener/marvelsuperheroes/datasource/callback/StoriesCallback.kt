package com.hilguener.superheroesapp.datasource.callback

import com.hilguener.superheroesapp.model.stories.Story

interface StoriesCallback {
    fun onComplete()
    fun onSuccess(response: List<Story>)
    fun onError(message: String)
}