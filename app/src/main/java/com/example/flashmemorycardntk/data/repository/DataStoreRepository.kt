package com.example.flashmemorycardntk.data.repository

import androidx.datastore.preferences.core.Preferences
import kotlinx.coroutines.flow.Flow

interface DataStoreRepository {

    suspend fun saveLongValue(key: Preferences.Key<Long>, value: Long)
    fun getLongValue(key: Preferences.Key<Long>): Flow<Long?>
}