package ru.zuma.materialdesigntest.rest

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*


/**
 * Created by Stephan on 11.06.2018.
 */

interface AuthAPI {

    @POST("auth/login")
    fun login(@Body user: User): Call<Void>

    @FormUrlEncoded
    @POST("auth/register")
    fun register(@Body user: User): Call<Void>
}