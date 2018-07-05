package ru.zuma.materialdesigntest.rest

import android.util.Log
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import ru.zuma.materialdesigntest.rest.model.ProductHits
import ru.zuma.materialdesigntest.rest.model.User

/**
 * Created by Stephan on 11.06.2018.
 */
interface CommiApi {
    companion object {
        var instance: CommiApi
        private var retrofit: Retrofit

        init {
            val interceptor = HttpLoggingInterceptor()
            interceptor.level = HttpLoggingInterceptor.Level.BODY
            val client = OkHttpClient.Builder().addInterceptor(interceptor).build()

            retrofit = Retrofit.Builder()
                    //.baseUrl("https://commiapi.azurewebsites.net/")
                    //.baseUrl("http://commirest.azurewebsites.net/")
                    .baseUrl("http://192.168.1.64:8080/")
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()

            instance = retrofit.create(CommiApi::class.java)
        }
    }

    @POST("auth/login")
    fun login(@Body user: User): Call<Void>

    @POST("auth/register")
    fun register(@Body user: User): Call<Void>

    @GET("products/hits")
    fun getProductsHits(@Query("category") category: String): Call<ProductHits>

    @GET("images/{id}.png")
    fun getImage(@Path("id") id: Long): Call<ResponseBody>
}

fun <T> Call<T>.unwrapCall(): T? {
    try {
        val resp = execute()

        if (resp.code() == 200) {
            return resp.body()
        } else {
            Log.w(javaClass.simpleName, "Request from ${request().url()} return code ${resp.code()}")
            return null
        }
    } catch (e: Throwable) {
        Log.e(javaClass.simpleName, e.message)
    }

    return null
}