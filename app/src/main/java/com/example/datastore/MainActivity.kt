package com.example.datastore

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.datastore.core.CorruptionException
import androidx.datastore.core.DataStore
import androidx.datastore.core.Serializer
import androidx.datastore.createDataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.preferencesKey
import androidx.datastore.preferences.createDataStore
import androidx.datastore.preferences.protobuf.InvalidProtocolBufferException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import java.io.InputStream
import java.io.OutputStream

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        var context = applicationContext

        // Preferences
        var preferences = DataStorePreferences(context)

        preferences.writeUser(546)  // Write Preferance

        var id = preferences.readUserId()   // Read Preferance
        Log.e("Data_Store", "Preferences " + id)



        // Proto Data Store
        var proto = DataStoreProto(context)
        proto.protoDataWrite(true)  // Write Data

        var showCompleted = proto.protoDataStoreRead() // Read Data

        Log.e("Data_Store", "Proto " + showCompleted)

    }
}