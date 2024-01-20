package com.hilguener.marvelsuperheroes.datasource

import com.hilguener.superheroesapp.datasource.callback.ComicsCallback
import com.hilguener.superheroesapp.model.comics.Comic
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

        call.enqueue(object : Callback<ComicsDTO<Comic>> {
            override fun onResponse(
                call: Call<ComicsDTO<Comic>>,
                response: Response<ComicsDTO<Comic>>
            ) {
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

            override fun onFailure(call: Call<ComicsDTO<Comic>>, t: Throwable) {
                callback.onError(t.message ?: "Erro interno")
                callback.onComplete()
            }
        })
    }


    fun searchComics(query: String, callback: ComicsCallback) {
        val service = RetrofitClient.retrofit().create(MarvelAPI::class.java)
        val comicsCall = service.searchComics(query)

        comicsCall.enqueue(object : Callback<ComicsDTO<Comic>> {
            override fun onResponse(
                call: Call<ComicsDTO<Comic>>,
                response: Response<ComicsDTO<Comic>>
            ) {
                if (response.isSuccessful) {
                    // A resposta da API foi bem-sucedida
                    val marvelResponse = response.body()
                    val results = marvelResponse?.data?.results
                    results?.let {
                        callback.onSuccess(it)
                    }
                } else {
                    // A resposta da API não foi bem-sucedida
                    val error = response.errorBody()?.toString()
                    // Lide com o erro conforme necessário
                }
            }

            override fun onFailure(call: Call<ComicsDTO<Comic>>, t: Throwable) {
                // Ocorreu uma falha na chamada à API
                // Lide com a falha conforme necessário
            }
        })
    }


}

