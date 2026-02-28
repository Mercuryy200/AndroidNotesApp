package com.example.notesapp.data.preferences

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.notesapp.data.model.Priority
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

enum class SortOrder { BY_DUE_DATE_ASC, BY_DUE_DATE_DESC, BY_PRIORITY }

data class UserPreferences(
    val sortOrder: SortOrder = SortOrder.BY_DUE_DATE_ASC,
    val defaultPriority: Priority = Priority.MEDIUM
)

@Singleton
class UserPreferencesRepository @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {
    companion object {
        private val SORT_ORDER_KEY = stringPreferencesKey("sort_order")
        private val DEFAULT_PRIORITY_KEY = stringPreferencesKey("default_priority")
    }

    val userPreferences: Flow<UserPreferences> = dataStore.data
        .catch { exception ->
            if (exception is IOException) emit(emptyPreferences())
            else throw exception
        }
        .map { preferences ->
            val sortOrder = SortOrder.valueOf(
                preferences[SORT_ORDER_KEY] ?: SortOrder.BY_DUE_DATE_ASC.name
            )
            val defaultPriority = Priority.valueOf(
                preferences[DEFAULT_PRIORITY_KEY] ?: Priority.MEDIUM.name
            )
            UserPreferences(sortOrder, defaultPriority)
        }

    suspend fun toggleSortOrder() {
        dataStore.edit { preferences ->
            val current = SortOrder.valueOf(
                preferences[SORT_ORDER_KEY] ?: SortOrder.BY_DUE_DATE_ASC.name
            )
            preferences[SORT_ORDER_KEY] = when (current) {
                SortOrder.BY_DUE_DATE_ASC -> SortOrder.BY_DUE_DATE_DESC.name
                SortOrder.BY_DUE_DATE_DESC -> SortOrder.BY_DUE_DATE_ASC.name
                SortOrder.BY_PRIORITY -> SortOrder.BY_DUE_DATE_ASC.name
            }
        }
    }

    suspend fun setSortOrder(sortOrder: SortOrder) {
        dataStore.edit { preferences ->
            preferences[SORT_ORDER_KEY] = sortOrder.name
        }
    }

    suspend fun setDefaultPriority(priority: Priority) {
        dataStore.edit { preferences ->
            preferences[DEFAULT_PRIORITY_KEY] = priority.name
        }
    }
}
