package com.luismunyoz.domain.browser.model

import java.time.ZonedDateTime

sealed class Item(
    val id: String,
    val parentId: String,
    val name: String,
    val modificationDate: ZonedDateTime
) {

    class Folder(
        id: String,
        parentId: String,
        name: String,
        modificationDate: ZonedDateTime
    ): Item(id, parentId, name, modificationDate)

    class Image(
        id: String,
        parentId: String,
        name: String,
        modificationDate: ZonedDateTime,
        val size: Int
    ): Item(id, parentId, name, modificationDate)

    class Other(
        id: String,
        parentId: String,
        name: String,
        modificationDate: ZonedDateTime
    ): Item(id, parentId, name, modificationDate)

}
