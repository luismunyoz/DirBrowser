package com.luismunyoz.data.browser.memory_cache

import com.luismunyoz.domain.browser.model.Item
import javax.inject.Inject

class MemoryCache @Inject constructor() {

    private val cacheMap: MutableMap<String, List<Item>> = mutableMapOf()

    fun getItems(id: String): List<Item>? = cacheMap[id]

    fun store(id: String, items: List<Item>) {
        cacheMap[id] = items
    }
}