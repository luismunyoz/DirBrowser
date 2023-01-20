package com.luismunyoz.data.browser.raw

import com.google.gson.annotations.SerializedName
import com.luismunyoz.domain.browser.model.Item
import java.time.ZonedDateTime

data class ItemRaw(
    @SerializedName("id") val id: String,
    @SerializedName("parentId") val parentId: String,
    @SerializedName("name") val name: String,
    @SerializedName("isDir") val isDirectory: Boolean,
    @SerializedName("modificationDate") val modificationDate: String,
    @SerializedName("size") val size: Int?,
    @SerializedName("contentType") val contentType: String?
) {

    companion object {
        private const val IMAGE_CONTENT_TYPE_JPEG = "image/jpeg"
        private const val IMAGE_CONTENT_TYPE_PNG = "image/png"
    }

    private val isImage
        get() = contentType in listOf(
            IMAGE_CONTENT_TYPE_JPEG,
            IMAGE_CONTENT_TYPE_PNG
        ) && !isDirectory && size != null

    private val isFolder
        get() = isDirectory

    fun toDomain(): Item =
        when {
            isImage -> Item.Image(
                id = id,
                parentId = parentId,
                name = name,
                modificationDate = ZonedDateTime.parse(modificationDate),
                size = size ?: 0
            )
            isFolder -> Item.Folder(
                id = id,
                parentId = parentId,
                name = name,
                modificationDate = ZonedDateTime.parse(modificationDate),
            )
            else -> Item.Other(
                id = id,
                parentId = parentId,
                name = name,
                modificationDate = ZonedDateTime.parse(modificationDate),
            )
        }
}
