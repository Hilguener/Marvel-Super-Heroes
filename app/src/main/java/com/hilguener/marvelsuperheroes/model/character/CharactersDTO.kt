package com.hilguener.superheroesapp.model.character

import com.google.gson.annotations.SerializedName

data class CharactersDTO<T>(
    val attributionHTML: String,
    val attributionText: String,
    val code: Int,
    val copyright: String,
    @SerializedName("data")
    val data: Data,
    val etag: String,
    val status: String
)























