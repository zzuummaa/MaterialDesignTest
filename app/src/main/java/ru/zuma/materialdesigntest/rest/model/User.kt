package ru.zuma.materialdesigntest.rest.model

/**
 * Created by Stephan on 11.06.2018.
 */
data class User (
        var name: String? = null,
        var passwd: String? = null,
        var email: String? = null,
        var usertype: String? = null
)
