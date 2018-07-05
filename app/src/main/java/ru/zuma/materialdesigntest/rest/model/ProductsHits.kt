package ru.zuma.materialdesigntest.rest.model


class ProductHits {

    var products: List<Product>? = null

    constructor() {}

    constructor(products: List<Product>) {
        this.products = products
    }

}