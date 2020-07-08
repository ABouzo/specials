package com.bouzo.specials.backend

import java.util.*
import java.util.concurrent.TimeUnit

class SimpleMemoryCache<T> : MemoryCache<T> {

    var cacheLifetime: Long = TimeUnit.HOURS.toMillis(1)

    private var cache: T? = null
    private var lastRefresh: Date = Date(0)

    override fun load(): T? {
        return cache
    }

    override fun save(data: T) {
        cache = data
        lastRefresh = Date()
    }

    override fun isFresh(): Boolean {
        return (Date().time - lastRefresh.time < cacheLifetime && cache != null)
    }
}