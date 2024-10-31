package com.bgbrlk.scoreboardbrlk.repository

interface AppDatastoreInterface {
    suspend fun putBoolean(key: String, value: Boolean)
    suspend fun getBoolean(key: String): Boolean?
    suspend fun putInteger(key: String, value: Int)
    suspend fun getInteger(key: String): Int?
}