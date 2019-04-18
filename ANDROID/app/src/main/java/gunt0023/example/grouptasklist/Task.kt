package gunt0023.example.grouptasklist

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.Ignore
import android.arch.persistence.room.PrimaryKey
import android.provider.BaseColumns

@Entity(tableName = TodoListDBContract.TodoListItem.TABLE_NAME)
class Task
{
    @ColumnInfo(name = TodoListDBContract.TodoListItem.COLUMN_NAME_TASK)
    var taskDetails: String? = null

    @ColumnInfo(name = TodoListDBContract.TodoListItem.COLUMN_DEADLINE)
    var taskDeadline: String? = null

    @ColumnInfo(name = TodoListDBContract.TodoListItem.COLUMN_COMPLETED)
    var completed: Boolean? = false

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = BaseColumns._ID)

    var taskId: Long? = null

    @Ignore constructor(taskDetails: String?) {
        this.taskDetails = taskDetails
        //this.completed = completed

    }
   // companion object{
        //@ColumnInfo(name = TodoListDBContract.TodoListItem.COLUMN_COMPLETED)
    //   var completed: Boolean? = false
   // }

    constructor(taskId: Long?, taskDetails: String?, taskDeadline: String?, completed: Boolean?){
        this.taskId = taskId
        this.taskDeadline = taskDeadline
        this.completed = completed
        this.taskDetails = taskDetails
    }

}