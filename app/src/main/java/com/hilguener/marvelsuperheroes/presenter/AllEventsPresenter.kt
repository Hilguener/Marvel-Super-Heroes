package com.hilguener.marvelsuperheroes.presenter

import com.hilguener.superheroesapp.datasource.AllEventsRemoteDataSource
import com.hilguener.superheroesapp.datasource.callback.EventsCallback
import com.hilguener.superheroesapp.model.events.Event
import com.hilguener.superheroesapp.ui.fragment.EventsFragment

class AllEventsPresenter(
    val view: EventsFragment,
    val dataSource: AllEventsRemoteDataSource = AllEventsRemoteDataSource()
) : EventsCallback {

    private var events: List<Event>? = null

    fun loadEvents() {
        view.showProgress()
        dataSource.getEvents(this)
    }

    override fun onError(errorMessage: String) {
        view.onFailure(errorMessage)
        view.hideProgressBar()
    }

    override fun onSuccess(events: List<Event>) {
        val events = events.filter { true }

        this@AllEventsPresenter.events = events
        view.showEvents(events)
        view.hideProgressBar()
    }


    override fun onComplete() {
        view.hideProgressBar()
    }
}