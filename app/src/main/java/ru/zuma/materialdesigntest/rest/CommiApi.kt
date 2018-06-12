package ru.zuma.materialdesigntest.rest

import android.preference.Preference
import android.util.Log
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import okhttp3.OkHttpClient




/**
 * Created by Stephan on 11.06.2018.
 */
abstract class CommiApi {
    companion object {
        var client: OkHttpClient
        var retrofit: Retrofit

        init {
            client = OkHttpClient.Builder()
                    .build()

            retrofit = Retrofit.Builder()
                    //.baseUrl("https://commiapi.azurewebsites.net/")
                    .baseUrl("http://192.168.1.64:8080/")
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
        }

    }


}