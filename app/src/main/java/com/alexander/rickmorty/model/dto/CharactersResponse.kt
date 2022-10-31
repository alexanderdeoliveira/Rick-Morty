package com.alexander.rickmorty.model.dto

import android.os.Parcelable
import com.alexander.rickmorty.model.dto.CharacterDto
import kotlinx.parcelize.Parcelize

@Parcelize
data class CharactersResponse(
        val info: InfoResponse,
        val results: List<CharacterDto>
) : Parcelable
