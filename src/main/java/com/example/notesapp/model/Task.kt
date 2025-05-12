import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.notesapp.model.Priority

@Entity
data class Task(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    var createdAt: Long,
    var priority: Priority,
    var name: String,
    var note: String?,
    var isCompleted: Boolean = false,
    var dueDate: Long
)
