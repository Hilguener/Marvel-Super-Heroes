package com.hilguener.superheroesapp.datasource

import com.hilguener.superheroesapp.datasource.callback.EventsCallback
import com.hilguener.superheroesapp.model.events.EventDataWrapper
import com.hilguener.superheroesapp.ws.MarvelAPI
import com.hilguener.superheroesapp.ws.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AllEventsRemoteDataSource {
    fun getEvents(callback: EventsCallback) {
        val service = RetrofitClient.retrofit().create(MarvelAPI::class.java)
        val eventsCall = service.getAllEvents()

        eventsCall.enqueue(object : Callback<EventDataWrapper> {
            override fun onResponse(
                call: Call<EventDataWrapper>,
                response: Response<EventDataWrapper>
            ) {
                if (response.isSuccessful) {
                    val eventDataWrapper = response.body()
                    val events = eventDataWrapper?.data?.results
                    events?.let {
                        callback.onSuccess(it)
                    } ?: callback.onError("Lista de eventos vazia")
                } else {
                    val error = response.errorBody()?.toString()
                    callback.onError(error ?: "Erro na chamada de eventos")
                }
                callback.onComplete()
            }

            override fun onFailure(call: Call<EventDataWrapper>, t: Throwable) {
                callback.onError(t.message ?: "Erro interno ao buscar eventos")
                callback.onComplete()
            }
        })
    }
}
