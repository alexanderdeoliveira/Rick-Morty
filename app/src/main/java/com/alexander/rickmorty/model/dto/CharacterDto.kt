package com.alexander.rickmorty.model.dto

import android.os.Parcelable
import com.alexander.rickmorty.model.db.Character
import kotlinx.parcelize.Parcelize

@Parcelize
data class CharacterDto(
        val id: Int,
        val name: String,
        val status: String,
        val species: String,
        val type: String,
        val gender: String,
        val image: String,
        val episode: List<String>?,
        val url: String,
        val created: String
) : Parcelable

fun CharacterDto.toCharacter(): Character {
    return Character(
            id = this.id,
            name = this.name,
            status = this.status,
            species = this.species,
            type = this.type,
            gender = this.gender,
            origin = "",
            location = "",
            image = this.image,
            url = this.url,
            created = this.created
    )
}
