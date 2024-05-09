package com.example.flashmemorycardntk.data.repository

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class DataStoreRepositoryImpl @Inject constructor(@ApplicationContext private val context: Context) :
    DataStoreRepository {

//    // Mantém uma única instância do DataStore
//    private val dataStoreGeneral by lazy {
//        context.dataStoreGeneral
//    }
//
//    private val Context.dataStoreGeneral: DataStore<Preferences> by preferencesDataStore(name = "general_preferences")

    // Utilize a propriedade de extensão para criar uma instância preguiçosa do DataStore
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "general_preferences")

    // Mantém uma única instância do DataStore acessível pela propriedade 'dataStoreGeneral'
    private val dataStoreGeneral: DataStore<Preferences> by lazy {
        context.dataStore
    }

    override suspend fun saveLongValue(key: Preferences.Key<Long>, value: Long) {
        dataStoreGeneral.edit { preferences ->
            preferences[key] = value
        }
    }

    override fun getLongValue(key: Preferences.Key<Long>): Flow<Long?> {
        return dataStoreGeneral.data
            .map { preferences ->
                preferences[key]
            }
    }
}