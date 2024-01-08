package com.hilguener.superheroesapp.datasource

import com.hilguener.superheroesapp.datasource.callback.StoriesCallback
import com.hilguener.superheroesapp.model.stories.Stories
import com.hilguener.superheroesapp.ws.MarvelAPI
import com.hilguener.superheroesapp.ws.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AllStoriesRemoteDataSource {

    fun getStories(callback: StoriesCallback) {
        val service = RetrofitClient.retrofit().create(MarvelAPI::class.java)
        val storiesCall = service.getAllStories()

        storiesCall.enqueue(object : Callback<Stories> {
            override fun onResponse(
                call: Call<Stories>,
                response: Response<Stories>
            ) {
                if (response.isSuccessful) {
                    val marvelResponse = response.body()
                    val results = marvelResponse?.data?.results

                    results?.let {
                        callback.onSuccess(it)
                    } ?: callback.onError("Lista de histórias vazia")
                } else {
                    val error = response.errorBody()?.toString()
                    callback.onError(error ?: "Erro na chamada de personagens")
                }
                callback.onComplete()
            }

            override fun onFailure(call: Call<Stories>, t: Throwable) {
                callback.onError(t.message ?: "Erro interno ao buscar personagens")
                callback.onComplete()
            }
        })
    }
}