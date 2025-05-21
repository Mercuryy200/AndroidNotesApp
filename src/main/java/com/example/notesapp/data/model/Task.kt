
package com.example.notesapp.data.model
import androidx.annotation.StringRes
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.notesapp.R

@Entity(tableName = "tasks")
data class Task(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") val id: Long = 0,
    @ColumnInfo(name = "name") var name: String,
    @ColumnInfo(name = "created_at") var createdAt: Long = System.currentTimeMillis(),
    @ColumnInfo(name = "priority") var priority: Priority = Priority.MEDIUM,
    @ColumnInfo(name = "note") var note: String?,
    @ColumnInfo(name = "is_completed") var isCompleted: Boolean = false,
    @ColumnInfo(name = "due_date") var dueDate: Long = System.currentTimeMillis(),
)
enum class Priority(@StringRes val labelRes: Int) {
    LOW(R.string.priority_low),
    MEDIUM(R.string.priority_medium),
    HIGH(R.string.priority_high);
}