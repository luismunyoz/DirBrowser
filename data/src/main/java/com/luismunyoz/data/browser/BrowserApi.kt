package com.luismunyoz.data.browser

import com.luismunyoz.data.browser.raw.ItemRaw
import com.luismunyoz.data.browser.raw.UserRaw
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface BrowserApi {

    companion object {
        private const val ITEM_ID = "id"
    }

    @GET("/me")
    fun getUser(): Call<UserRaw>

    @GET("/items/{$ITEM_ID}")
    fun getItems(@Path(ITEM_ID) itemId: String): Call<List<ItemRaw>>
}