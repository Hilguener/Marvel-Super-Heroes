package com.hilguener.marvelsuperheroes.presenter

import com.hilguener.marvelsuperheroes.ui.activity.EventsActivity
import com.hilguener.superheroesapp.datasource.AllEventsRemoteDataSource
import com.hilguener.superheroesapp.datasource.callback.EventsCallback
import com.hilguener.superheroesapp.model.events.Event


class AllEventsPresenter(
    val view: EventsActivity,
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