package com.hilguener.marvelsuperheroes.datasource

import com.hilguener.marvelsuperheroes.datasource.callback.SeriesCallback
import com.hilguener.marvelsuperheroes.model.series.SeriesDTO
import com.hilguener.superheroesapp.ws.MarvelAPI
import com.hilguener.superheroesapp.ws.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AllSeriesRemoteDatasource {
    fun getAllseries(callback: SeriesCallback) {
        val service = RetrofitClient.retrofit().create(MarvelAPI::class.java)
        val seriesCall = service.getAllSeries()

        seriesCall.enqueue(object : Callback<SeriesDTO> {
            override fun onResponse(
                call: Call<SeriesDTO>,
                response: Response<SeriesDTO>
            ) {
                if (response.isSuccessful) {
                    val seriesDataWrapper = response.body()
                    val series = seriesDataWrapper?.data?.results
                    series?.let {
                        callback.onSuccess(it)
                    } ?: callback.onError("Lista de séries vazia")
                } else {
                    val error = response.errorBody()?.toString()
                    callback.onError(error ?: "Erro na chamada de séries")
                }
                callback.onComplete()
            }

            override fun onFailure(call: Call<SeriesDTO>, t: Throwable) {
                callback.onError(t.message ?: "Erro interno ao buscar séries")
                callback.onComplete()
            }
        })
    }

}