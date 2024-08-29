package com.example.scoreboardbrlk.ui.score

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first

private val Context.appDataStore: DataStore<Preferences> by preferencesDataStore(name = "app_preferences")

class AppDatastoreRepository(private val context: Context) {
    suspend fun putString(key: String, value: String){
        val preferencesKey = stringPreferencesKey(key)
        context.appDataStore.edit { preferences ->
            preferences[preferencesKey] = value
        }

    }
    suspend fun getString(key: String): String? {
        val preferencesKey = stringPreferencesKey(key)
        val preferences = context.appDataStore.data.first()
        return preferences[preferencesKey]
    }

}