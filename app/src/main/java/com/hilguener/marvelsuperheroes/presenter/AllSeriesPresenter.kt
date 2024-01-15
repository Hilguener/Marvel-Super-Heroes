package com.hilguener.marvelsuperheroes.presenter

import com.hilguener.marvelsuperheroes.datasource.AllSeriesRemoteDatasource
import com.hilguener.marvelsuperheroes.datasource.callback.BaseView
import com.hilguener.marvelsuperheroes.datasource.callback.SeriesCallback
import com.hilguener.marvelsuperheroes.model.series.Serie

class AllSeriesPresenter(
    val view: BaseView,
    val dataSource: AllSeriesRemoteDatasource = AllSeriesRemoteDatasource()
) : SeriesCallback {

    private var seriesList: List<Serie>? = null

    fun loadSeries() {
        view.showProgress()
        dataSource.getAllseries(this)
    }

    override fun onError(message: String) {
        view.onFailure(message)
        view.hideProgressBar()
    }

    override fun onSuccess(response: List<Serie>) {
        this@AllSeriesPresenter.seriesList = response
        view?.showSeries(response)  // Use 'view?.' for null-safety
        view?.hideProgressBar()
    }


    override fun onComplete() {
        view.hideProgressBar()
    }
}
