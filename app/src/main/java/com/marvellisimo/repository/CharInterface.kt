package com.marvellisimo.repository

import com.marvellisimo.dto.Character
import com.marvellisimo.dto.Response
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Url

interface CharInterface {
    @GET
    fun getCharacter(@Url url: String): Single<Character>

    @GET
    fun getCharacterData(@Url url: String): Single<Response>
}