package com.bgbrlk.scoreboardbrlk.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first
import javax.inject.Inject

private val Context.appDataStore: DataStore<Preferences> by preferencesDataStore(name = "app_preferences")

class AppDatastore @Inject constructor(private val context: Context) : AppDatastoreInterface {
    override suspend fun putBoolean(key: String, value: Boolean) {
        val preferencesKey = booleanPreferencesKey(key)
        context.appDataStore.edit { preferences ->
            preferences[preferencesKey] = value
        }
    }

    override suspend fun getBoolean(key: String): Boolean? {
        val preferencesKey = booleanPreferencesKey(key)
        val preferences = context.appDataStore.data.first()
        return preferences[preferencesKey]
    }

    override suspend fun putInteger(key: String, value: Int) {
        val preferencesKey = intPreferencesKey(key)
        context.appDataStore.edit { preferences ->
            preferences[preferencesKey] = value
        }
    }

    override suspend fun getInteger(key: String): Int? {
        val preferencesKey = intPreferencesKey(key)
        val preferences = context.appDataStore.data.first()
        return preferences[preferencesKey]
    }
}