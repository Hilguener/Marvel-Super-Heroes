package com.hilguener.marvelsuperheroes.datasource

import com.hilguener.superheroesapp.datasource.callback.CharactersCallBack
import com.hilguener.superheroesapp.model.character.CharactersDTO
import com.hilguener.superheroesapp.model.character.Character
import com.hilguener.superheroesapp.ws.MarvelAPI
import com.hilguener.superheroesapp.ws.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AllCharactersRemoteDataSource {
    fun getCharacters(callback: CharactersCallBack) {
        val service = RetrofitClient.retrofit().create(MarvelAPI::class.java)
        val charactersCall = service.getAllCharacters()

        charactersCall.enqueue(object : Callback<CharactersDTO<Character>> {
            override fun onResponse(
                call: Call<CharactersDTO<Character>>,
                response: Response<CharactersDTO<Character>>
            ) {
                if (response.isSuccessful) {
                    val marvelResponse = response.body()
                    val results = marvelResponse?.data?.results

                    results?.let {
                        callback.onSuccess(it)
                    } ?: callback.onError("Lista de personagens vazia")
                } else {
                    val error = response.errorBody()?.toString()
                    callback.onError(error ?: "Erro na chamada de personagens")
                }
                callback.onComplete()
            }

            override fun onFailure(call: Call<CharactersDTO<Character>>, t: Throwable) {
                callback.onError(t.message ?: "Erro interno ao buscar personagens")
                callback.onComplete()
            }
        })
    }
}

