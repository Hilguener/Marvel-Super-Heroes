package com.hilguener.marvelsuperheroes.datasource

import com.hilguener.superheroesapp.datasource.callback.ComicsCallback

import com.hilguener.superheroesapp.model.comics.ComicsDTO
import com.hilguener.superheroesapp.ws.MarvelAPI
import com.hilguener.superheroesapp.ws.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AllComicsRemoteDataSource {

    fun findAllComics(callback: ComicsCallback) {
        val service = RetrofitClient.retrofit().create(MarvelAPI::class.java)
        val call = service.getComics()

        call.enqueue(object : Callback<ComicsDTO> {
            override fun onResponse(call: Call<ComicsDTO>, response: Response<ComicsDTO>) {
                if (response.isSuccessful) {
                    val comicsDTO = response.body()
                    val comicsList = comicsDTO?.data?.results

                    comicsList?.let {
                        callback.onComicsLoaded(null, comicsList)
                    } ?: run {
                        callback.onError("Lista de quadrinhos vazia")
                    }
                } else {
                    val error = response.errorBody()?.toString()
                    callback.onError(error ?: "Erro na chamada")
                }
                callback.onComplete()
            }

            override fun onFailure(call: Call<ComicsDTO>, t: Throwable) {
                callback.onError(t.message ?: "Erro interno")
                callback.onComplete()
            }
        })
    }


}

