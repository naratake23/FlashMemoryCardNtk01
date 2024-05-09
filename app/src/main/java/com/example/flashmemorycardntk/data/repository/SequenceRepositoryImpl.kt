package com.example.flashmemorycardntk.data.repository

import com.example.flashmemorycardntk.data.dao.SequenceDao
import com.example.flashmemorycardntk.data.entities.SequenceEntity
import kotlinx.coroutines.flow.Flow

class SequenceRepositoryImpl(private val sequenceDao: SequenceDao) {

    suspend fun insertSequenceListRepo(sequenceList: List<SequenceEntity>) {
        sequenceDao.insertSequenceList(sequenceList)
    }

    suspend fun deleteAllSequencesRepo() {
        sequenceDao.deleteAllSequences()
    }

    fun getSequencesListByGroupIdRepo(groupId: Long): Flow<List<SequenceEntity>> {
        return sequenceDao.getSequencesListByGroupId(groupId)
    }
}