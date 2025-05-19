import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter

@Entity(tableName = "tasks")
data class Task(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") val id: Long = 0,
    @ColumnInfo(name = "name") var name: String,
    @ColumnInfo(name = "created_at") var createdAt: Long = System.currentTimeMillis(),
    @ColumnInfo(name = "priority") var priority: Priority = Priority.MOYEN,
    @ColumnInfo(name = "created_at") var note: String?,
    @ColumnInfo(name = "is_completed") var isCompleted: Boolean = false,
    @ColumnInfo(name = "due_date") var dueDate: Long = System.currentTimeMillis(),
)

enum class Priority{
    BAS, MOYEN, ÉLEVÉ;
}

class Converters {
    @TypeConverter
    fun fromPriority(p: Priority): Int = when(p) {
        Priority.BAS -> 0;
        Priority.MOYEN -> 1;
        Priority.ÉLEVÉ -> 2
    }
    @TypeConverter
    fun toPriority(v: Int): Priority = when(v) {
        0 -> Priority.BAS;
        1 -> Priority.MOYEN;
        2 -> Priority.ÉLEVÉ;
        else -> Priority.MOYEN
    }
}