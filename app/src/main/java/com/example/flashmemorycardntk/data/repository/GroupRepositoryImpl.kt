package com.example.flashmemorycardntk.data.repository

import com.example.flashmemorycardntk.data.dao.GroupDao
import com.example.flashmemorycardntk.data.entities.GroupEntity
import com.example.flashmemorycardntk.data.entities.GroupWithCardCount
import kotlinx.coroutines.flow.Flow

class GroupRepositoryImpl(private val groupDao: GroupDao) {

    suspend fun insertGroupRepo(group: GroupEntity) {
        groupDao.insertGroup(group)
    }

    suspend fun updateGroupRepo(group: GroupEntity) {
        groupDao.updateGroup(group)
    }

    suspend fun updateGroupListRepo(group: List<GroupEntity>) {
        groupDao.updateGroupList(group)
    }

    suspend fun deleteGroupListRepo(groupList: List<GroupEntity>) {
        groupDao.deleteGroupList(groupList)
    }

    suspend fun getGroupByIdRepo(groupId: Long): GroupEntity {
        return groupDao.getGroupById(groupId)
    }

    fun getAllGroupsRepo(): Flow<List<GroupEntity>> {
        return groupDao.getAllGroups()
    }

    fun getGroupsWithCardCountRepo(): Flow<List<GroupWithCardCount>> {
        return groupDao.getGroupsWithCardCount()
    }
}