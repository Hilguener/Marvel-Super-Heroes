package com.hilguener.superheroesapp.datasource.callback



import com.hilguener.superheroesapp.model.events.Event


interface EventsCallback {
    fun onSuccess(events: List<Event>)
    fun onError(errorMessage: String)
    fun onComplete()
}

