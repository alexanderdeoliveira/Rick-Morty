package com.alexander.rickmorty.model.db

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.alexander.rickmorty.model.viewobject.CharacterViewObject
import kotlinx.parcelize.Parcelize

@Entity(tableName = "characters")
@Parcelize
data class Character(
        @PrimaryKey
        val id: Int,
        val name: String,
        val status: String,
        val species: String,
        val type: String,
        val gender: String,
        val origin: String,
        val location: String,
        val image: String,
        val url: String,
        val created: String
) : Parcelable

fun Character.toCharacterViewObject(): CharacterViewObject {
    return CharacterViewObject(
            id = this.id,
            name = this.name,
            status = this.status,
            species = this.species,
            type = this.type,
            gender = this.gender,
            origin = this.origin,
            location = this.location,
            image = this.image,
            url = this.url,
            created = this.created
    )
}
