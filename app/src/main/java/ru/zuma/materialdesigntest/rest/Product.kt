package ru.zuma.materialdesigntest.rest

/**
 * Created by Stephan on 26.06.2018.
 */

data class Product (
        var id: Long? = null,
        var category: String? = null,
        var name: String? = null,
        var price: Float? = null,
        var stars: Float? = null,
        var isLiked: Boolean? = null
)