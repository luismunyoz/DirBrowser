package com.luismunyoz.dirbrowser.app.browser.list.model

import com.luismunyoz.domain.browser.model.Item
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

sealed class ItemUIItem(
    val id: String,
    val name: String,
    val modified: String
) {

    class Folder(
        id: String,
        name: String,
        modified: String
    ) : ItemUIItem(id, name, modified)

    class Image(
        id: String,
        name: String,
        modified: String
    ) : ItemUIItem(id, name, modified) {

        fun getUrl(baseUrl: String) =
            "$baseUrl/items/$id/data"
    }

    class Other(
        id: String,
        name: String,
        modified: String
    ) : ItemUIItem(id, name, modified)
}

fun Item.toUIItem(): ItemUIItem =
    when (this) {
        is Item.Folder -> ItemUIItem.Folder(
            id = id,
            name = name,
            modified = modificationDate.toUI()
        )
        is Item.Image -> ItemUIItem.Image(
            id = id,
            name = name,
            modified = modificationDate.toUI()
        )
        is Item.Other -> ItemUIItem.Other(
            id = id,
            name = name,
            modified = modificationDate.toUI()
        )
    }

fun ZonedDateTime.toUI(): String {
    val formatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM)
    return format(formatter)
}