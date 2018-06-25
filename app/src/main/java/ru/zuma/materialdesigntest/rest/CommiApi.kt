package ru.zuma.materialdesigntest.rest

import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST


/**
 * Created by Stephan on 11.06.2018.
 */
interface CommiApi {
    companion object {
        var instance: CommiApi
        private var retrofit: Retrofit

        init {
            retrofit = Retrofit.Builder()
                    //.baseUrl("https://commiapi.azurewebsites.net/")
                    .baseUrl("http://commirest.azurewebsites.net/")
                    //.baseUrl("http://192.168.1.64:8080/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()

            instance = retrofit.create(CommiApi::class.java)
        }

    }

    @POST("auth/login")
    fun login(@Body user: User): Call<Void>

    @POST("auth/register")
    fun register(@Body user: User): Call<Void>
}