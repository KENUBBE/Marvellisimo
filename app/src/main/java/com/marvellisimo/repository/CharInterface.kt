package com.marvellisimo.repository

import com.marvellisimo.character.CharacterDTO
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Url

interface CharInterface {
    @GET
    fun getCharacter(@Url url: String): Single<CharacterDTO>
}