package ru.zuma.materialdesigntest.rest.model

import android.graphics.drawable.Drawable
import com.google.gson.annotations.SerializedName

/**
 * Created by Stephan on 26.06.2018.
 */

class Product {
    var id: Long? = 0

    var category: String? = null

    var name: String? = null

    var price: Float? = 0f

    var stars: Float? = 0f

    @SerializedName("is_liked")
    var isLiked: Boolean? = null

    @SerializedName("icon_id")
    var iconId: Long? = 0

    @Transient
    var icon: Drawable? = null

    constructor() {}

    constructor(id: Long?, category: String?, name: String?, price: Float?, stars: Float?, isLiked: Boolean?, iconId: Long?) {
        this.id = id
        this.category = category
        this.name = name
        this.price = price
        this.stars = stars
        this.isLiked = isLiked
        this.iconId = iconId
    }

    override fun toString(): String {
        return "Product(id=$id, category=$category, name=$name, price=$price, stars=$stars, isLiked=$isLiked, iconId=$iconId)"
    }
}

