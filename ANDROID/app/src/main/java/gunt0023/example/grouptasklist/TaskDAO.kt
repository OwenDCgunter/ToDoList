package gunt0023.example.grouptasklist

import android.arch.persistence.room.*

@Dao
interface TaskDAO {
    @Query("SELECT * FROM " + TodoListDBContract.TodoListItem.TABLE_NAME)
    fun retrieveTaskList(): List<Task>
    @Insert
    fun addNewTask(task: Task): Long
    @Update
    fun updateTask(task: Task)
    @Delete
    fun deleteTask(task: Task)
}