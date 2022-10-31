package com.alexander.rickmorty

import com.alexander.rickmorty.model.dto.CharactersResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface CharacterApi {
    @GET("character")
    suspend fun getCharacters(
            @Query("page") page: Int? = null,
            @Query("name") name: String? = null,
            @Query("status") status: String? = null
    ): CharactersResponse
}
