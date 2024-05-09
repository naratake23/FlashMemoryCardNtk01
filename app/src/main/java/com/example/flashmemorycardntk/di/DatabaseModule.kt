package com.example.flashmemorycardntk.di

import android.content.Context
import androidx.room.Room
import com.example.flashmemorycardntk.data.DatabaseInitializer
import com.example.flashmemorycardntk.data.dao.CardDao
import com.example.flashmemorycardntk.data.dao.GroupDao
import com.example.flashmemorycardntk.data.dao.SequenceDao
import com.example.flashmemorycardntk.data.database.AppDatabase
import com.example.flashmemorycardntk.data.repository.CardRepositoryImpl
import com.example.flashmemorycardntk.data.repository.CardUpdateServiceRepository
import com.example.flashmemorycardntk.data.repository.CardUpdateServiceRepositoryImpl
import com.example.flashmemorycardntk.data.repository.DataStoreRepository
import com.example.flashmemorycardntk.data.repository.DataStoreRepositoryImpl
import com.example.flashmemorycardntk.data.repository.GroupRepositoryImpl
import com.example.flashmemorycardntk.data.repository.SequenceRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        val callback = DatabaseInitializer.createCallback()
        return Room.databaseBuilder(context.applicationContext, AppDatabase::class.java, "fmcntk_database")
            .addCallback(callback)
            .fallbackToDestructiveMigration()
            .build()
    }

    //provê uma instancia do CardDao
    @Provides
    fun provideCardDao(database: AppDatabase): CardDao = database.cardDao()

    @Provides
    fun provideGroupDao(database: AppDatabase): GroupDao = database.groupDao()

    @Provides
    fun provideSequenceDao(database: AppDatabase): SequenceDao = database.sequenceDao()

    //provê uma instancia do cardRepository
    @Provides
    fun provideCardRepository(cardDao: CardDao): CardRepositoryImpl = CardRepositoryImpl(cardDao)

    @Provides
    fun provideGroupRepository(groupDao: GroupDao): GroupRepositoryImpl =
        GroupRepositoryImpl(groupDao)

    @Provides
    fun provideSequenceRepository(sequenceDao: SequenceDao): SequenceRepositoryImpl =
        SequenceRepositoryImpl(sequenceDao)

    @Singleton
    @Provides
    fun provideDataStoreRepository(@ApplicationContext context: Context): DataStoreRepository =
        DataStoreRepositoryImpl(context)

    @Singleton
    @Provides
    fun provideCardUpdateServiceShuffleNPlayRepository(): CardUpdateServiceRepository =
        CardUpdateServiceRepositoryImpl()
}