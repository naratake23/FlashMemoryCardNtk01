package com.example.flashmemorycardntk.model

import com.example.flashmemorycardntk.data.entities.CardEntity
import com.example.flashmemorycardntk.data.entities.GroupEntity

class GlobalProperties {
    companion object {
        var groupsSelected: List<GroupEntity> = emptyList()
        var cardsSelected: List<CardEntity> = emptyList()
    }
}
//-----------------------------------------------------------------------------
