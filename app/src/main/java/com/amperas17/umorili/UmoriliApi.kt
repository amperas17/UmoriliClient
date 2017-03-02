package com.amperas17.umorili

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface UmoriliApi {
    @GET("api/get")
    fun getData(@Query("name") resourceName: String, @Query("num") count: Int): Call<List<PostModel>>
}