package com.luismunyoz.domain.browser.model

data class User(
    val firstName: String,
    val lastName: String,
    val rootItem: Item
)