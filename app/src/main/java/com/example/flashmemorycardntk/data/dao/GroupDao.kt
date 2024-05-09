package com.example.flashmemorycardntk.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.flashmemorycardntk.data.entities.GroupEntity
import com.example.flashmemorycardntk.data.entities.GroupWithCardCount
import kotlinx.coroutines.flow.Flow

@Dao
interface GroupDao {

    @Insert
    suspend fun insertGroup(groupEntity: GroupEntity)

    @Update
    suspend fun updateGroup(groupEntity: GroupEntity)

    @Update
    suspend fun updateGroupList(groupEntity: List<GroupEntity>)

    @Delete
    suspend fun deleteGroupList(groupEntity: List<GroupEntity>)

    @Query("SELECT * FROM group_entity")
    fun getAllGroups(): Flow<List<GroupEntity>>

    @Query("SELECT * FROM group_entity WHERE groupId = :groupId")
    suspend fun getGroupById(groupId: Long): GroupEntity



    @Query("SELECT g.*, COUNT(c.cardId) AS cardCount FROM group_entity AS g LEFT JOIN card_entity AS c ON g.groupId = c.groupId AND c.isMarkedForDeletion = 0 GROUP BY g.groupId")
    fun getGroupsWithCardCount(): Flow<List<GroupWithCardCount>>
}