package com.bouzo.specials.backend

interface MemoryCache<T> {
    fun load(): T?
    fun save(data: T)
    fun isFresh(): Boolean
}