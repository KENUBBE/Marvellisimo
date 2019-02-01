package com.marvellisimo.repository

import com.marvellisimo.dto.character.Character
import com.marvellisimo.dto.character.Response
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url

interface Data {

    @GET
    fun getCharacterData(@Url url: String): Single<Response>

    @GET
    fun getSerieData(@Url url: String): Single<com.marvellisimo.dto.series.Response>

    @GET
    fun getCharacterByStartWith(@Url url: String): Single<Response>

}