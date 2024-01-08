package com.hilguener.marvelsuperheroes.presenter

import com.hilguener.superheroesapp.datasource.AllStoriesRemoteDataSource
import com.hilguener.superheroesapp.datasource.callback.StoriesCallback
import com.hilguener.superheroesapp.model.stories.Story
import com.hilguener.marvelsuperheroes.ui.fragment.StoriesFragment

class AllStoriesPresenter(
    val view: StoriesFragment,
    val dataSource: AllStoriesRemoteDataSource = AllStoriesRemoteDataSource()
) :
    StoriesCallback {


    private var stories: List<Story>? = null

    fun loadStories() {
        view.showProgress()
        dataSource.getStories(this)
    }

    override fun onError(message: String) {
        view.onFailure(message)
        view.hideProgressBar()
    }

    override fun onSuccess(response: List<Story>) {
        // Extracting only the characters from the response
        val stories = response.filter { it is Story }

        this@AllStoriesPresenter.stories = stories
        view.showStories(stories)
        view.hideProgressBar()
    }

    override fun onComplete() {
        view.hideProgressBar()
    }
}