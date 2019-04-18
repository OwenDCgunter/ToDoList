package gunt0023.example.grouptasklist

import android.provider.BaseColumns

object TodoListDBContract {
    const val DATABASE_VERSION = 2
    const val DATABASE_NAME = "todo_list_db"

    class TodoListItem: BaseColumns {
        companion object {
            const val TABLE_NAME = "todo_list_item"
            const val COLUMN_NAME_TASK = "task_details"
            const val COLUMN_DEADLINE = "task_deadline"
            const val COLUMN_COMPLETED = "task_completed"
        }
    }

}