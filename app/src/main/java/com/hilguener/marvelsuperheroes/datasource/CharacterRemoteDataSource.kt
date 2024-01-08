package com.hilguener.superheroesapp.datasource

import android.util.Log
import com.hilguener.superheroesapp.datasource.callback.ComicsCallback
import com.hilguener.superheroesapp.model.character.CharactersDTO
import com.hilguener.superheroesapp.model.character.Character
import com.hilguener.superheroesapp.model.comics.Comic
import com.hilguener.superheroesapp.model.comics.ComicsDTO
import com.hilguener.superheroesapp.ws.MarvelAPI
import com.hilguener.superheroesapp.ws.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CharacterRemoteDataSource {

    fun findCharacterById(characterId: Int, callback: ComicsCallback) {
        RetrofitClient.retrofit()
            .create(MarvelAPI::class.java)
            .getCharacterById(characterId)
            .enqueue(object : Callback<CharactersDTO<Character>> {
                override fun onResponse(
                    call: Call<CharactersDTO<Character>>,
                    response: Response<CharactersDTO<Character>>
                ) {
                    if (response.isSuccessful) {
                        val charactersDTO: CharactersDTO<Character>? = response.body()
                        val character: Character? = charactersDTO?.data?.results?.firstOrNull()

                        if (character != null) {
                            // Recuperar comics do personagem
                            getCharacterComicsById(characterId) { comics ->
                                callback.onComicsLoaded(character, comics)
                            }
                        } else {
                            callback.onError("Personagem não encontrado")
                        }
                    } else {
                        val error = response.errorBody()?.toString()
                        callback.onError(error ?: "Erro na chamada")
                    }
                    callback.onComplete()
                }

                override fun onFailure(call: Call<CharactersDTO<Character>>, t: Throwable) {
                    callback.onError(t.message ?: "Erro interno")
                    callback.onComplete()
                }
            })
    }

    private fun getCharacterComicsById(characterId: Int, callback: (List<Comic>?) -> Unit) {
        RetrofitClient.retrofit()
            .create(MarvelAPI::class.java)
            .getCharacterComicsById(characterId)
            .enqueue(object : Callback<ComicsDTO> {

                override fun onResponse(call: Call<ComicsDTO>, response: Response<ComicsDTO>) {
                    if (response.isSuccessful) {
                        val comicsDTO: ComicsDTO? = response.body()
                        val comics: List<Comic>? = comicsDTO?.data?.results
                        callback(comics) // Ajuste para passar 'comics' para o callback
                    } else {
                        // Tratar erro na obtenção dos comics
                        val error = response.errorBody()?.toString()
                        Log.e("CharacterDataSource", "Erro ao obter comics: $error")
                        callback(null)
                    }
                }

                override fun onFailure(call: Call<ComicsDTO>, t: Throwable) {
                    Log.e("CharacterDataSource", "Falha ao obter comics: ${t.message}")
                    callback(null)
                }
            })
    }

}







