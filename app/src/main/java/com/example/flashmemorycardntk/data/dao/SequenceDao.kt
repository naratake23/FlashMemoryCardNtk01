package com.example.flashmemorycardntk.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.flashmemorycardntk.data.entities.SequenceEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SequenceDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSequenceList(sequence: List<SequenceEntity>)

    @Query("DELETE FROM sequence_entity")
    suspend fun deleteAllSequences()

    @Query("SELECT * FROM sequence_entity WHERE groupId = :groupId ORDER BY orderInGroup ASC")
    fun getSequencesListByGroupId(groupId: Long): Flow<List<SequenceEntity>>
}