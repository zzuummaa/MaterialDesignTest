package ru.zuma.materialdesigntest

/**
 * Created by Stephan on 11.06.2018.
 */

fun isEmailValid(email: String): Boolean {
    //TODO: Replace this with your own logic
    return email.contains("@")
}

fun isPasswordValid(password: String): Boolean {
    //TODO: Replace this with your own logic
    return password.length > 4
}