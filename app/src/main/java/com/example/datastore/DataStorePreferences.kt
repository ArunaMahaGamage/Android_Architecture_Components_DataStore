package com.example.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.preferencesKey
import androidx.datastore.preferences.createDataStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking

class DataStorePreferences(context: Context) {

    var dataStore: DataStore<Preferences> = context.createDataStore(
        name = "settings"
    )

    val USER_ID = preferencesKey<Int>("user_id")

    fun writeUser(userId: Int) {
        runBlocking(Dispatchers.IO) {
            writeUserID(userId)
        }
    }
    suspend fun writeUserID(userId: Int) {
        dataStore.edit { settings ->
            settings[USER_ID] = userId
        }
    }

    fun readUserId() :Int {
        val userIdFlow: Flow<Int> = dataStore.data
            .map { preferences ->
                // No type safety.
                preferences[USER_ID] ?: 0
            }
        // you can add flow value to live data
        // below you can see filter out value
        val userId: Int
        runBlocking(Dispatchers.IO) {
            userId = userIdFlow.first()
        }
        return userId
    }
}