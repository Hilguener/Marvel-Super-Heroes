package com.hilguener.superheroesapp.datasource

import com.hilguener.superheroesapp.model.character.Character
import com.hilguener.superheroesapp.model.comics.Comic

sealed class CategoryItem {

    abstract fun getId(): Int
    data class CharacterItem(val character: Character) : CategoryItem() {
        override fun getId(): Int = character.id
    }

    data class ComicItem(val comic: Comic) : CategoryItem() {
        override fun getId(): Int = comic.id
    }
}