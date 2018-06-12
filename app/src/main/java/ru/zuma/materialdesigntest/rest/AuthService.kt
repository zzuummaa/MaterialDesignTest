package ru.zuma.materialdesigntest.rest

import android.os.Build
import android.support.annotation.RequiresApi
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Created by Stephan on 12.06.2018.
 */

typealias OnAuthSuccess = (String) -> Unit
typealias OnAuthFailure = (String?, Throwable?) -> Unit

class AuthService() {
    companion object {
        fun loginCommi(user: User, onAuthSuccess: OnAuthSuccess? = null, onAuthFailure: OnAuthFailure? = null) {
            CommiApi.instance.login(user).enqueue(object: Callback<Void> {
                override fun onFailure(call: Call<Void>?, t: Throwable?) {
                    onAuthFailure?.invoke(null, t)
                }

                @RequiresApi(Build.VERSION_CODES.N)
                override fun onResponse(call: Call<Void>?, response: Response<Void>?) {
                    var toastStr = ""

                    if (response!!.code() == 200) {

                        var commiCookie = response.raw().headers("Set-Cookie")
                                .stream()
                                .findAny()
                                .filter { it.startsWith("commi") }
                                .ifPresent { onAuthSuccess?.invoke(it) }

                        return
                    } else if(response!!.code() == 400) {
                        toastStr = "Авторизация не удалась"
                    } else {
                        toastStr = "Ошибка сервера"
                    }

                    onAuthFailure?.invoke(toastStr, null)
                }

            })
        }

        fun registerCommi(user: User, onAuthSuccess: OnAuthSuccess? = null, onAuthFailure: OnAuthFailure? = null) {
            CommiApi.instance.register(user).enqueue(object: Callback<Void> {
                override fun onFailure(call: Call<Void>?, t: Throwable?) {
                    onAuthFailure?.invoke(null, t)
                }

                @RequiresApi(Build.VERSION_CODES.N)
                override fun onResponse(call: Call<Void>?, response: Response<Void>?) {
                    var toastStr = ""

                    if (response!!.code() == 200) {

                        var commiCookie = response.raw().headers("Set-Cookie")
                                .stream()
                                .findAny()
                                .filter { it.startsWith("commi") }
                                .ifPresent { onAuthSuccess?.invoke(it) }

                        return
                    } else if(response!!.code() == 400) {
                        toastStr = "Регистрация не удалась"
                    } else {
                        toastStr = "Ошибка сервера"
                    }

                    onAuthFailure?.invoke(toastStr, null)
                }

            })
        }
    }
}