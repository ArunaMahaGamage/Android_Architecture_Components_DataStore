package com.example.datastore

import android.content.Context
import androidx.datastore.core.CorruptionException
import androidx.datastore.core.DataStore
import androidx.datastore.core.Serializer
import androidx.datastore.createDataStore
import androidx.datastore.preferences.protobuf.InvalidProtocolBufferException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import java.io.InputStream
import java.io.OutputStream

class DataStoreProto(context: Context) {

    private val dataStore: DataStore<UserPreferences> =
        context.createDataStore(
            fileName = "user_prefs.pb",
            serializer = UserPreferencesSerializer)

    fun protoDataWrite(completed: Boolean) {
        runBlocking(Dispatchers.IO) {
            updateShowCompleted(completed)
        }
    }
    suspend fun updateShowCompleted(completed: Boolean) {
        dataStore.updateData { preferences ->
            preferences.toBuilder().setShowCompleted(completed).build()
        }
    }

    fun protoDataStoreRead() :Boolean {
        val userPreferencesFlow: Flow<UserPreferences> = dataStore.data
        val showCompleted: Boolean
        runBlocking(Dispatchers.IO) {
            showCompleted = userPreferencesFlow.first().showCompleted
        }
        return showCompleted
    }

    object UserPreferencesSerializer : Serializer<UserPreferences> {
        override val defaultValue: UserPreferences = UserPreferences.getDefaultInstance()
        override fun readFrom(input: InputStream): UserPreferences {
            try {
                return UserPreferences.parseFrom(input)
            } catch (exception: InvalidProtocolBufferException) {
                throw CorruptionException("Cannot read proto.", exception)
            }
        }

        override fun writeTo(t: UserPreferences, output: OutputStream) = t.writeTo(output)
    }
}