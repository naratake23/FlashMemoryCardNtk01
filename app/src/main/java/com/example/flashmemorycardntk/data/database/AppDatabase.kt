package com.example.flashmemorycardntk.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.flashmemorycardntk.data.dao.CardDao
import com.example.flashmemorycardntk.data.dao.GroupDao
import com.example.flashmemorycardntk.data.dao.SequenceDao
import com.example.flashmemorycardntk.data.entities.CardEntity
import com.example.flashmemorycardntk.data.entities.GroupEntity
import com.example.flashmemorycardntk.data.entities.SequenceEntity


@Database(entities = [CardEntity::class, GroupEntity::class, SequenceEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase: RoomDatabase() {

    abstract fun cardDao(): CardDao
    abstract fun groupDao(): GroupDao
    abstract fun sequenceDao(): SequenceDao

}