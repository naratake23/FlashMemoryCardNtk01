package com.example.flashmemorycardntk.model

import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.longPreferencesKey

enum class EnumDSKeysLong(val key: Preferences.Key<Long>) {
    GROUP_SELECTED_SHUFFLE(key = longPreferencesKey("group_selected_shuffle")),
    GROUP_SELECTED_FAB(key = longPreferencesKey("group_selected_fab")),
    PLAY_CURRENT_CARD(key = longPreferencesKey("play_current_card")),
    PLAY_TOTAL_CARD(key = longPreferencesKey("play_total_card")),
    PLAY_CORRECT_CARD(key = longPreferencesKey("play_correct_card")),
}