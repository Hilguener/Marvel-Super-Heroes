package com.hilguener.superheroesapp.ws

import com.hilguener.superheroesapp.model.character.CharactersDTO
import com.hilguener.superheroesapp.model.character.Character
import com.hilguener.superheroesapp.model.comics.ComicsDTO
import com.hilguener.superheroesapp.model.events.EventDataWrapper
import com.hilguener.superheroesapp.model.stories.Stories
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import java.math.BigInteger
import java.security.MessageDigest

interface MarvelAPI {

    companion object {
        const val API_KEY = "0700c4d72a0a2ee5189575e206a764ce"
        private const val PRIVATE_KEY = "87e30ab98a2a268b2d22e0807b758fcb9b788475"

        fun getCurrentTimeStamp(): String = System.currentTimeMillis().toString()

        fun generateHash(timeStamp: String): String {
            val input = "$timeStamp$PRIVATE_KEY$API_KEY"
            val md = MessageDigest.getInstance("MD5")
            return BigInteger(1, md.digest(input.toByteArray())).toString(16).padStart(32, '0')
        }
    }

    @GET("/v1/public/characters")
    fun getAllCharacters(
        @Query("apikey") apiKey: String = API_KEY,
        @Query("ts") ts: String = getCurrentTimeStamp(),
        @Query("hash") hash: String = generateHash(getCurrentTimeStamp()),
        @Query("offset") offset: Int = 100,
        @Query("limit") limit: Int = 100
    ): Call<CharactersDTO<Character>>

    @GET("/v1/public/characters/{characterId}")
    fun getCharacterById(
        @Path("characterId") characterId: Int,
        @Query("apikey") apiKey: String = API_KEY,
        @Query("ts") ts: String = getCurrentTimeStamp(),
        @Query("hash") hash: String = generateHash(getCurrentTimeStamp())
    ): Call<CharactersDTO<Character>>

    @GET("/v1/public/characters/{characterId}/comics")
    fun getCharacterComicsById(
        @Path("characterId") characterId: Int,
        @Query("apikey") apiKey: String = API_KEY,
        @Query("ts") ts: String = getCurrentTimeStamp(),
        @Query("hash") hash: String = generateHash(getCurrentTimeStamp())
    ): Call<ComicsDTO>

    @GET("/v1/public/comics")
    fun getComics(
        @Query("apikey") apiKey: String = API_KEY,
        @Query("ts") ts: String = getCurrentTimeStamp(),
        @Query("hash") hash: String = generateHash(getCurrentTimeStamp()),
        @Query("offset") offset: Int = 100,
        @Query("limit") limit: Int = 100
    ): Call<ComicsDTO>

    @GET("/v1/public/comics/{comicId}")
    fun getComicsById(
        @Path("comicId") comicId: Int,
        @Query("apikey") apiKey: String = API_KEY,
        @Query("ts") ts: String = getCurrentTimeStamp(),
        @Query("hash") hash: String = generateHash(getCurrentTimeStamp()),
    ): Call<ComicsDTO>

    @GET("/v1/public/events")
    fun getAllEvents(
        @Query("apikey") apiKey: String = API_KEY,
        @Query("ts") ts: String = getCurrentTimeStamp(),
        @Query("hash") hash: String = generateHash(getCurrentTimeStamp()),
        @Query("offset") offset: Int = 0,
        @Query("limit") limit: Int = 100
    ): Call<EventDataWrapper>


    @GET("/v1/public/stories")
    fun getAllStories(
        @Query("apikey") apiKey: String = API_KEY,
        @Query("ts") ts: String = getCurrentTimeStamp(),
        @Query("hash") hash: String = generateHash(getCurrentTimeStamp()),
        @Query("offset") offset: Int = 0,
        @Query("limit") limit: Int = 100
    ): Call<Stories>




}
