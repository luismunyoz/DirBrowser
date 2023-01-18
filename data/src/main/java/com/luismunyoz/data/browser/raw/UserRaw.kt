package com.luismunyoz.data.browser.raw

import com.google.gson.annotations.SerializedName
import com.luismunyoz.domain.browser.model.User

data class UserRaw(
    @SerializedName("firstName") val firstName: String,
    @SerializedName("lastName") val lastName: String,
    @SerializedName("rootItem") val rootItem: ItemRaw
) {

    fun toDomain() = User(
        firstName = firstName,
        lastName = lastName,
        rootItem = rootItem.toDomain()
    )
}