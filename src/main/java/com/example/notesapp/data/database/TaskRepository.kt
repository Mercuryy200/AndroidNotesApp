import androidx.lifecycle.LiveData
import com.example.notesapp.data.database.TaskDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class TaskRepository(private val dao: TaskDao) {

    val allTasks: LiveData<List<Task>> = dao.getAll()

    fun getTask(id: Long): LiveData<Task?> = dao.getById(id)

    suspend fun add(task: Task) = withContext(Dispatchers.IO) {
        dao.insert(task)
    }
    suspend fun update(task: Task) = withContext(Dispatchers.IO) {
        dao.update(task)
    }
    suspend fun delete(task: Task) = withContext(Dispatchers.IO) {
        dao.delete(task)
    }
}