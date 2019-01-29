package com.marvellisimo

import io.reactivex.Observable
import io.reactivex.Single
import retrofit2.http.GET

interface MarvelService {
    @GET("todos")
    fun getData(): Single<List<Todos>>
}