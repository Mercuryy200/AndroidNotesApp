
package com.example.notesapp.data.model
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tasks")
data class Task(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") val id: Long = 0,
    @ColumnInfo(name = "name") var name: String,
    @ColumnInfo(name = "created_at") var createdAt: Long = System.currentTimeMillis(),
    @ColumnInfo(name = "priority") var priority: Priority = Priority.MOYEN,
    @ColumnInfo(name = "note") var note: String?,
    @ColumnInfo(name = "is_completed") var isCompleted: Boolean = false,
    @ColumnInfo(name = "due_date") var dueDate: Long = System.currentTimeMillis(),
)

enum class Priority{
    BAS, MOYEN, ÉLEVÉ;
}
