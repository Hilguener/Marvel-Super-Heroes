package com.hilguener.superheroesapp.datasource

import com.hilguener.superheroesapp.datasource.callback.ComicCallback
import com.hilguener.superheroesapp.model.comics.Comic
import com.hilguener.superheroesapp.model.comics.ComicsDTO
import com.hilguener.superheroesapp.ws.MarvelAPI
import com.hilguener.superheroesapp.ws.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ComicRemoteDataSource {

    fun findComicsById(comicId: Int, callback: ComicCallback) {
        RetrofitClient.retrofit()
            .create(MarvelAPI::class.java)
            .getComicsById(comicId)
            .enqueue(object : Callback<ComicsDTO> {
                override fun onResponse(call: Call<ComicsDTO>, response: Response<ComicsDTO>) {
                    if (response.isSuccessful) {
                        val comicsDTO: ComicsDTO? = response.body()

                        // Faça a manipulação dos dados recebidos conforme necessário
                        // Por exemplo, se você tem um objeto Comic dentro do ComicsDTO:
                        val comic: Comic? = comicsDTO?.data?.results?.firstOrNull()

                        if (comic != null) {
                            // Você pode realizar operações com os dados do comic recebido
                            callback.onSuccess(comic)
                        } else {
                            callback.onError("Comic não encontrado")
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